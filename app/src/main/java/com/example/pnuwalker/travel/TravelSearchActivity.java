package com.example.pnuwalker.travel;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapAddressInfo;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class TravelSearchActivity extends AppCompatActivity {

    EditText searchInputEditText;
    ImageButton backBtn;

    LinearLayout searchResultLayout;
    RelativeLayout mapLayout;
    LinearLayout searchCategoryView;
    LinearLayout mapLayoutParent;
    ListView searchResultListView;
    SearchResultListAdapter adapter;

    ImageButton backToCategoryBtn;
    Button[] categoryBtn;

    ArrayList<TMapPOIItem> searchResult;
    int visiblePosition;

    TMapData tMapData;
    TMapView tMapView;

    double userLon;
    double userLat;

    double currLon;
    double currLat;

    boolean showUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_search);

        searchResult = new ArrayList<>();
        mapLayout = findViewById(R.id.search_map);
        mapLayoutParent = findViewById(R.id.search_map_parent_layout);
        searchInputEditText = findViewById(R.id.search_textinput);
        backBtn = findViewById(R.id.search_back_btn);
        backBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        categoryBtn = new Button[16];
        tMapData = new TMapData();

        Intent startIntent = getIntent();

        userLon = startIntent.getDoubleExtra("userLon",129.08102);
        userLat = startIntent.getDoubleExtra("userLat",35.23380);
        showUser = startIntent.getBooleanExtra("showUser", false);

        currLon = userLon;
        currLat = userLat;

        tMapView = new TMapView(this);
        mapLayout.addView(tMapView);

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(15); //지도 초기 확대수준 설정
        tMapView.setIconVisibility(showUser); //현재 위치를 표시하는 파랑색 아이콘을 표기
        tMapView.setLocationPoint(129.08102472195395, 35.23380020523731); //현재 위치 설정
        tMapView.setCenterPoint(129.08102472195395, 35.23380020523731); // 지도 중심좌표 설정

        //LongClick 액션 설정
        tMapView.setOnLongClickListenerCallback((markerlist, poilist, point) -> {
            currLon = point.getLongitude();
            currLat = point.getLatitude();
            setSearchCenterMarker();
        });

        setSearchCenterMarker();
        //기본 Marker ( 개구멍 + 입/출구 ) 표기
        setEssentialMarker();

        searchCategoryView = findViewById(R.id.search_option_view);
        searchResultLayout = findViewById(R.id.search_result_layout);
        searchResultListView = findViewById(R.id.search_result_list);

        searchInputEditText.setOnKeyListener( (v,keyCode,event)-> searchByEditText(event, keyCode) );

        adapter = new SearchResultListAdapter();
        searchResultListView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println(searchResult.get(position).getPOIName());
            Intent intent = new Intent();
            System.out.println(new SimplePOI(searchResult.get(position)));
            intent.putExtra("result", new SimplePOI(searchResult.get(position)));
            setResult(RESULT_OK, intent);
            finish();
        });

        searchResultListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                TMapPoint p = searchResult.get(visiblePosition).getPOIPoint();
                TMapMarkerItem marker = new TMapMarkerItem();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pnu_marker_search ,options ).copy(Bitmap.Config.ARGB_8888, true);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                paint.setTextAlign(Paint.Align.CENTER);

                Canvas canvas = new Canvas(bm);
                canvas.drawText(Integer.toString(visiblePosition),bm.getWidth()/2 , bm.getHeight()/2 + 2, paint);

                marker.setTMapPoint(p);
                marker.setIcon(bm);
                marker.setPosition(0, (float) 0.8);
                tMapView.addMarkerItem("user_custom", marker);
                tMapView.setCenterPoint(p.getLongitude(), p.getLatitude());
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ( searchResult != null)
                    visiblePosition = firstVisibleItem;
            }
        });
        searchResultListView.setAdapter(adapter);

        backToCategoryBtn = findViewById(R.id.search_back_to_category_btn);
        backToCategoryBtn.setOnClickListener((v) -> { showCategory(); });

        categoryBtnInit();

    }

    //검색 위치 마커 표시
    private void setSearchCenterMarker() {
        TMapMarkerItem centerPoint = new TMapMarkerItem();
        centerPoint.setTMapPoint(new TMapPoint(currLat, currLon));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pnu_marker_search_center , options);
        centerPoint.setPosition(0, (float) 0.8);
        centerPoint.setIcon(bm);

        tMapView.addMarkerItem("center", centerPoint);
    }

    //출입구 마커 표시
    private void setEssentialMarker() {
        String[] entranceArray = getResources().getStringArray(R.array.pnu_entrance);

        for ( int i = 0; i < entranceArray.length; i++ ) {
            String[] temp = entranceArray[i].split(",");

            TMapMarkerItem marker = new TMapMarkerItem();
            TMapPoint p = new TMapPoint(Double.parseDouble(temp[1]), Double.parseDouble(temp[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pnu_marker_exit ,options ).copy(Bitmap.Config.ARGB_8888, true);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            paint.setTextAlign(Paint.Align.CENTER);

            Canvas canvas = new Canvas(bm);
            canvas.drawText(temp[2],bm.getWidth()/2 , bm.getHeight()/2 + 2, paint);

            marker.setTMapPoint(p);
            marker.setIcon(bm);
            marker.setPosition(0, 0);

            tMapView.addMarkerItem("entrance_" + i, marker);
        }
    }

    //카테고리 버튼 초기화
    private void categoryBtnInit() {
        int[] id = { R.id.search_category1_btn, R.id.search_category2_btn, R.id.search_category3_btn,
                     R.id.search_category4_btn, R.id.search_category5_btn, R.id.search_category6_btn,
                     R.id.search_category7_btn, R.id.search_category8_btn, R.id.search_category9_btn,
                     R.id.search_category10_btn, R.id.search_category11_btn};

        for (int i = 0; i < 11; i++) {
            categoryBtn[i] = findViewById(id[i]);
            categoryBtn[i].setOnClickListener((v) -> serachByCategory(v) );

            //clopy
            if (i == 9)
                categoryBtn[i].setOnClickListener((v) -> showClopy() );
        }


    }

    private void search(TMapPoint point, String keyWord) {
        Log.d("Search", "Start Search");
        tMapData.findAroundKeywordPOI(point, keyWord , 6, 200, arrayList -> {
            searchResult = arrayList;
            if (searchResult.size() == 0) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(() -> Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show(), 0);
                return;
            } else {
                visiblePosition = 0;
                adapter.clear();
                for ( TMapPOIItem p : searchResult ) {
                    TMapPolyLine polyLine = new TMapPolyLine();
                    polyLine.addLinePoint(p.getPOIPoint());
                    polyLine.addLinePoint(new TMapPoint(userLat, userLon));
                    adapter.addItem(p, polyLine.getDistance());
                }

                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    searchCategoryView.setVisibility(View.GONE);
                    searchResultLayout.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mapLayoutParent.getLayoutParams());
                    params.weight = 40f;
                    mapLayoutParent.setLayoutParams(params);
                });
            }
        });
    }

    private void serachByCategory(View v) {
        Button b = findViewById(v.getId());
        String keyWord = b.getText().toString();

        if (keyWord.equals("문화사"))
            keyWord = "print";

        TMapPoint point = new TMapPoint(currLat, currLon);
        search(point, keyWord);
    }

    private boolean searchByEditText(KeyEvent e, int keyCode) {
        if ((e.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            String keyWord = searchInputEditText.getText().toString();
            TMapPoint point = new TMapPoint(currLat, currLon);
            search(point, keyWord);
            return true;
        } else {
            return false;
        }

    }

    private void showCategory() {
        searchCategoryView.setVisibility(View.VISIBLE);
        searchResultLayout.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mapLayoutParent.getLayoutParams());
        params.weight = 65f;
        mapLayoutParent.setLayoutParams(params);
    }

    private void showClopy() {
        adapter.clear();
        searchResult.clear();
        String[] clopyValue = getResources().getStringArray(R.array.pnu_clopy);

        for(String s : clopyValue) {
            String[] temp = s.split(","); //name, lon, lat
            TMapPOIItem p = new TMapPOIItem();
            p.noorLon = temp[1];
            p.noorLat = temp[2];
            p.name = temp[0];
            p.desc = "";
            p.upperBizName = "시설";
            p.middleBizName = "구역내시설물";
            p.lowerBizName = "학교내시설물";
            p.roadName = "";
            p.upperAddrName = "";
            p.middleAddrName = "";
            p.lowerAddrName = "";
            p.firstNo = "";
            p.secondNo = "";
            p.buildingNo1 = "";
            p.buildingNo2 = "";
            p.telNo = "";

            TMapPolyLine polyLine = new TMapPolyLine();
            polyLine.addLinePoint(p.getPOIPoint());
            polyLine.addLinePoint(new TMapPoint(userLat, userLon));

            adapter.addItem(p, polyLine.getDistance());
            searchResult.add(p);
        }

        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            searchCategoryView.setVisibility(View.GONE);
            searchResultLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mapLayoutParent.getLayoutParams());
            params.weight = 40f;
            mapLayoutParent.setLayoutParams(params);
        });
    }

}
