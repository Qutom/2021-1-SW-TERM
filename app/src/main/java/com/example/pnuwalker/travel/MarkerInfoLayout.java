package com.example.pnuwalker.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.pnuwalker.R;

public class MarkerInfoLayout {

    public static final int DETAIL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int SCHEDULE = 4;

    private RelativeLayout layout;
    private TextView buildingNumber;
    private TextView name;

    private Button btnDetail; // 자세히보기 지정 버튼
    private Button btnStart; // 길찾기 시작 지정 버튼
    private Button btnEnd; // 길찾기 도착 지정 버튼
    private Button btnSetSchedule; // 일정등록 버튼

    public MarkerInfoLayout(View view) {
        layout = view.findViewById(R.id.marker_info);
        buildingNumber = view.findViewById(R.id.marker_info_buildingnumber);
        name = view.findViewById(R.id.marker_info_name);
        btnDetail = (Button) view.findViewById(R.id.marker_info_btndetail);
        btnStart = (Button) view.findViewById(R.id.marker_info_btnstart);
        btnEnd = (Button) view.findViewById(R.id.marker_info_btnend);
        btnSetSchedule = (Button) view.findViewById(R.id.marker_info_btnschedule);

        //레이아웃 클릭시 지도가 같이 클릭되는 것을 방지
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return true;
            }
        });
    }

    //Building Number가 빈문자열일 때, TextView를 안보이게함(Gone)
    public void setBuildingNumber(final String number) {
        if (number.equals("")) {
            buildingNumber.setVisibility(View.GONE);
        } else {
            buildingNumber.setVisibility(View.VISIBLE);
            buildingNumber.setText(number);
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

    public void setName(final String nameStr) { name.setText(nameStr); }
    public void setVisibility(int value) { layout.setVisibility(value); }

    public int getVisibility() { return layout.getVisibility(); }

}
