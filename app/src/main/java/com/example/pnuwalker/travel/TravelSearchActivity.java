package com.example.pnuwalker.travel;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class TravelSearchActivity extends AppCompatActivity {

    EditText searchInputEditText;
    ImageButton backBtn;

    LinearLayout searchResultLayout;
    RelativeLayout mapLayout;
    LinearLayout searchCategoryView;
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
    boolean showUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_search);

        mapLayout = findViewById(R.id.search_map);
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
        tMapView = new TMapView(this);
        mapLayout.addView(tMapView);

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(15); //지도 초기 확대수준 설정
        tMapView.setSightVisible(showUser); //현재 보고있는 방향을 표시
        tMapView.setIconVisibility(showUser); //현재 위치를 표시하는 파랑색 아이콘을 표기
        tMapView.setLocationPoint(129.08102472195395, 35.23380020523731); //현재 위치 설정
        tMapView.setCenterPoint(129.08102472195395, 35.23380020523731); // 지도 중심좌표 설정

        searchCategoryView = findViewById(R.id.search_option_view);
        searchResultLayout = findViewById(R.id.search_result_layout);
        searchResultListView = findViewById(R.id.search_result_list);

        searchInputEditText.setOnKeyListener( (v,keyCode,event)-> searchByEditText(event, keyCode) );

        adapter = new SearchResultListAdapter();
        searchResultListView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println(searchResult.get(position).getPOIName());
            Intent intent = new Intent();
            intent.putExtra("result", new SearchResult(searchResult.get(position)));
            setResult(RESULT_OK, intent);
            finish();
        });

        searchResultListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                TMapPoint p = searchResult.get(visiblePosition).getPOIPoint();

                TMapMarkerItem marker = new TMapMarkerItem();
                marker.setTMapPoint(p);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot, options));
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

    private void categoryBtnInit() {
        int[] id = { R.id.search_category1_btn, R.id.search_category2_btn, R.id.search_category3_btn,
                     R.id.search_category4_btn, R.id.search_category5_btn, R.id.search_category6_btn,
                     R.id.search_category7_btn, R.id.search_category8_btn, R.id.search_category9_btn,
                     R.id.search_category10_btn, R.id.search_category11_btn, R.id.search_category12_btn,
                     R.id.search_category13_btn, R.id.search_category14_btn, R.id.search_category15_btn,
                     R.id.search_category16_btn};

        for (int i = 0; i < 16; i++) {
            categoryBtn[i] = findViewById(id[i]);
            categoryBtn[i].setOnClickListener((v) -> serachByCategory(v) );
        }


    }

    private void search(TMapPoint point, String keyWord) {
        Log.d("Search", "Start Search");
        tMapData.findAroundKeywordPOI(point, keyWord , 10, 200, arrayList -> {
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
                });
            }
        });
    }

    private void serachByCategory(View v) {
        Button b = findViewById(v.getId());
        String keyWord = b.getText().toString();
        TMapPoint point = new TMapPoint(35.23288, 129.08787);
        search(point, keyWord);
    }

    private boolean searchByEditText(KeyEvent e, int keyCode) {
        if ((e.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            String keyWord = searchInputEditText.getText().toString();
            TMapPoint point = new TMapPoint(35.23288, 129.08787);
            search(point, keyWord);
            return true;
        } else {
            return false;
        }

    }

    private void showCategory() {
        searchCategoryView.setVisibility(View.VISIBLE);
        searchResultLayout.setVisibility(View.GONE);
    }

}
