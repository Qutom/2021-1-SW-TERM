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
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    static TMapPolyLine[] today_schedule_polylint = new TMapPolyLine[30];

    private GpsTracker gpsTracker;
    public double gps_latitude = 0 ;
    public double gps_longitude = 0;

//    // 나중에 시간표에 저장된 DB를 가져 올 것이므로, 함수 구상용도이고, 임의로 시간순 작성.
//    static String[] today_schedule = new String[]{"컴알","고토","도서관자습",};
//    static String[] today_schedule_time = new String[]{"202104281200","202104281400","202104242200",};      //테스트용. 날짜시간 맞게 변경후 시연할것.
//    static String[] today_schedule_site = new String[]{"제도관","인문관","새벽벌",};
//    static double[] today_schedule_site_xpoint = new double[]{129.082112,129.08134602,129.081379771,};
//    static double[] today_schedule_site_ypoint = new double[]{35.231154,35.232287960304575,35.23577906709686,};

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



        //티맵 표시.
        frag_mapLayout = view.findViewById(R.id.frag_map_layout);
        frag_tMapView = new TMapView(getActivity());
        frag_mapLayout.addView(frag_tMapView);
        fragfindPNUPath = new FindPath(getContext());

        gpsTracker = new GpsTracker(getActivity());
        gps_latitude = gpsTracker.getLatitude();
        gps_longitude = gpsTracker.getLongitude();

        initmap();

        int week = doDayOfWeek();


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

        String strmon,strday,strdate = "", strtime, strmin ;


        if(hour < 10){
            strtime = "0" + String.valueOf(hour);
        }
        else strtime = String.valueOf(hour);
        if(min < 10){
            strmin = "0" + String.valueOf(min);
        }
        else strmin = String.valueOf(min);

        strdate = strdate + strtime + strmin;

        //이전-현재-다음 일정 표기.
        int i = 0;  // 일정이 2이상인 경우에 사용.

        //일정이 하나만 있는경우.
        if(schesize == 1){
            Date tmp_time = null;
            try {
                tmp_time = simpleDate.parse(today_schedule_time[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tmp = today_schedule_time[0].substring(8,12);
            makeMaker(0);
            if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                TextView next_schedule = view.findViewById(R.id.will_do_time);
                next_schedule.setText(today_schedule[0]);
                TextView next_site = view.findViewById(R.id.will_do_site);
                next_site.setText(today_schedule_site[0]);
                String bef15min = updater(today_schedule_time[i]);
            }
            else{
                tmp = today_schedule_end[0].substring(8,12);
                if(Integer.parseInt(tmp) > Integer.parseInt(strdate)) {
                    TextView bef_schedule = view.findViewById(R.id.do_time);
                    bef_schedule.setText(today_schedule[0]);
                    TextView now_site = view.findViewById(R.id.do_site);
                    now_site.setText(today_schedule_site[0]);
                }
                else {
                    TextView bef_schedule = view.findViewById(R.id.did_time);
                    bef_schedule.setText(today_schedule[0]);
                    TextView bef_site = view.findViewById(R.id.did_site);
                    bef_site.setText(today_schedule_site[0]);
                }
            }
        }
        else if(schesize == 2){
            while(i< schesize){

                String tmp = today_schedule_time[i].substring(8,12);
                makeMaker(i);
                if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                    TextView bef_schedule = view.findViewById(R.id.did_time);
                    bef_schedule.setText("");
                    TextView bef_site = view.findViewById(R.id.did_site);
                    bef_site.setText("");
                    TextView next_schedule = view.findViewById(R.id.will_do_time);
                    next_schedule.setText(today_schedule[i]);
                    TextView next_site = view.findViewById(R.id.will_do_site);
                    next_site.setText(today_schedule_site[i]);

                    if(i > 0) {
                        DaySchedule a = schedules.get(i);
                        polyLine = a.getPolyLine();
                        if (polyLine == null)
                            System.out.println("null");
                        else
                            frag_tMapView.addTMapPolyLine("path"+ i, polyLine);
                        tmp = today_schedule_end[0].substring(8,12);
                        if(Integer.parseInt(tmp) > Integer.parseInt(strdate)) {
                            TextView now_schedule = view.findViewById(R.id.do_time);
                            now_schedule.setText(today_schedule[0]);
                            TextView now_site = view.findViewById(R.id.do_site);
                            now_site.setText(today_schedule_site[0]);
                        }
                        else {
                            bef_schedule = view.findViewById(R.id.did_time);
                            bef_schedule.setText(today_schedule[i - 1]);
                            bef_site = view.findViewById(R.id.did_site);
                            bef_site.setText(today_schedule_site[i - 1]);
                        }
                    }
                    break;
                }
                else{
                    TextView bef_schedule = view.findViewById(R.id.did_time);
                    bef_schedule.setText(today_schedule[i]);
                    TextView bef_site = view.findViewById(R.id.did_site);
                    bef_site.setText(today_schedule_site[i]);
                }
                i++;
            }
        }
        else if (schesize >=3){
            while(i < schesize){

                String tmp = today_schedule_time[i].substring(8,12);

                if(Integer.parseInt(tmp) > Integer.parseInt(strdate)){
                    makeMaker(i);
                    TextView bef_schedule = view.findViewById(R.id.did_time);
                    bef_schedule.setText("");
                    TextView bef_site = view.findViewById(R.id.did_site);
                    bef_site.setText("");
                    TextView next_schedule = view.findViewById(R.id.will_do_time);
                    next_schedule.setText(today_schedule[i]);
                    TextView next_site = view.findViewById(R.id.will_do_site);
                    next_site.setText(today_schedule_site[i]);
                    if(i > 0) {
                        DaySchedule a = schedules.get(i);
                        polyLine = a.getPolyLine();
                        if (polyLine == null)
                            System.out.println("null");
                        else
                            frag_tMapView.addTMapPolyLine("path"+ i, polyLine);

                        tmp = today_schedule_end[0].substring(8,12);
                        if(Integer.parseInt(tmp) > Integer.parseInt(strdate)) {
                            TextView now_schedule = view.findViewById(R.id.do_time);
                            now_schedule.setText(today_schedule[0]);
                            TextView now_site = view.findViewById(R.id.do_site);
                            now_site.setText(today_schedule_site[0]);
                        }
                        else {
                            bef_schedule = view.findViewById(R.id.did_time);
                            bef_schedule.setText(today_schedule[i - 1]);
                            bef_site = view.findViewById(R.id.did_site);
                            bef_site.setText(today_schedule_site[i - 1]);
                        }

                        if(i > 1){
                            TextView beff_schedule = view.findViewById(R.id.did_time);
                            beff_schedule.setText(today_schedule[i-2]);
                            TextView beff_site = view.findViewById(R.id.did_site);
                            beff_site.setText(today_schedule_site[i-2]);
                        }
                    }
                    break;
                }
                else{
                    TextView bef_schedule = view.findViewById(R.id.did_time);
                    bef_schedule.setText(today_schedule[i]);
                    TextView bef_site = view.findViewById(R.id.did_site);
                    bef_site.setText(today_schedule_site[i]);
                }
                i++;
            }
        }

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

        Button button2 = view.findViewById(R.id.btn_whole);

        //버튼1 기능 추가
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(1);
            }
        });

        return view;
    }

    public String updater(String time){

        String to = time;

        String tmptimemin = to.substring(10,12);
        int inttmptimemin = Integer.parseInt(tmptimemin);

        String tmptimehour = to.substring(8,10);
        int inttmptimehour = Integer.parseInt(tmptimehour);

        String tmptimeday = to.substring(6,8);
        int inttmptimeday = Integer.parseInt(tmptimeday);

        String tmptimemonth = to.substring(4,6);
        int inttmptimemonth = Integer.parseInt(tmptimemonth);

        String tmptimeyear = to.substring(0,4);
        int inttmptimeyear = Integer.parseInt(tmptimeyear);

        if(inttmptimemin -15 < 0){
            if(inttmptimehour - 1 < 0){
                if(inttmptimeday - 1 <= 0){
                    if(inttmptimemonth - 1 <= 0){
                        int tmp = Integer.parseInt(tmptimemonth);
                        if(tmp == 3) {
                            //윤년
                            if(inttmptimeyear % 4 ==0) {
                                inttmptimemin = inttmptimemin + 45;
                                inttmptimehour = inttmptimehour + 23;
                                inttmptimeday = inttmptimeday + 28;
                                inttmptimemonth--;
                            }
                            inttmptimemin = inttmptimemin + 45;
                            inttmptimehour = inttmptimehour + 23;
                            inttmptimeday = inttmptimeday + 27;
                            inttmptimemonth--;
                        }
                        else if((tmp <=7 && tmp % 2 == 0) || (tmp >=8 && tmp %2 ==1)){
                            inttmptimemin = inttmptimemin + 45;
                            inttmptimehour = inttmptimehour + 23;
                            inttmptimeday = inttmptimeday + 30;
                            inttmptimemonth = inttmptimemonth + 11;
                            inttmptimeyear--;
                        }
                        else {
                            inttmptimemin = inttmptimemin + 45;
                            inttmptimehour = inttmptimehour + 23;
                            inttmptimeday = inttmptimeday + 29;
                            inttmptimemonth = inttmptimemonth + 11;
                            inttmptimeyear--;
                        }
                    }
                    else{
                        int tmp = Integer.parseInt(tmptimemonth);
                        if(tmp == 3) {
                            //윤년
                            if(inttmptimeyear % 4 ==0) {
                                inttmptimemin = inttmptimemin + 45;
                                inttmptimehour = inttmptimehour + 23;
                                inttmptimeday = inttmptimeday + 28;
                                inttmptimemonth--;
                            }
                            else {
                                inttmptimemin = inttmptimemin + 45;
                                inttmptimehour = inttmptimehour + 23;
                                inttmptimeday = inttmptimeday + 27;
                                inttmptimemonth--;
                            }
                        }
                        else if((tmp <=7 && tmp % 2 == 0) || (tmp >=8 && tmp %2 ==1)){
                            inttmptimemin = inttmptimemin + 45;
                            inttmptimehour = inttmptimehour + 23;
                            inttmptimeday = inttmptimeday + 30;
                            inttmptimemonth--;
                        }
                        else {
                            inttmptimemin = inttmptimemin + 45;
                            inttmptimehour = inttmptimehour + 23;
                            inttmptimeday = inttmptimeday + 29;
                            inttmptimemonth--;
                        }
                    }
                }
                else{
                    inttmptimemin = inttmptimemin + 45;
                    inttmptimehour = inttmptimehour + 23;
                    inttmptimeday--;
                }
            }
            else{
                inttmptimemin = inttmptimemin + 45;
                inttmptimehour--;
            }
        }
        else{
            inttmptimemin = inttmptimemin - 15;
        }

        tmptimeyear = String.valueOf(inttmptimeyear);
        if(inttmptimemonth < 10) {
            tmptimemonth = "0" + inttmptimemonth;
        } else {
            tmptimemonth = String.valueOf(inttmptimemonth);
        }
        if(inttmptimeday < 10) {
            tmptimeday = "0" + inttmptimeday;
        } else {
            tmptimeday = String.valueOf(inttmptimeday);
        }
        if(inttmptimehour < 10) {
            tmptimehour = "0" + inttmptimehour;
        } else {
            tmptimehour = String.valueOf(inttmptimehour);
        }
        if(inttmptimemin < 10) {
            tmptimemin = "0" + inttmptimemin;
        } else {
            tmptimemin = String.valueOf(inttmptimemin);
        }

        String tmptime = tmptimeyear + tmptimemonth;
        tmptime = tmptime + tmptimeday;
        tmptime = tmptime + tmptimehour;
        tmptime = tmptime + tmptimemin;

        return tmptime;

    }

    public void initmap() {

        frag_tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        frag_tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        frag_tMapView.setZoomLevel(17); //지도 초기 확대수준 설정
        frag_tMapView.setSightVisible(true); //현재 보고있는 방향을 표시
        frag_tMapView.setIconVisibility(true); //현재 위치를 표시하는 파랑색 아이콘을 표기
        frag_tMapView.setLocationPoint(129.082112, 35.231154);
        frag_tMapView.setCenterPoint(129.082112, 35.231154);
//        frag_tMapView.setLocationPoint(gps_longitude, gps_latitude);
//        frag_tMapView.setCenterPoint(gps_longitude, gps_latitude);


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


    private int doDayOfWeek(){
        Calendar cal = Calendar.getInstance();


        int nWeek = cal.get(Calendar.DAY_OF_WEEK);

        return nWeek;

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


    private void makeMaker(int cnt){

        Activity activity = getActivity();

        while(cnt < today_schedule_time.length) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            TMapPoint tMapPoint1 = new TMapPoint(today_schedule_site_ypoint[cnt], today_schedule_site_xpoint[cnt]); // 마커 좌표.

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;
            if(cnt == 0) {
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.map_pin_red, options);
                markerItem1.setIcon(icon); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName("mark" + cnt); // 마커의 타이틀 지정

                frag_tMapView.addMarkerItem("markerItem" + cnt, markerItem1); // 지도에 마커 추가
            }
            else if(cnt + 1 < today_schedule_time.length){
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.pnu_marker_orange, options);
                markerItem1.setIcon(icon); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName("mark" + cnt); // 마커의 타이틀 지정

                frag_tMapView.addMarkerItem("markerItem" + cnt, markerItem1); // 지도에 마커 추가
            }
            else{
                Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.pnu_marker_green, options);
                markerItem1.setIcon(icon); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName("mark" + cnt); // 마커의 타이틀 지정

                frag_tMapView.addMarkerItem("markerItem" + cnt, markerItem1); // 지도에 마커 추가
            }
            cnt++;
        }

    }


}
