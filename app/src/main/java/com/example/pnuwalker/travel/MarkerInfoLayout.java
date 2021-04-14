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

    private RelativeLayout layout;
    private TextView buildingNumber;
    private TextView name;

    private Button btnDetail;
    private Button btnStart;
    private Button btnEnd;
    private Button btnSetSchedule;

    public MarkerInfoLayout(View activity) {
        layout = activity.findViewById(R.id.marker_info);
        buildingNumber = activity.findViewById(R.id.marker_info_buildingnumber);
        name = activity.findViewById(R.id.marker_info_name);

        //레이아웃 클릭시 지도가 같이 클릭되는 것을 방지
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return true;
            }
        });
    }


    public void setBuildingNumber(final String number) {
        if (number == "") {
            buildingNumber.setVisibility(View.GONE);
        } else {
            buildingNumber.setVisibility(View.VISIBLE);
            buildingNumber.setText(number);
        }
    }

    public void setName(final String nameStr) { name.setText(nameStr); }
    public void setVisibility(int value) { layout.setVisibility(value); }

    public int getVisibility() { return layout.getVisibility(); }

}
