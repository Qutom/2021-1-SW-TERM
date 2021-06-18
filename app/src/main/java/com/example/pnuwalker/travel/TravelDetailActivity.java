package com.example.pnuwalker.travel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapAddressInfo;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class TravelDetailActivity extends AppCompatActivity {
    TextView nameText;
    TextView address1Text;
    TextView address2Text;
    TextView bizNameText;
    TextView telNoText;
    TextView timeText;

    RecyclerView imageListView;
    LinearLayout timeLayout;
    LinearLayout telNoLayout;
    RelativeLayout mapLayout;

    TMapView tMapView;

    ImageButton backBtn;
    ImageButton dialBtn;
    Button nameExtendBtn;

    String name;
    SimplePOI poi;
    PNUBuildingInfo pnuInfo;
    TravalDetailImageListAdapter adapter;
    boolean isPNU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_building_detail);

        mapLayout = findViewById(R.id.detail_map_layout);
        imageListView = findViewById(R.id.detail_image_layout);
        telNoLayout = findViewById(R.id.detail_telno_layout);
        timeLayout = findViewById(R.id.detail_time_layout);

        tMapView = new TMapView(this);
        mapLayout.addView(tMapView);

        nameText = findViewById(R.id.detail_name);
        address1Text = findViewById(R.id.detail_address1);
        address2Text = findViewById(R.id.detail_address2);
        bizNameText = findViewById(R.id.detail_bizname);
        telNoText = findViewById(R.id.detail_telNo);
        timeText = findViewById(R.id.detail_time_info);

        backBtn = findViewById(R.id.detail_back_btn);
        backBtn.setOnClickListener((v) -> finish());
        dialBtn = findViewById(R.id.detail_dial_btn);
        dialBtn.setOnClickListener((v) -> dial());
        nameExtendBtn = findViewById(R.id.detail_name_extend_btn);
        nameExtendBtn.setOnClickListener((v) -> {showLongName();});

        adapter = new TravalDetailImageListAdapter();
        imageListView.setAdapter(adapter);
        imageListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        Intent intent = getIntent();

        isPNU = intent.getBooleanExtra("is_pnu", false);
        if (isPNU) {
            pnuInfo = (PNUBuildingInfo) intent.getSerializableExtra("pnu");
            setPNUData();
        } else {
            poi = (SimplePOI)intent.getSerializableExtra("poi");
            POINullCheck();
            setSimplePOIData();
        }

    }

    private void setPNUData() {
        telNoLayout.setVisibility(View.GONE);
        timeLayout.setVisibility(View.VISIBLE);
        imageListView.setVisibility(View.VISIBLE);

        nameText.setText(pnuInfo.name);
        bizNameText.setText("시설/구역내시설물/학교내시설물");

        TMapData tMapData = new TMapData();

        try {
            tMapData.reverseGeocoding(pnuInfo.lat, pnuInfo.lon, "A03", (info) -> {
                String addDo = info.strCity_do == null ? "" : info.strCity_do;
                String dong = info.strLegalDong == null ? "" : info.strLegalDong;
                String gu_gun = info.strGu_gun == null ? "" : info.strGu_gun;
                String roadName = info.strRoadName == null ? "" : info.strRoadName;

                runOnUiThread(() -> address1Text.setText(addDo + " " + gu_gun + " " + dong));
                runOnUiThread(() -> address2Text.setText(addDo + " " + gu_gun + " " + roadName));
            });
        } catch (Exception e) {
            e.printStackTrace();
            address1Text.setText("Error");
            address2Text.setText("Error");
        }

        //시간 설정
        if ( pnuInfo.timeInfo.equals("null") ) {
            timeText.setText("시간 정보가 없습니다.");
        } else {
            timeText.setText(pnuInfo.timeInfo);
        }
        
        //이미지 설정
        if ( pnuInfo.image.size() > 0 ) {
            System.out.println(pnuInfo.image);
            adapter.clear();
            for (int i = 0; i < pnuInfo.image.size(); i++)
                adapter.addItem(new TravelDetailImageListItem(pnuInfo.image.get(i)));
            runOnUiThread(() -> adapter.notifyDataSetChanged() );
        }

        //지도 설정
        tMapView.setCenterPoint(pnuInfo.lon, pnuInfo.lat);
        tMapView.setZoomLevel(16);

        TMapPoint point = new TMapPoint(pnuInfo.lat, pnuInfo.lon);
        TMapMarkerItem marker = new TMapMarkerItem();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        marker.setTMapPoint(point);
        marker.setIcon(makeTextedIcon(Integer.toString(pnuInfo.buildingNum),25, Color.BLACK, options));
        marker.setPosition(0, (float) 0.8);
        tMapView.addMarkerItem("marker", marker);
    }

    private void setSimplePOIData() {
        name = poi.name;
        if ( poi.name.length() > 13 ) {
            nameText.setText(poi.name.substring(0,6) + "...");
            nameExtendBtn.setVisibility(View.VISIBLE);
        } else {
            nameText.setText(poi.name);
            nameExtendBtn.setVisibility(View.GONE);
        }

        address1Text.setText(poi.upperAddrName + " " + poi.middleAddrName + " " + poi.lowerAddrName);
        address2Text.setText(poi.upperAddrName + " " + poi.middleAddrName + " " + poi.roadName + " " + poi.firstNo + "-" + poi.secondNo);
        bizNameText.setText(poi.upperBizName + "/" + poi.middleBizName + "/" + poi.lowerBizName);

        telNoLayout.setVisibility(View.VISIBLE);
        timeLayout.setVisibility(View.GONE);
        imageListView.setVisibility(View.GONE);

        if (poi.telNo != null && poi.telNo.length() != 0) {
            telNoText.setText(poi.telNo);
            dialBtn.setVisibility(View.VISIBLE);
        } else {
            telNoText.setText("-");
            dialBtn.setVisibility(View.GONE);
        }

        //지도 설정
        tMapView.setCenterPoint(poi.lon, poi.lat);
        tMapView.setZoomLevel(16);

        TMapPoint point = new TMapPoint(poi.lat, poi.lon);
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setTMapPoint(point);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot, options));
        marker.setPosition(0, (float) 0.8);
        tMapView.addMarkerItem("marker", marker);

    }

    private void dial() {
        String phoneNumber = telNoText.getText().toString().replaceAll("-","");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void showLongName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(name);
        builder.show();
    }

    private void POINullCheck() {
        if ( poi.upperBizName == null ) poi.upperBizName = "";
        if ( poi.middleBizName == null ) poi.middleBizName = "";
        if ( poi.lowerBizName == null ) poi.lowerBizName = "";
        if ( poi.upperAddrName == null ) poi.upperAddrName = "";
        if ( poi.middleAddrName == null ) poi.middleAddrName = "";
        if ( poi.lowerAddrName == null ) poi.lowerAddrName = "";
        if ( poi.secondNo == null ) poi.secondNo = "";
        if ( poi.firstNo == null ) poi.firstNo = "";
        if ( poi.roadName == null ) poi.roadName = "";
        if ( poi.buildingNo1 == null ) poi.buildingNo1 = "";
        if ( poi.buildingNo2 == null ) poi.buildingNo2 = "";
    }

    public Bitmap makeTextedIcon(String text, int TextSize, int textColor, BitmapFactory.Options options) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pnu_marker_search, options).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(textColor);
        paint.setTextSize(TextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, bm.getWidth() / 2, bm.getHeight() / 2 + 2, paint);

        return bm;
    }

    private void startLargeImageActivity(int imageId) {
        Intent intent = new Intent(this, TravelDetailLargeImageActivity.class);
    }
}
