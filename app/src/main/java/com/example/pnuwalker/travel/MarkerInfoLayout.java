package com.example.pnuwalker.travel;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;

import com.example.pnuwalker.Pair;
import com.example.pnuwalker.R;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class MarkerInfoLayout implements Animation.AnimationListener {
    public static final int DETAIL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int SCHEDULE = 4;

    private TMapPoint point;
    private TMapPOIItem poi;
    private PNUBuildingInfo pnuInfo;
    private RelativeLayout layout;
    private LinearLayout gpsButtonLayout;
    private TextView buildingNumber;
    private TextView name;
    private Animation showAnimation;
    private Animation hideAnimation;

    private Button btnDetail; // 자세히보기 지정 버튼
    private Button btnStart; // 길찾기 시작 지정 버튼
    private Button btnEnd; // 길찾기 도착 지정 버튼
    private Button btnSetSchedule; // 일정등록 버튼

    private boolean isVisible;
    private boolean inPNU = false;

    public MarkerInfoLayout(View view) {
        gpsButtonLayout = view.findViewById(R.id.travel_gps_button_layout);
        layout = view.findViewById(R.id.marker_info);
        buildingNumber = view.findViewById(R.id.marker_info_buildingnumber);
        name = view.findViewById(R.id.marker_info_name);
        btnDetail = (Button) view.findViewById(R.id.marker_info_btndetail);
        btnStart = (Button) view.findViewById(R.id.marker_info_btnstart);
        btnEnd = (Button) view.findViewById(R.id.marker_info_btnend);
        btnSetSchedule = (Button) view.findViewById(R.id.marker_info_btnschedule);
        showAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.show_marker_info_anim);
        hideAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.hide_marker_info_anim);

        isVisible = false;

        //레이아웃 클릭시 지도가 같이 클릭되는 것을 방지
        layout.setOnTouchListener((v, e) -> true);
    }

    //Building Number가 빈문자열일 때, TextView를 안보이게함(Gone)
    public void setBuildingNumber(final String number) {
        int dp_20 = (int)layout.getResources().getDimension(R.dimen.dp_10);
        int dp_10 = (int)layout.getResources().getDimension(R.dimen.dp_10);
        int dp_3 = (int)layout.getResources().getDimension(R.dimen.dp_3);

        if (number.equals("")) {
            buildingNumber.setVisibility(View.GONE);
            name.setTextSize(Dimension.SP, 28);
            name.setPadding(dp_10, dp_20, 0 , 0);
        } else {
            name.setTextSize(Dimension.SP, 20);
            buildingNumber.setVisibility(View.VISIBLE);
            buildingNumber.setText(number);
            name.setPadding(dp_10, dp_3, 0 , 0);
        }
    }
    
    //버튼 클릭시 Listen를 설정
    public void setBtnClickListener(int button, View.OnClickListener listener) {
        switch (button) {
            case DETAIL : {
                btnDetail.setOnClickListener(listener);
                break;
            }
            case START : {
                btnStart.setOnClickListener(listener);
                break;
            }
            case END : {
                btnEnd.setOnClickListener(listener);
                break;
            }
            case SCHEDULE : {
                btnSetSchedule.setOnClickListener(listener);
                break;
            }
        }
    }

    public void setPOI(TMapPOIItem poi) { this.poi = poi; }
    public void setName(final String nameStr) {
        if ( nameStr.length() > 20 ) {
            name.setText(nameStr.substring(0,19) + "...");
        } else {
            name.setText(nameStr);
        }
    }

    public void setIsPNU(boolean b) { inPNU = b; }
    public void setPNUInfo(PNUBuildingInfo info) { pnuInfo = info; }
    public void setVisibility(int value) { layout.setVisibility(value); }
    public void setTMapPoint(TMapPoint point) { this.point = point; }
    public void setBtnDetailActive(boolean bool) {
        btnDetail.setClickable(bool);
        if (bool)
            btnDetail.setTextColor(Color.WHITE);
        else
            btnDetail.setTextColor(Color.GRAY);
    }

    public boolean isPNU() { return inPNU; }
    public SimplePOI getSimplePOI() { return new SimplePOI(poi); }
    public PNUBuildingInfo getPNUInfo() { return pnuInfo; }
    public TMapPOIItem getPOI() { return poi; }
    public String getName() { return (String) name.getText(); }
    public double getLatitude() { return point.getLatitude(); }
    public double getLongitude() { return point.getLongitude(); }
    public int getVisibility() { return layout.getVisibility(); }
    public TMapPoint getTMapPoint() { return point; }

    public void show() {
        layout.setVisibility(View.VISIBLE);
        layout.startAnimation(showAnimation);
        gpsButtonLayout.startAnimation(showAnimation);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gpsButtonLayout.getLayoutParams());
        params.addRule(RelativeLayout.ABOVE, R.id.marker_info);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        gpsButtonLayout.setLayoutParams(params);
    }
    public void hide() {
        layout.startAnimation(hideAnimation);
        gpsButtonLayout.startAnimation(hideAnimation);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gpsButtonLayout.getLayoutParams());
        params.removeRule(RelativeLayout.ABOVE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        gpsButtonLayout.setLayoutParams(params);
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        if ( isVisible ) {
            layout.setVisibility(View.GONE);
            isVisible = false;
        } else {
            isVisible = true;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
