package com.example.pnuwalker.travel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pnuwalker.AddScheduleActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.pathfind.FindPath;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class TravelFragment extends Fragment {
    TMapView tMapView;
    TMapData tMapData;
    RelativeLayout mapLayout;
    TableLayout searchLayout;
    MarkerInfoLayout markerInfo;
    FindPath findPNUPath;
    TMapPoint currentLocation;

    LocationManager locationManager;
    LocationListener locationListener;

    ImageButton gpsBtn;
    ImageButton locationBtn;
    TMapPoint pathFindStartPoint;
    TMapPoint pathFindEndPoint;

    boolean showUser = false;
    boolean isLongClick = false;
    boolean isFirstGpsMove = false;
    boolean isTracking = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeGps();
    }

    //검색 결과를 받아 설명창과 마커를 표기
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tMapView.removeMarkerItem("user_custom");

            TMapMarkerItem marker = new TMapMarkerItem();
            SimplePOI result = (SimplePOI) data.getSerializableExtra("result");
            TMapPoint p = new TMapPoint(result.lat, result.lon);
            marker.setTMapPoint(p);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot, options));
            marker.setPosition(0, (float) 0.8);

            tMapView.addMarkerItem("user_custom", marker);
            markerInfo.setBuildingNumber("");
            markerInfo.setName(result.name);
            markerInfo.show();
            markerInfo.setTMapPoint(p);
            markerInfo.setBtnDetailActive(true);

            TMapPOIItem poi = new TMapPOIItem();
            poi.name = result.name;
            poi.telNo = result.telNo;
            poi.upperAddrName = result.upperAddrName;
            poi.middleAddrName = result.middleAddrName;
            poi.lowerAddrName = result.lowerAddrName;
            poi.firstNo = result.firstNo;
            poi.secondNo = result.secondNo;
            poi.upperBizName = result.upperBizName;
            poi.middleBizName = result.middleBizName;
            poi.lowerBizName = result.lowerBizName;
            poi.roadName = result.roadName;
            poi.buildingNo1 = result.buildingNo1;
            poi.buildingNo2 = result.buildingNo2;
            poi.noorLon = Double.toString(result.lon);
            poi.noorLat = Double.toString(result.lat);
            markerInfo.setPOI(poi);
            markerInfo.setIsPNU(false);

            tMapView.setCenterPoint(result.lon, result.lat);
        }
    }

    //TMap,TMapData, Button등 각종 객체를 생성하고 설정
    private void init(View view) {
        findPNUPath = new FindPath(getActivity());
        mapLayout = view.findViewById(R.id.map_layout);
        tMapView = new TMapView(getActivity());
        mapLayout.addView(tMapView);
        tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        //=============================================================================================================================
        //ShowUser를 설정하는 함수가 필요(GPS와 연관)
        //=============================================================================================================================

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(17); //지도 초기 확대수준 설정
        tMapView.setLocationPoint(129.08102, 35.23380 ); //현재 위치 설정
        tMapView.setCenterPoint(129.08102472195395, 35.23380020523731); // 지도 중심좌표 설정
        tMapView.setTMapLogoPosition(TMapView.TMapLogoPositon.POSITION_BOTTOMLEFT);

        currentLocation = new TMapPoint(35.23380020523731, 129.08102472195395 );
        tMapData = new TMapData();
        
        //searchLayout 설정
        searchLayout = view.findViewById(R.id.call_search_layout);
        searchLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TravelSearchActivity.class);
            TMapPoint point = showUser ? tMapView.getLocationPoint() : tMapView.getCenterPoint();
            intent.putExtra("userLon", point.getLongitude());
            intent.putExtra("userLat", point.getLatitude());
            intent.putExtra("showUser", showUser);
            startActivityForResult(intent, 1000);
        });

        //markerInfoLayout 설정
        markerInfo = new MarkerInfoLayout(view);

        //버튼 설정
        gpsBtn = view.findViewById(R.id.travel_gps_btn);
        gpsBtn.setOnClickListener((v) -> toggleGps());

        locationBtn = view.findViewById(R.id.travel_location_btn);
        locationBtn.setOnClickListener((v) -> setTrackingMode());

        markerInfo.setBtnClickListener(MarkerInfoLayout.DETAIL, v -> {
            Intent intent = new Intent(getActivity(), TravelDetailActivity.class);

            intent.putExtra("is_pnu", markerInfo.isPNU());
            if( markerInfo.isPNU() ) //부산대 관련 건물인가?
                intent.putExtra("pnu", markerInfo.getPNUInfo());
            else
                intent.putExtra("poi", markerInfo.getSimplePOI());

            startActivity(intent);
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.START, v -> {
            TMapPoint point = markerInfo.getTMapPoint();
            pathFindStartPoint = point;

            tMapView.removeMarkerItem("start");

            TMapMarkerItem marker = new TMapMarkerItem();
            marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.path_start));
            marker.setPosition(0, (float) 0.8);
            marker.setTMapPoint(point);
            tMapView.addMarkerItem("start" ,marker);

            if ( pathFindEndPoint != null && !isSamePoint(pathFindStartPoint, pathFindEndPoint) ) {
                tMapView.removeTMapPolyLine("path1");
                findPNUPath.findPath(pathFindStartPoint, pathFindEndPoint, false);
                TMapPolyLine line = findPNUPath.getPolyLine();
                line.setLineColor(Color.MAGENTA);
                line.setLineWidth(5f);
                line.setOutLineWidth(0);
                if (line == null)
                    System.out.println("null");
                else
                    tMapView.addTMapPolyLine("path1", line);
                System.out.println(String.format("%f, %f -> %f, %f로 길찾기를 수행", pathFindStartPoint.getLatitude() , pathFindStartPoint.getLongitude()
                        , pathFindEndPoint.getLatitude(), pathFindEndPoint.getLongitude()) );
            }
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.END, v -> {
            TMapPoint point = markerInfo.getTMapPoint();
            pathFindEndPoint = point;

            tMapView.removeMarkerItem("end");

            TMapMarkerItem marker = new TMapMarkerItem();
            marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.path_end));
            marker.setPosition(0, (float) 0.8);
            marker.setTMapPoint(point);
            tMapView.addMarkerItem("end" ,marker);

            if ( pathFindStartPoint != null && !isSamePoint(pathFindStartPoint, pathFindEndPoint) ) {
                tMapView.removeTMapPolyLine("path1");
                findPNUPath.findPath(pathFindStartPoint, pathFindEndPoint, false);
                TMapPolyLine line = findPNUPath.getPolyLine();
                line.setLineColor(Color.MAGENTA);
                line.setLineWidth(5f);
                line.setOutLineWidth(0);
                if (line == null)
                    System.out.println("null");
                else
                    tMapView.addTMapPolyLine("path1", line);
                System.out.println(String.format("%f, %f -> %f, %f로 길찾기를 수행", pathFindStartPoint.getLatitude() , pathFindStartPoint.getLongitude()
                , pathFindEndPoint.getLatitude(), pathFindEndPoint.getLongitude()) );
            }
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.SCHEDULE, v -> {
            Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
            TMapPoint point = markerInfo.getTMapPoint();

            intent.putExtra("destName" , markerInfo.getName());
            intent.putExtra("destLat"  , markerInfo.getLatitude());
            intent.putExtra("destLon"  , markerInfo.getLongitude());
            startActivity(intent);
        });

        //맵 액션 설정
        //Long Click시 이름이 (ReverseGeoCoding) 인 이름의 마커를 생성하고 MarkerInfo 창을 띄움
        tMapView.setOnLongClickListenerCallback((markerlist, poilist, point) -> {
            isLongClick = true;
            TMapMarkerItem marker = new TMapMarkerItem();

            marker.setTMapPoint(point);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot, options));
            marker.setPosition(0, (float) 0.8);

            tMapView.addMarkerItem("user_custom", marker);
            markerInfo.setBuildingNumber("");
            markerInfo.setName("사용자 선택");
            markerInfo.show();
            markerInfo.setTMapPoint(point);
            markerInfo.setBtnDetailActive(false);
            markerInfo.setIsPNU(false);
        });

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            TMapPoint prevCenterPoint;
            int prevZoom;
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                TMapPoint currCenterPoint = tMapView.getCenterPoint();
                int currZoom = tMapView.getZoomLevel();
                if ( !isLongClick ) {
                    if ( isSamePoint(prevCenterPoint, currCenterPoint) && currZoom == prevZoom) {
                        if (markerInfo.getVisibility() == View.VISIBLE) {
                            markerInfo.hide();
                            markerInfo.setVisibility(View.INVISIBLE);
                            tMapView.removeMarkerItem("user_custom");
                        }
                    }

                }

                isLongClick = false;
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                prevCenterPoint = tMapView.getCenterPoint();
                prevZoom = tMapView.getZoomLevel();
                return false;
            }
        });

        //필수 마커 생성
        addEssentialMarker();

        //필수 PolyLine 생성
        addEssentialPolyLine();

        //GPS
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                System.out.println("SSSS" + location.getLongitude() + " " + location.getLatitude());
                TMapPoint p = tMapView.getCenterPoint();
                tMapView.setCenterPoint(p.getLongitude(), p.getLatitude());
                tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
                currentLocation = new TMapPoint(location.getLatitude(), location.getLongitude());

                if (isFirstGpsMove) {
                    tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
                    isFirstGpsMove = false;
                }
            }

            @Override public void onProviderEnabled(@NonNull String provider) {}
            @Override public void onProviderDisabled(@NonNull String provider) {}
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

    }

    private void addEssentialPolyLine() {
        String[] pnuStrings = getResources().getStringArray(R.array.pnu_shortcut);
        for (int i = 0;  i < pnuStrings.length; i++) {
            String[] temp = pnuStrings[i].split(":");
            TMapPolyLine polyLine = new TMapPolyLine();

            polyLine.setLineColor(Color.parseColor("#FFF38B"));
            polyLine.setOutLineColor(Color.parseColor("#FFF38B"));
            polyLine.setOutLineWidth(0);
            polyLine.setLineWidth(10);

            for (int j = 0; j < temp.length; j++) {
                String[] strValue = temp[j].split(",");
                polyLine.addLinePoint(new TMapPoint(Double.parseDouble(strValue[1]), Double.parseDouble(strValue[0])));
            }

            tMapView.addTMapPolyLine("pnu_polyline_" + i, polyLine);
        }
    }

    private void addEssentialMarker() {
        //부산대 건물에 대한 마커 생성
        String[] pnuStrings = getResources().getStringArray(R.array.pnu_buildings);
        String[] pnuBuildingInfo = getResources().getStringArray(R.array.pnu_buildings_detail_info);
        TypedArray images = getResources().obtainTypedArray(R.array.pnu_buildings_detail_image);

        //pnuBuildingInfo에 대한 Map 생성
        HashMap<Integer, String> pnuInfoMap = new HashMap<>(20);
        for ( String s : pnuBuildingInfo ) {
            String[] temp = s.split("#");
            pnuInfoMap.put(Integer.parseInt(temp[0]), temp[1]);
        }

        int[] iconId = {R.drawable.pnu_marker_red,
                        R.drawable.pnu_marker_orange,
                        R.drawable.pnu_marker_yellow,
                        R.drawable.pnu_marker_green,
                        R.drawable.pnu_marker_cyan,
                        R.drawable.pnu_marker_lightblue,
                        R.drawable.pnu_marker_purple,
                        R.drawable.pnu_marker_library,
                        R.drawable.pnu_marker_cafeteria};

        int number;
        double latitude;
        double longtitude;

        //temp = [number, name, latitude, longtitude, descrption , icon]
        String[] temp;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        for (int i = 0; i < pnuStrings.length; i++ ) {
            int icon;
            temp = pnuStrings[i].split(":");

            number = Integer.parseInt(temp[0]);
            latitude = Double.parseDouble(temp[2]);
            longtitude = Double.parseDouble(temp[3]);


            String id = "pnu_" + i;
            MarkerOverlay marker = new MarkerOverlay(getContext(), temp[0] , temp[1], temp[4], id, markerInfo);
            marker.setID(id);
            marker.setTMapPoint(new TMapPoint(latitude, longtitude));

            if ( temp[5].equals("none") ) {
                icon = iconId[(number/100) - 1];
                marker.setIcon(makeTextedIcon(iconId[(number/100) - 1], temp[0], 25, Color.BLACK , options));
            } else {
                icon = iconId[Integer.valueOf(temp[5])];
                marker.setIcon(BitmapFactory.decodeResource(getResources(), iconId[Integer.valueOf(temp[5])], options));
            }
            
            //부산대 자세히 정보 추가
            if ( pnuInfoMap.containsKey(number) ) {
                String[] info = pnuInfoMap.get(number).split("%"); //number$time정보:imageIndex정보 (정보가 없을시 null로 표기됨)

                ArrayList<Integer> imageList = new ArrayList<>();

                if ( !info[1].equals("null") ) { //image 정보가 있을때,
                    String[] indexInfo = info[1].split(",");

                    for (String s : indexInfo) {
                        int index = Integer.parseInt(s);
                        imageList.add(images.getResourceId(index,-1));
                    }
                }

                PNUBuildingInfo pnuInfo = new PNUBuildingInfo(temp[1], longtitude, latitude, info[0], imageList, number);
                marker.setPnuInfo(pnuInfo);
            } else {
                PNUBuildingInfo pnuInfo = new PNUBuildingInfo(temp[1], longtitude, latitude, number);
                marker.setPnuInfo(pnuInfo);
            }

            tMapView.addMarkerItem2(marker.getID(), marker);
        }

        //CLOPY 마커 생성

        //버스 정류장?
    }

    public Bitmap makeTextedIcon(int drawableId, String text, int TextSize, int textColor, BitmapFactory.Options options) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId ,options ).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(textColor);
        paint.setTextSize(TextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text,bm.getWidth()/2 , bm.getHeight()/2 + 2, paint);

        return bm;
    }

    private boolean isSamePoint(TMapPoint p1, TMapPoint p2) {
        return (p1.getLongitude() == p2.getLongitude()) && (p1.getLatitude() == p2.getLatitude());
    }

    private void toggleGps() {
        if ( showUser ) { //gps 끔
            gpsBtn.setBackground(getResources().getDrawable(R.drawable.gps_deactivatd));
            tMapView.setSightVisible(false);
            tMapView.setIconVisibility(false);
            closeGps();
        } else {
            gpsBtn.setBackground(getResources().getDrawable(R.drawable.gps_activated));
            tMapView.setSightVisible(true);
            tMapView.setIconVisibility(true);
            openGps();
        }
    }

    private void openGps() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, locationListener);
            showUser = true;
            isFirstGpsMove = true;
        }
    }

    private void closeGps() {
        Log.d("GPS", "end GPS");
        showUser = false;
        locationManager.removeUpdates(locationListener);
    }

    private void setTrackingMode() {
        isTracking = !isTracking;
        tMapView.setTrackingMode(isTracking);

        if ( isTracking ) {
            locationBtn.setBackgroundResource(R.drawable.gps_activated);
        } else {
            locationBtn.setBackgroundResource(R.drawable.gps_deactivatd);
        }
    }


}
