package com.example.pnuwalker.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pnuwalker.GpsTracker;
import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.controlschedule.DaySchedule;
import com.example.pnuwalker.controlschedule.ScheduleReader;
import com.example.pnuwalker.pathfind.FindPath;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;


public class MainFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    //메인 액티비티 객체 선언
    MainActivity activity;
    MainFragwhole mainFragwhole;

    static TMapView frag_tMapView;
    private static TMapGpsManager mygps;
    RelativeLayout frag_mapLayout;
    FindPath fragfindPNUPath;

    SimpleDateFormat onlyDate = new SimpleDateFormat("yyyyMMdd");

    SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmm");
    TextView next_time;

    // 나중에 시간표에 저장된 DB를 가져 올 것이므로, 함수 구상용도이고, 임의로 시간순 작성.
    static String[] today_schedule = new String[30];
    static String[] today_schedule_time = new String[30];      //테스트용. 날짜시간 맞게 변경후 시연할것.
    static String[] today_schedule_end = new String[30];
    static String[] today_schedule_site = new String[30];
    static double[] today_schedule_site_xpoint = new double[30];
    static double[] today_schedule_site_ypoint = new double[30];

    private GpsTracker gpsTracker;
    public double gps_latitude = 0 ;
    public double gps_longitude = 0;

    //이전-현재-다음 일정 표기용
    static int viewschedule ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if(mainFragwhole.tmpfragchange == 1){
            mainFragwhole.tmpfragchange = 0;
            activity.onFragmentChange(1);
        }


        //현재시각 표시.
        TextView cur_time;
        Date curDate = new Date();
        long tmpnow = curDate.getTime();
        String get_time = nowDate.format(tmpnow);

        TextView texttime = view.findViewById(R.id.did_time);
        TextView textschedule = view.findViewById(R.id.did_schedule);
        TextView textsite = view.findViewById(R.id.did_site);

        //텍스트표기용.
        String[] text_today_schedule = new String[3];
        String[] text_today_schedule_time = new String[3];
        String[] text_today_schedule_end = new String[3];
        String[] text_today_schedule_site = new String[3];

        //이전-현재-다음 일정 표기용
        viewschedule = 1;

        //티맵 표시.
        frag_mapLayout = view.findViewById(R.id.frag_map_layout);
        frag_tMapView = new TMapView(getActivity());
        frag_mapLayout.addView(frag_tMapView);
        fragfindPNUPath = new FindPath(getContext());

        gpsTracker = new GpsTracker(getActivity());
        gps_latitude = gpsTracker.getLatitude();
        gps_longitude = gpsTracker.getLongitude();

        initmap();

        String onlyday = onlyDate.format(tmpnow);
        int index = 0 ;

        Calendar cal = Calendar.getInstance();
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH )   ;
        int day = cal.get ( cal.DATE ) ;

        int hour = cal.get ( cal.HOUR_OF_DAY ) ;
        int min = cal.get ( cal.MINUTE );
        int temporalDayofWeek  = cal.get( cal.DAY_OF_WEEK) - 2;


        int schesize = 0;       //ScheduleReader.

        ArrayList<DaySchedule> schedules = new ScheduleReader(activity.helper, year, month+1 , day , temporalDayofWeek, 0, 0).schedules;

        double yy = 0;
        schesize = schedules.size();
        String h = "",k = "",j = "", l = "",m = "" ;
        TMapPolyLine polyLine;
        String whole = "";
        for(int i = 0 ; i < schesize; i++){
            DaySchedule a = schedules.get(i);
            today_schedule[i] = a.getName();
            today_schedule_site_ypoint[i] = a.getDestLat();
            today_schedule_site_xpoint[i] = a.getDestLon();
            today_schedule_site[i] = a.getDestName();


            if(String.valueOf(a.getStartMin()).length() < 2) m = "0" + String.valueOf(a.getStartMin());
            else m = String.valueOf(a.getStartMin());
            h = String.valueOf(a.getStartHour()) + m;
            if(h.length() < 4) h = "0" + h;
            j = String.valueOf(day);
            if(j.length() < 2)    j = "0" + j;
            l = String.valueOf(month);
            if(l.length() < 2)    l = "0" + l;
            today_schedule_time[i] = String.valueOf(year) + l + j + h;

            if(String.valueOf(a.getEndMin()).length() < 2) m = "0" + String.valueOf(a.getEndMin());
            else m = String.valueOf(a.getEndMin());
            h = String.valueOf(a.getEndHour()) + m;
            if(h.length() < 4) h = "0" + h;
            j = String.valueOf(day);
            if(j.length() < 2)    j = "0" + j;
            l = String.valueOf(month);
            if(l.length() < 2)    l = "0" + l;
            today_schedule_end[i] = String.valueOf(year) + l + j + h;
        }

        //현재시각 표시.

        cur_time = view.findViewById(R.id.frag_cur_time);
        cur_time.setText(get_time);

        String strdate = "", strtime, strmin ;


        if(hour < 10){
            strtime = "0" + String.valueOf(hour);
        }
        else strtime = String.valueOf(hour);
        if(min < 10){
            strmin = "0" + String.valueOf(min);
        }
        else strmin = String.valueOf(min);

        strdate = strdate + strtime + strmin;

        for(int tmpi = 0; tmpi <= 2; tmpi++){
            text_today_schedule_time[tmpi] = "";
            text_today_schedule_end[tmpi] = "";
        }

        //마커는 현재와 다음일정만! (한 개인 경우는 다음 일정만 있을때!)
        //이전-현재-다음 일정 표기.
        int i = 0;  // 일정이 2이상인 경우에 사용.
        int tmpi = 0;

        //일정이 하나만 있는경우.
        if(schesize == 1){
            Arrays.fill(text_today_schedule,"");
            Arrays.fill(text_today_schedule_site,"");
            Date tmp_time = null;
            try {
                tmp_time = simpleDate.parse(today_schedule_time[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tmp = today_schedule_time[0].substring(8,12);
            String tmpend = today_schedule_end[0].substring(8,12);
            if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                frag_tMapView.removeAllMarkerItem();
                text_today_schedule[2] = today_schedule[0];
                text_today_schedule_site[2] = today_schedule_site[0];
                text_today_schedule_time[2] = tmp;
                text_today_schedule_end[2] = tmpend;
                makeMaker(tmpi, 1);
                //String bef15min = updater(today_schedule_time[i]);
            }
            else{
                tmp = today_schedule_time[0].substring(8,12);
                tmpend = today_schedule_end[0].substring(8,12);
                if(Integer.parseInt(tmpend) > Integer.parseInt(strdate)) {
                    text_today_schedule[1] = today_schedule[0];
                    text_today_schedule_site[1] = today_schedule_site[0];
                    text_today_schedule_time[1] = tmp;
                    text_today_schedule_end[1] = tmpend;
                    //makeMaker(tmpi, 1);
                }
                else {
                    text_today_schedule[0] = today_schedule[0];
                    text_today_schedule_site[0] = today_schedule_site[0];
                    text_today_schedule_time[0] = tmp;
                    text_today_schedule_end[0] = tmpend;
                    //makeMaker(tmpi, 0);
                }
            }
            tmpi++;
        }
        else if(schesize == 2){
            Arrays.fill(text_today_schedule,"");
            Arrays.fill(text_today_schedule_site,"");
            while(i< schesize){

                String tmp = today_schedule_time[i].substring(8,12);
                String tmpend = today_schedule_end[i].substring(8,12);
                if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                    frag_tMapView.removeAllMarkerItem();
                    text_today_schedule[2] = today_schedule[i];
                    text_today_schedule_site[2] = today_schedule_site[i];
                    text_today_schedule_time[2] = tmp;
                    text_today_schedule_end[2] = tmpend;
                    makeMaker(tmpi, 1);
                    if(i > 0) {
                        DaySchedule a = schedules.get(i);
                        polyLine = a.getPolyLine();

                        tmp = today_schedule_time[0].substring(8,12);
                        tmpend = today_schedule_end[0].substring(8,12);
                        //현재 일정이 존재.
                        if(Integer.parseInt(tmpend) > Integer.parseInt(strdate)) {
                            if (polyLine == null)
                                System.out.println("null");
                            else
                                frag_tMapView.addTMapPolyLine("path", polyLine);
                            text_today_schedule[1] = today_schedule[0];
                            text_today_schedule_site[1] = today_schedule_site[0];
                            text_today_schedule_time[1] = tmp;
                            text_today_schedule_end[1] = tmpend;
                            makeMaker(tmpi-1, 0);
                        }
                        //현재일정 존재x, 이전-다음 구조.
                        else {
                            text_today_schedule[0] = today_schedule[i-1];
                            text_today_schedule_site[0] = today_schedule_site[i-1];
                            text_today_schedule_time[0] = today_schedule_time[i-1].substring(8,12);
                            text_today_schedule_end[0] = today_schedule_end[i-1].substring(8,12);
                            //makeMaker(tmpi, 0);
                        }
                    }
                    tmpi++;
                    break;
                }
                else{
                    tmp = today_schedule_time[i].substring(8,12);
                    tmpend = today_schedule_end[i].substring(8,12);
                    //현재인경우,
                    if(Integer.parseInt(tmpend) > Integer.parseInt(strdate)) {
                        text_today_schedule[1] = today_schedule[i];
                        text_today_schedule_site[1] = today_schedule_site[i];
                        text_today_schedule_time[1] = tmp;
                        text_today_schedule_end[1] = tmpend;
                        //makeMaker(tmpi, 0);

                    }
                    //이전 인경우,
                    else {
                        text_today_schedule[0] = today_schedule[i];
                        text_today_schedule_site[0] = today_schedule_site[i];
                        text_today_schedule_time[0] = today_schedule_time[i].substring(8,12);
                        text_today_schedule_end[0] = today_schedule_end[i].substring(8,12);
                        //makeMaker(tmpi, 0);

                    }
                }
                tmpi++;
                i++;
            }
        }
        else if (schesize >=3){
            Arrays.fill(text_today_schedule,"");
            Arrays.fill(text_today_schedule_site,"");
            while(i < schesize){

                String tmp = today_schedule_time[i].substring(8,12);
                String tmpend = today_schedule_end[i].substring(8,12);
                if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                    //makeMaker(i);
                    text_today_schedule[2] = today_schedule[i];
                    text_today_schedule_site[2] = today_schedule_site[i];
                    text_today_schedule_time[2] = tmp;
                    text_today_schedule_end[2] = tmpend;
                    makeMaker(i, 1);
                    if(i > 0) {
                        tmp = today_schedule_time[i-1].substring(8,12);
                        tmpend = today_schedule_end[i-1].substring(8,12);
                        //현재-다음
                        if(Integer.parseInt(tmpend) > Integer.parseInt(strdate)) {
                            DaySchedule a = schedules.get(i);
                            polyLine = a.getPolyLine();
                            if (polyLine == null)
                                System.out.println("null");
                            else
                                frag_tMapView.addTMapPolyLine("path"+ i, polyLine);
                            text_today_schedule[1] = today_schedule[i-1];
                            text_today_schedule_site[1] = today_schedule_site[i-1];
                            text_today_schedule_time[1] = tmp;
                            text_today_schedule_end[1] = tmpend;
                            makeMaker(i-1, 0);

                            //이전-현재-다음
                            if(i > 1){
                                text_today_schedule[0] = today_schedule[i-2];
                                text_today_schedule_site[0] = today_schedule_site[i-2];
                                text_today_schedule_time[0] = today_schedule_time[i-2].substring(8,12);
                                text_today_schedule_end[0] = today_schedule_end[i-2].substring(8,12);
                            }
                        }
                        //이전-다음
                        else {
                            text_today_schedule[0] = today_schedule[i-1];
                            text_today_schedule_site[0] = today_schedule_site[i-1];
                            text_today_schedule_time[0] = tmp;
                            text_today_schedule_end[0] = tmpend;
                            break;
                        }

                    }
                    tmpi++;
                    break;
                }
                else{
                    tmp = today_schedule_time[i].substring(8,12);
                    tmpend = today_schedule_end[i].substring(8,12);
                    if(Integer.parseInt(tmpend) > Integer.parseInt(strdate)) {
                        text_today_schedule[1] = today_schedule[i];
                        text_today_schedule_site[1] = today_schedule_site[i];
                        text_today_schedule_time[1] = today_schedule_time[i].substring(8,12);
                        text_today_schedule_end[1] = today_schedule_end[i].substring(8,12);
                        //makeMaker(tmpi, 0);
                        tmpi++;
                    }
                    else {
                        text_today_schedule[0] = today_schedule[i];
                        text_today_schedule_site[0] = today_schedule_site[i];
                        text_today_schedule_time[0] = today_schedule_time[i].substring(8,12);
                        text_today_schedule_end[0] = today_schedule_end[i].substring(8,12);
                        //makeMaker(tmpi, 0);
                        tmpi++;
                    }
                }
                i++;
            }
        }

        String tmpstrsche = "현재 일정(" + text_today_schedule_time[1] +" ~ " + text_today_schedule_end[1] +")" ;
        texttime.setText(tmpstrsche);
        textschedule.setText(text_today_schedule[1]);
        textsite.setText(text_today_schedule_site[1]);

        //남은시간 표기
        int jj = 0;
        while(jj < schesize)
        {

            String tmp = today_schedule_time[jj].substring(8, 12);
            int schedule_int = Integer.parseInt(tmp);
            int strdate_int = Integer.parseInt(strdate);
            String tmpint;
            if(schedule_int > strdate_int){

                int diff = schedule_int - strdate_int ;

                if(Integer.parseInt(strmin) > Integer.parseInt( tmp.substring(2,4))) {
                    diff = diff - 40;
                }
                tmpint = String.valueOf(diff);

                if(diff <10) tmpint = "0" + tmpint;
                if(diff <100) tmpint = "0" + tmpint;
                if(diff <1000)  tmpint = "0" + tmpint;

                tmpint = tmpint.substring(0,2) + ":" + tmpint.substring(2,4);

                next_time = view.findViewById(R.id.time_to_next);
                next_time.setText(tmpint);
                break;
            }

            jj++;
        }
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

        Button buttonnext = view.findViewById(R.id.next_day);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewschedule < 2) {
                    if(viewschedule == 1) {
                        String settmp = "다음 일정(" + text_today_schedule_time[2] + " ~ " + text_today_schedule_end[2] +")";
                        texttime.setText(settmp);
                        textschedule.setText(text_today_schedule[2]);
                        textsite.setText(text_today_schedule_site[2]);
                        viewschedule = 2;
                    }
                    if(viewschedule == 0) {
                        String settmp = "현재 진행 일정(" + text_today_schedule_time[1] + " ~ " + text_today_schedule_end[1] +")";
                        texttime.setText(settmp);
                        textschedule.setText(text_today_schedule[1]);
                        textsite.setText(text_today_schedule_site[1]);
                        viewschedule = 1;
                    }

                }

            }
        });

        Button buttonbef = view.findViewById(R.id.bef_day);
        buttonbef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewschedule > 0) {
                    if(viewschedule == 1) {
                        String settmp = "이전 일정(" + text_today_schedule_time[0] + " ~ " + text_today_schedule_end[0] +")";
                        texttime.setText(settmp);
                        textschedule.setText(text_today_schedule[0]);
                        textsite.setText(text_today_schedule_site[0]);
                        viewschedule = 0;
                    }
                    if(viewschedule == 2) {
                        String settmp = "현재 진행 일정(" + text_today_schedule_time[1] + " ~ " + text_today_schedule_end[1] +")";
                        texttime.setText(settmp);
                        textschedule.setText(text_today_schedule[1]);
                        textsite.setText(text_today_schedule_site[1]);
                        viewschedule = 1;
                    }

                }

            }
        });

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

        //원래는 gps.
        frag_tMapView.setLocationPoint(gps_longitude, gps_latitude);
        frag_tMapView.setCenterPoint(gps_longitude, gps_latitude);


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

        mygps.OpenGps();
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

    private void makeMaker(int cnt,int scend){

        Activity activity = getActivity();


        if(cnt < today_schedule_time.length) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();

            TMapPoint tMapPoint1 = new TMapPoint(today_schedule_site_ypoint[cnt], today_schedule_site_xpoint[cnt]); // 마커 좌표.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            if (scend == 0){
                frag_tMapView.removeMarkerItem("markerItem"+ cnt); // 기존 마커 삭제
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.path_start, options);
                markerItem1.setIcon(icon); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName("mark" + cnt); // 마커의 타이틀 지정

                frag_tMapView.addMarkerItem("markerItem" + cnt, markerItem1); // 지도에 마커 추가
            }
            else{
                frag_tMapView.removeMarkerItem("markerItem"+ cnt); // 기존 마커 삭제
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.path_end, options);
                markerItem1.setIcon(icon); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName("mark" + cnt); // 마커의 타이틀 지정
                frag_tMapView.addMarkerItem("markerItem" + cnt, markerItem1); // 지도에 마커 추가
            }
        }
    }

}
