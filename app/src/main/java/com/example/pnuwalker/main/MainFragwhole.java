package com.example.pnuwalker.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainFragwhole extends Fragment {

    MainActivity activity;
    static TMapView frag_tMapView;
    static TMapData frag_tMapdata;
    private  static TMapGpsManager mygps;
    RelativeLayout frag_mapLayout;

    //현재시각 표시.
    TextView cur_time;
    Date curDate = new Date();
    SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long  tmpnow =  curDate.getTime() ;
    String get_time = nowDate.format(tmpnow);

    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmm");
    TextView next_time;

    // 나중에 시간표에 저장된 DB를 가져 올 것이므로, 함수 구상용도이고, 임의로 시간순 작성.
    String[] today_schedule = new String[]{"컴알","고토","도서관자습"};
    String[] today_schedule_time = new String[]{"202104281200","202104281400","202104242200"};      //테스트용. 날짜시간 맞게 변경후 시연할것.
    String[] today_schedule_site = new String[]{"제도관","인문관","새벽벌"};
    double[] today_schedule_site_xpoint = new double[]{129.082112,129.08134602,129.081379771};
    double[] today_schedule_site_ypoint = new double[]{35.231154,35.232287960304575,35.23577906709686};

    String today_whole_schedule = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_wholeschedule, container, false);

        activity = (MainActivity) getActivity();

        //티맵 표시.
        frag_mapLayout = rootView.findViewById(R.id.frag_map_layout);
        frag_tMapView = new TMapView(getActivity());
        frag_mapLayout.addView(frag_tMapView);


        //버튼2 객체 초기화
        Button button2 = rootView.findViewById(R.id.today_schedule);

        //버튼2 기능 추가
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(2);
            }
        });

        wholeinitmap();

        //이전-현재-다음 일정 표기.
        int i = 0, k;
        while(i < today_schedule_time.length){
            today_whole_schedule = today_whole_schedule + today_schedule[i] +"(" + today_schedule_site[i] + ")";

            makeMaker(i);

            i++;
            if(i < today_schedule_time.length)
                today_whole_schedule = today_whole_schedule + "- ";
        }

        TextView bef_schedule = rootView.findViewById(R.id.whole_schedule);
        bef_schedule.setText(today_whole_schedule);

        ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
        int m = 1;
        while(m < today_schedule_time.length  && m >0 ) {

            alTMapPoint.add(new TMapPoint(today_schedule_site_ypoint[m-1], today_schedule_site_xpoint[m-1]));
            alTMapPoint.add(new TMapPoint(today_schedule_site_ypoint[m], today_schedule_site_xpoint[m]));
            m++;
        }
        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for (int l = 0; l < alTMapPoint.size(); l++) {
            tMapPolyLine.addLinePoint(alTMapPoint.get(l));
            frag_tMapView.addTMapPolyLine("Line"+ l, tMapPolyLine);
        }


        return rootView;
    }

    public void wholeinitmap() {

        frag_tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        frag_tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        frag_tMapView.setZoomLevel(17); //지도 초기 확대수준 설정
        frag_tMapView.setSightVisible(true); //현재 보고있는 방향을 표시
        frag_tMapView.setIconVisibility(true); //현재 위치를 표시하는 파랑색 아이콘을 표기
        frag_tMapView.setLocationPoint(129.082112, 35.231154);
        frag_tMapView.setCenterPoint(129.082112, 35.231154);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }

        mygps = new TMapGpsManager(getActivity());
        mygps.setMinTime(1000);
        mygps.setMinDistance(1);
        mygps.setProvider(mygps.GPS_PROVIDER);
        mygps.setProvider(mygps.NETWORK_PROVIDER);
        mygps.setLocationCallback();

        //mygps.OpenGps();
        System.out.println("gps test");


    }

    private void makeMaker(int cnt){

        Activity activity = getActivity();

        while(cnt < today_schedule_time.length) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            TMapPoint tMapPoint1 = new TMapPoint(today_schedule_site_ypoint[cnt], today_schedule_site_xpoint[cnt]); // 마커 좌표.

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;
            // 마커 아이콘
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.map_pin_red, options);
            markerItem1.setIcon(icon); // 마커 아이콘 지정
            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
            markerItem1.setName(today_schedule[cnt]); // 마커의 타이틀 지정

            frag_tMapView.addMarkerItem("markerItem"+cnt, markerItem1); // 지도에 마커 추가
            cnt++;
        }

    }

}