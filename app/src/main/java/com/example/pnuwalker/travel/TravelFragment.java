package com.example.pnuwalker.travel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
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

import androidx.fragment.app.Fragment;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TravelFragment extends Fragment implements View.OnClickListener {
    SlidingUpPanelLayout slidingUpPanelLayout;
    TMapView tMapView;
    TMapData tMapData;
    RelativeLayout mapLayout;
    MarkerInfoLayout markerInfo;
    TMapPoint currentLocation;
    EditText inputText;

    Button searchCafeBtn;
    Button searchFoodBtn;
    Button searchSupplyBtn;
    Button searchPCBtn;
    Button searchPrintBtn;
    Button searchConStoreBtn;
    Button searchBookStoreBtn;

    TMapPoint pathFindStartPoint;
    TMapPoint pathFindEndPoint;
    int pathFindPointSelectMode = 0; // 0 = None, 1 = Select Start, 2 = Select End

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
        markerInfo = new MarkerInfoLayout(view);

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

        //맵 액션 설정
        //Long Click시 이름이 (ReverseGeoCoding) 인 이름의 마커를 생성하고 MarkerInfo 창을 띄움
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point) {
                
                markerInfo.setBuildingNumber("");
                markerInfo.setName("사용자 선택");
                markerInfo.setVisibility(View.VISIBLE);
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
                return false;
            }
        });

        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
                Intent intent = new Intent(getActivity(), PNUbuildingDetailActivity.class);
                startActivity(intent);
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

                } else { //선택 해제됨 (또는 작업이 완료됨)

                }
            }
        });
        //버튼 설정
        /*
        searchCafeBtn = (Button)view.findViewById(R.id.search_cafe_btn);
        searchFoodBtn = (Button)view.findViewById(R.id.search_food_btn);
        searchSupplyBtn = (Button)view.findViewById(R.id.search_supplies_btn);
        searchPrintBtn = (Button)view.findViewById(R.id.search_print_btn);
        searchConStoreBtn = (Button)view.findViewById(R.id.search_constore_btn);
        searchBookStoreBtn = (Button)view.findViewById(R.id.search_bookstore_btn);
        searchPCBtn = (Button)view.findViewById(R.id.search_pc_btn);

        searchCafeBtn.setOnClickListener(this);
        searchFoodBtn.setOnClickListener(this);
        searchSupplyBtn.setOnClickListener(this);
        searchPrintBtn.setOnClickListener(this);
        searchConStoreBtn.setOnClickListener(this);
        searchBookStoreBtn.setOnClickListener(this);
        searchPCBtn.setOnClickListener(this);
        */
        //필수 마커 생성
        addEssentialMarker();

        //필수 PolyLine 생성
        //addEssentialPolyLine();


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



        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
            MarkerOverlay marker = new MarkerOverlay(getContext(), Integer.toString(i), names.get(i), descriptions.get(i) ,"s_result" + i , markerInfo);;
            marker.setID("s_result" + i);
            marker.setTMapPoint(points.get(i));
            marker.setIcon(icon);
            tMapView.addMarkerItem2(marker.getID(), marker);
        }

    }

    public Bitmap makeTextedIcon(int drawableId, String text, int TextSize, BitmapFactory.Options options){

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
