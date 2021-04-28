package com.example.pnuwalker.travel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pnuwalker.AddScheduleActivity;
import com.example.pnuwalker.FindPNUPath;
import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TravelFragment extends Fragment implements View.OnClickListener ,TMapGpsManager.onLocationChangedCallback {
    TMapView tMapView;
    TMapData tMapData;
    RelativeLayout mapLayout;
    MarkerInfoLayout markerInfo;
    FindPNUPath findPNUPath;
    TMapPoint currentLocation;
    EditText inputText;
    TMapGpsManager gps;

    TMapPoint pathFindStartPoint;
    TMapPoint pathFindEndPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        String temp = null;
        searchKeywordPOI(currentLocation, temp, 3);
    }

    //TMap,TMapData, Button등 각종 객체를 생성하고 설정
    private void init(View view) {
        findPNUPath = new FindPNUPath();
        mapLayout = view.findViewById(R.id.map_layout);
        tMapView = new TMapView(getActivity());
        mapLayout.addView(tMapView);
        tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(17); //지도 초기 확대수준 설정
        tMapView.setSightVisible(true); //현재 보고있는 방향을 표시
        tMapView.setIconVisibility(true); //현재 위치를 표시하는 파랑색 아이콘을 표기
        tMapView.setLocationPoint(129.08102472195395, 35.23380020523731 ); //현재 위치 설정
        tMapView.setCenterPoint(129.08102472195395, 35.23380020523731); // 지도 중심좌표 설정

        currentLocation = new TMapPoint(35.23380020523731, 129.08102472195395 );
        tMapData = new TMapData();

        //markerInfoLayout 설정
        markerInfo = new MarkerInfoLayout(view);

        //버튼 설정
        markerInfo.setBtnClickListener(MarkerInfoLayout.DETAIL, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PNUbuildingDetailActivity.class);
                startActivity(intent);
            }
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.START, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint point = markerInfo.getTMapPoint();
                pathFindStartPoint = point;

                tMapView.removeMarkerItem("start");

                TMapMarkerItem marker = new TMapMarkerItem();
                marker.setTMapPoint(point);
                tMapView.addMarkerItem("start" ,marker);

                if ( pathFindEndPoint != null ) {
                    tMapView.removeTMapPolyLine("path1");
                    //조정된 길찾기 알고리즘으로 대체될것임
                    findPNUPath.findPath(pathFindStartPoint, pathFindEndPoint, false);
                    TMapPolyLine line = findPNUPath.getPolyLine();
                    if (line == null)
                        System.out.println("null");
                    else
                        tMapView.addTMapPolyLine("path1", line);
                    System.out.println(String.format("%f, %f -> %f, %f로 길찾기를 수행", pathFindStartPoint.getLatitude() , pathFindStartPoint.getLongitude()
                            , pathFindEndPoint.getLatitude(), pathFindEndPoint.getLongitude()) );
                }
            }
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.END, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint point = markerInfo.getTMapPoint();
                pathFindEndPoint = point;

                tMapView.removeMarkerItem("end");

                TMapMarkerItem marker = new TMapMarkerItem();
                marker.setTMapPoint(point);
                tMapView.addMarkerItem("end" ,marker);

                if ( pathFindStartPoint != null ) {
                    tMapView.removeTMapPolyLine("path1");
                    //조정된 길찾기 알고리즘으로 대체될것임
                    findPNUPath.findPath(pathFindStartPoint, pathFindEndPoint, false);
                    TMapPolyLine line = findPNUPath.getPolyLine();
                    if (line == null)
                        System.out.println("null");
                    else
                        tMapView.addTMapPolyLine("path1", line);
                    System.out.println(String.format("%f, %f -> %f, %f로 길찾기를 수행", pathFindStartPoint.getLatitude() , pathFindStartPoint.getLongitude()
                    , pathFindEndPoint.getLatitude(), pathFindEndPoint.getLongitude()) );
                }
            }
        });

        markerInfo.setBtnClickListener(MarkerInfoLayout.SCHEDULE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                TMapPoint point = markerInfo.getTMapPoint();

                intent.putExtra("destName" , markerInfo.getName());
                intent.putExtra("destLat"  , markerInfo.getLatitude());
                intent.putExtra("destLon"  , markerInfo.getLongitude());
                startActivity(intent);
            }
        });

        //맵 액션 설정
        //Long Click시 이름이 (ReverseGeoCoding) 인 이름의 마커를 생성하고 MarkerInfo 창을 띄움
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point) {

                TMapMarkerItem marker = new TMapMarkerItem();

                marker.setTMapPoint(point);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot, options));
                marker.setPosition(0, (float) 0.8);

                tMapView.addMarkerItem("user_custom", marker);
                markerInfo.setBuildingNumber("");
                markerInfo.setName("사용자 선택");
                markerInfo.setVisibility(View.VISIBLE);
                markerInfo.setTMapPoint(point);
                markerInfo.setBtnDetailActive(false);
            }
        });

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {

                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                markerInfo.setVisibility(View.INVISIBLE);
                tMapView.removeMarkerItem("user_custom");
                return false;
            }
        });

        //edittext 설정
        inputText = (EditText)view.findViewById(R.id.search_textinput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = String.valueOf(inputText.getText());
                    System.out.println(text);

                    if ( !text.equals("") )
                        searchKeywordPOI(currentLocation, text, 3);

                    return true;
                }
                return false;
            }
        });

        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( hasFocus ) { //선택됨
                    //검색 Activity를 띄운다
                }
            }
        });

        //필수 마커 생성
        addEssentialMarker();

        //필수 PolyLine 생성
        //addEssentialPolyLine();

        //gps 설정
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }


    }

    //gps 연동
    @Override
    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        tMapView.setCenterPoint(lon, lat);
        tMapView.setLocationPoint(lon , lat);
        System.out.println("aaa");
    }

    private void addEssentialMarker() {
        //부산대 건물에 대한 마커 생성
        String[] pnuStrings = getResources().getStringArray(R.array.pnu_buildings);
        int[] iconId = {R.drawable.pnu_marker_red,
                        R.drawable.pnu_marker_orange,
                        R.drawable.pnu_marker_yellow,
                        R.drawable.pnu_marker_green,
                        R.drawable.pnu_marker_cyan,
                        R.drawable.pnu_marker_lightblue,
                        R.drawable.pnu_marker_purple,
                        R.drawable.pnu_marker_library};

        int number;
        String name;
        String description;
        double latitude;
        double longtitude;

        //temp = [number, name, latitude, longtitude, descrption , icon]
        String[] temp = new String[6];

        Activity activity = getActivity();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        for (int i = 0; i < pnuStrings.length; i++ ) {
            temp = pnuStrings[i].split(":");
            System.out.println(temp[0]);

            number = Integer.valueOf(temp[0]);
            name = temp[1];
            latitude = Double.valueOf(temp[2]);
            longtitude = Double.valueOf(temp[3]);
            description = temp[4];

            String id = "pnu_" + i;
            MarkerOverlay marker = new MarkerOverlay(getContext(), temp[0] , temp[1], temp[4], id, markerInfo);
            marker.setID(id);
            marker.setTMapPoint(new TMapPoint(latitude, longtitude));

            if ( temp[5].equals("none") ) {
                marker.setIcon(makeTextedIcon(iconId[(int)(number/100) - 1], temp[0], 22 , options));
            } else {
                marker.setIcon(BitmapFactory.decodeResource(getResources(), iconId[Integer.valueOf(temp[5])], options));
            }

            tMapView.addMarkerItem2(marker.getID(), marker);


        }


        //CLOPY 마커 생성

        //버스 정류장?
    }

    private void drawCircle(TMapPoint point, int km) {
        tMapView.removeTMapCircle("search_area_circle");

        TMapCircle circle = new TMapCircle();
        circle.setCenterPoint(point);
        circle.setRadius(km * 1000);
        circle.setAreaColor(Color.GREEN);
        circle.setAreaAlpha(14);
        tMapView.addTMapCircle("search_area_circle", circle);
    }

    private void searchKeywordPOI(TMapPoint searchPoint, String keyword, int radius) {
        //Keyword POI 검색
        tMapData.findAroundKeywordPOI(searchPoint, keyword, radius, 200, new TMapData.FindAroundKeywordPOIListenerCallback() {
            @Override
            public void onFindAroundKeywordPOI(ArrayList<TMapPOIItem> arrayList) {
                if (arrayList == null) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    }, 0);
                    return;
                }

                ArrayList<TMapPoint> points = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> descriptions = new ArrayList<>();

                for (TMapPOIItem poi : arrayList) {
                    points.add(poi.getPOIPoint());
                    names.add(poi.getPOIName());

                    String desc = poi.getPOIContent();
                    if (desc == null)
                        descriptions.add(" ");
                    else
                        descriptions.add(desc);
                }

                //마커 그리기
                addMarkerFromList(points, names, descriptions);
                //circle 그리기(검색 반경)
                drawCircle(searchPoint, radius);
            }
        });
    }

    
    //검색 결과로 생성된 마커(최대 200개)를 모두 삭제
    private void removeSearchResultMarker() {
        for (int i = 0; i < 200; i++ )
            tMapView.removeMarkerItem2("s_result" + i);
    }
    
    //매개변수로 주어진 리스트들을 지도에 다중마커로 표시
    private void addMarkerFromList(ArrayList<TMapPoint> points , ArrayList<String> names, ArrayList<String> descriptions) {

        removeSearchResultMarker();
        Activity activity = getActivity();

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.poi_dot, options);


        for (int i = 0; i < points.size(); i++) {
            String id = "s_result" + i;
            MarkerOverlay marker = new MarkerOverlay(getContext(), "", names.get(i), descriptions.get(i) ,"s_result" + i , markerInfo);
            marker.setID("s_result" + i);
            marker.setTMapPoint(points.get(i));
            marker.setIcon(icon);
            tMapView.addMarkerItem2(marker.getID(), marker);
        }

    }

    public Bitmap makeTextedIcon(int drawableId, String text, int TextSize, BitmapFactory.Options options) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId ,options ).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.WHITE);
        paint.setTextSize(TextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text,bm.getWidth()/2 , bm.getHeight()/2, paint);

        return bm;
    }

}
