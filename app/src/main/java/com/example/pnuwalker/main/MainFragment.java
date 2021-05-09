
package com.example.pnuwalker.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
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
import androidx.appcompat.app.AppCompatActivity;

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
import java.text.ParseException;


public class MainFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback{

    //메인 액티비티 객체 선언
    MainActivity activity;

    //화면이 붙을때 작동하는 메서드
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //현재 소속된 액티비티를 메인 액티비티로 한다.
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
    String[] today_schedule_time = new String[]{"202104281200","202104281400","202104282200"};      //테스트용. 날짜시간 맞게 변경후 시연할것.
    String[] today_schedule_site = new String[]{"제도관","인문관","새벽벌"};
    double[] today_schedule_site_xpoint = new double[]{129.082112,129.08134602,129.081379771};
    double[] today_schedule_site_ypoint = new double[]{35.231154,35.232287960304575,35.23577906709686};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //티맵 표시.
        frag_mapLayout = view.findViewById(R.id.frag_map_layout);
        frag_tMapView = new TMapView(getActivity());
        frag_mapLayout.addView(frag_tMapView);



        initmap();


        //현재시각 표시.
        cur_time = view.findViewById(R.id.frag_cur_time);
        cur_time.setText(get_time);

        //이전-현재-다음 일정 표기.
        int i = 0, k;
        while(i < today_schedule_time.length){
            Date tmp_time = null;
            try {
                tmp_time = simpleDate.parse(today_schedule_time[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                curDate = simpleDate.parse(simpleDate.format(curDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(tmp_time.getTime() > curDate.getTime() ){
                makeMaker(i);
                k = i;
                TextView next_schedule = view.findViewById(R.id.will_do_time);
                next_schedule.setText(today_schedule[i]);
                TextView next_site = view.findViewById(R.id.will_do_site);
                next_site.setText(today_schedule_site[i]);
                if(i > 0) {
                    TextView now_schedule = view.findViewById(R.id.do_time);
                    now_schedule.setText(today_schedule[i-1]);
                    TextView now_site = view.findViewById(R.id.do_site);
                    now_site.setText(today_schedule_site[i-1]);
                    if(i > 1){
                        TextView bef_schedule = view.findViewById(R.id.did_time);
                        bef_schedule.setText(today_schedule[i-2]);
                        TextView bef_site = view.findViewById(R.id.did_site);
                        bef_site.setText(today_schedule_site[i-2]);
                    }
                }
                break;
            }

            i++;
        }


        //다음일과까지 남은 시간 표기.
        Date d1 = null;
        try {
            if(i < today_schedule_time.length)
                d1 = simpleDate.parse(today_schedule_time[i]);
            else
                d1 = simpleDate.parse(simpleDate.format(curDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            curDate = simpleDate.parse(simpleDate.format(curDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        long diff =  (d1.getTime() - curDate.getTime()) -32400000;
        String get_next_time = f.format(diff);
        next_time = view.findViewById(R.id.time_to_next);
        next_time.setText(get_next_time);


        ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
        int m = i;
        while(m < today_schedule_time.length && m > 0 ) {

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

        //fragment_xml를 MainFragment.java와 묶어주는 역할을 하는 메서드
        //inflate 시키면 Object 타입으로 넘어오기 때문에 캐스팅 필요
        //(사용할 자원, 자원을 담을 곳, T/F)
        //메인에 직접 들어가면 True, 프래그먼트에 있으면 False


        //fragment_main.xml에 접근하기위해서는 rootView. 으로 접근해야한다
        //버튼1 초기화
        Button button1 = view.findViewById(R.id.btn_whole);

        //버튼1 기능 추가
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(1);
            }
        });

        //ViewGroup은 View에 속하므로 View가 리턴 타입이어도 ViewGroup을 리턴할 수 있다.


        return view;
    }



    public void initmap() {

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

    //gps 연동
    @Override
    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        frag_tMapView.setCenterPoint(lon, lat);
        frag_tMapView.setLocationPoint(lon , lat);
        System.out.println("aaa");
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
