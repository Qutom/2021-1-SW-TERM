package com.example.pnuwalker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TravelFragment extends Fragment implements  View.OnClickListener {
    SlidingUpPanelLayout slidingUpPanelLayout;
    TMapView tMapView;
    TMapData tMapData;
    RelativeLayout mapLayout;
    TMapPoint currentLocation;
    EditText inputText;

    Button searchCafeBtn;
    Button searchFoodBtn;
    Button searchSupplyBtn;
    Button searchPCBtn;
    Button searchPrintBtn;
    Button searchConStoreBtn;
    Button searchBookStoreBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment, container, false);
        init(view);

        return view;
    }

    @Override
    public void onClick(View v) {

        String temp = null;

        switch (v.getId()) {
            case R.id.search_cafe_btn :
                temp = "카페";
                break;
            case R.id.search_food_btn :
                temp = "음식점";
                break;
            case R.id.search_print_btn :
                temp = "print";
                break;
            case R.id.search_supplies_btn :
                temp = "문구점";
                break;
            case R.id.search_constore_btn :
                temp = "편의점";
                break;
            case R.id.search_pc_btn :
                temp = "PC방";
                break;
            case R.id.search_bookstore_btn :
                temp = "서점";
                break;
            case R.id.text_search_btn :
                String text = String.valueOf(inputText.getText());
                System.out.println(text);
                inputText.setText("");
                searchKeywordPOI(currentLocation, text, 3);
                break;
            default :
                temp = "";
        }

        searchKeywordPOI(currentLocation, temp, 3);
    }

    //TMap,TMapData, Button등 각종 객체를 생성하고 설정
    private void init(View view) {
        slidingUpPanelLayout = view.findViewById(R.id.main_layout);
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
        tMapView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
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

        //필수 마커 생성


        //필수 PolyLine 생성


    }

    private void addEssentialMarker() {
        //부산대 건물에 대한 마커 생성

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
            MarkerOverlay marker = new MarkerOverlay(getContext(), names.get(i), "s_result" + i);;
            marker.setPosition(0.2f,0.2f);
            marker.setID("s_result" + i);
            marker.setTMapPoint(points.get(i));
            marker.setIcon(icon);
            tMapView.addMarkerItem2(marker.getID(), marker);
        }

    }
}
