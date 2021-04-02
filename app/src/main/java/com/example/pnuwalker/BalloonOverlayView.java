package com.example.pnuwalker;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BalloonOverlayView extends FrameLayout {

    private LinearLayout layout;
    private TextView title;
    private TextView number;
    private TextView subTitle;

    //부산대학교 내부 건물을 위한 일반적인 BalloonView
    public BalloonOverlayView(Context context, int number, String name, String description, String id) {
        super(context);

        setPadding(10, 0, 10, 0);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);

        setupView(context, layout, number, name , description, id);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.NO_GRAVITY;
        addView(layout, params);
    }

    protected void setupView(Context context, final ViewGroup parent, int number, String name, String description, String id) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.bubble_pnu_normal, parent, true);

        number = (TextView) view.findViewById(R.id.bubble_number);
        title = (TextView) view.findViewById(R.id.bubble_title);
        subTitle = (TextView) view.findViewById(R.id.bubble_subtitle);


        setNumber(number);
        setTitle(name);
        setSubTitle(id);

    }

    //부산대학교 내부 건물을 위한 특별한(도서관, 학생식당...) BalloonView

    public void setNumber(int num) { number.setText(Integer.toString(num)); }
    public void setTitle(String str) {
        title.setText(str);
    }

    public void setSubTitle(String str) {
        subTitle.setText(str);
    }

    public void setTxtContents1(String strContents) {
        txtContents1.setText(strContents);
    }

    public void setTxtContents2(String strContents) {
        txtContents2.setText(strContents);
    }

    public void setTxtContents3(String strContents) {
        txtContents3.setText(strContents);
    }


}
