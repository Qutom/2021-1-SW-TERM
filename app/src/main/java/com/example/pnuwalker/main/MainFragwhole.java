package com.example.pnuwalker.main;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.controlschedule.DaySchedule;
import com.example.pnuwalker.controlschedule.ScheduleReader;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainFragwhole extends Fragment {

    MainActivity activity;

    private  TMapGpsManager mygps;
    RelativeLayout frag_mapLayout;


    private TMapPolyLine polyLine;

    Button showDateDialogBtn;

    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmm");
    TextView next_time;
    Calendar cal = Calendar.getInstance();


    int year = cal.get ( cal.YEAR );
    int month = cal.get ( cal.MONTH )   ;
    int day = cal.get ( cal.DATE ) ;

    int hour = cal.get ( cal.HOUR_OF_DAY ) ;
    int min = cal.get ( cal.MINUTE );
    int temporalDayofWeek  = cal.get( cal.DAY_OF_WEEK) - 2;
    int week ;
    int schesize = 0;       //ScheduleReader.


    // 나중에 시간표에 저장된 DB를 가져 올 것이므로, 함수 구상용도이고, 임의로 시간순 작성.
    String[] today_schedule = new String[20];
    String[] today_schedule_time = new String[20];      //테스트용. 날짜시간 맞게 변경후 시연할것.
    String[] today_schedule_site = new String[20];
    double[] today_schedule_site_xpoint = new double[20];
    double[] today_schedule_site_ypoint = new double[20];
    String[] today_schedule_polyliney = new String[20];
    String[] today_schedule_polylinex = new String[20];

//    // 나중에 시간표에 저장된 DB를 가져 올 것이므로, 함수 구상용도이고, 임의로 시간순 작성.
//    String[] today_schedule = new String[]{"컴알","고토","도서관자습"};
//    String[] today_schedule_time = new String[]{"202104281200","202104281400","202104242200"};      //테스트용. 날짜시간 맞게 변경후 시연할것.
//    String[] today_schedule_site = new String[]{"제도관","인문관","새벽벌"};
//    double[] today_schedule_site_xpoint = new double[]{129.082112,129.08134602,129.081379771};
//    double[] today_schedule_site_ypoint = new double[]{35.231154,35.232287960304575,35.23577906709686};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_wholeschedule, container, false);
        SimpleDateFormat onlyDate = new SimpleDateFormat("yyyyMMdd");


        //현재시각 표시.
        TextView cur_time;
        Date curDate = new Date();
        SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long  tmpnow =  curDate.getTime() ;
        String get_time = nowDate.format(tmpnow);

        activity = (MainActivity) getActivity();
        String today_whole_schedule = "";


        //티맵 표시.
        TMapView frag_tMapView;
        frag_mapLayout = rootView.findViewById(R.id.frag_map_layout);
        frag_tMapView = new TMapView(getActivity());
        frag_mapLayout.addView(frag_tMapView);

        int index = 0 ;         //scheduletab.

        //버튼2 객체 초기화
        Button button2 = rootView.findViewById(R.id.today_schedule);

        //버튼2 기능 추가
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(2);
            }
        });

        showDateDialogBtn = rootView.findViewById(R.id.whole_show_date_dialog_btn);
        showDateDialogBtn.setOnClickListener((v -> {showDateDialog(); })
        );

        cur_time = rootView.findViewById(R.id.whole_frag_cur_time);

        String strmon,strday,strdate ;
        if(month < 10){
            strmon = "0" + String.valueOf(month+1);
        }
        else strmon = String.valueOf(month+1);
        if(day < 10){
            strday = "0" +String.valueOf(day);
        }
        else strday = String.valueOf(day);

        strdate = String.valueOf(year)+strmon+strday;


        wholeinitmap(frag_tMapView);

        try {
            week = doDayOfWeek(year, month, day, "yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String onlyday = onlyDate.format(tmpnow);
//        if(year != 0) {
//            try {
//                week = doDayOfWeek(year,month,day,"yyyy-MM-dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }




       ArrayList<DaySchedule> schedules = new ScheduleReader(activity.helper, year, month+1 , day , temporalDayofWeek, 0, 0).schedules;



        double yy = 0;
        schesize = schedules.size();
        String h = "",k = "",j = "", l = "",m = "" ;
        TMapPolyLine polyLine;
        String whole = "";
        for(int i = 0 ; i < schesize; i++){
            DaySchedule a = schedules.get(i);
            h = a.getName();
            k = String.valueOf(a.getDestLat());
            j = String.valueOf(a.getDestLon());
            l = a.getDestName();
            m = String.valueOf(a.getStartHour()) + String.valueOf(a.getStartMin());
            if(m.length() < 3) m = "0" + m;
            whole = whole + h + "( " + l +": "+ m + " )";
            if(i+1 < schesize)  whole = whole +"-> ";
            makeMaker(frag_tMapView,i,schesize, Double.parseDouble(k), Double.parseDouble(j));

            polyLine = a.getPolyLine();
            if (polyLine == null)
                System.out.println("null");
            else
                frag_tMapView.addTMapPolyLine("path"+ i, polyLine);

        }




        //이전-현재-다음 일정 표기.
        int i = 0;
        while(i < index){
            today_whole_schedule = today_whole_schedule + today_schedule[i] +"(" + today_schedule_site[i] + ")";

            //makeMaker(i);

            i++;
            if(i < index)
                today_whole_schedule = today_whole_schedule + "- ";
        }

        TextView bef_schedule = rootView.findViewById(R.id.whole_schedule);
        bef_schedule.setText(whole);

        cur_time.setText(strdate);



        return rootView;
    }

    public void wholeinitmap(TMapView frag_tMapView) {

        frag_tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        frag_tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        frag_tMapView.setZoomLevel(16); //지도 초기 확대수준 설정
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

    private String strTimetoMinute(String str) { //hh_mm
        String[] temp = str.split("_");
        return temp[0] + temp[1];
    }

    private String strlocsplitfront(String str) { //hh_mm
        String[] temp = str.split(",");
        return temp[0] ;
    }

    private String strlocsplitback(String str) { //hh_mm
        String[] temp = str.split(",");
        return temp[1] ;
    }

    private int doDayOfWeek(){
        Calendar cal = Calendar.getInstance();


        int nWeek = cal.get(Calendar.DAY_OF_WEEK);

        return nWeek;

    }

    private int doDayOfWeek(int year, int month, int day, String dateType) throws ParseException {

        String strmon,strday;

        if(month <10) strmon = "0" + String.valueOf(month);
        else strmon = String.valueOf(month);
        if(day < 10) strday = "0" + String.valueOf(day);
        else strday = String.valueOf(day);
        String date = String.valueOf(year)+ strmon+ strday;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);


        int nWeek = cal.get(Calendar.DAY_OF_WEEK);

        return nWeek;
    }



    private void makeMaker(TMapView frag_tMapView, int cnt, int size, double y, double x){

        Activity activity = getActivity();

        if(cnt < size) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            TMapPoint tMapPoint1 = new TMapPoint(y, x); // 마커 좌표.

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;
            // 마커 아이콘
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.map_pin_red, options);
            markerItem1.setIcon(icon); // 마커 아이콘 지정
            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
            markerItem1.setName("mark"+cnt); // 마커의 타이틀 지정

            frag_tMapView.addMarkerItem("markerItem"+cnt, markerItem1); // 지도에 마커 추가
        }

    }

    private void showDateDialog() {
        Calendar cal = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        int currYear = cal.get(cal.YEAR);
        int currMonth = cal.get(cal.MONTH);
        int currDay = cal.get(cal.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int _year, int _month, int _dayOfMonth) {
                year = _year;
                month = _month;
                day = _dayOfMonth;

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DATE, day);
                temporalDayofWeek = cal.get(Calendar.DAY_OF_WEEK) -2;



                setDateDialogBtnText(getDateString());
            }
        }, year ,month, day);

        minDate.set(currYear,currMonth,currDay);
        dialog.getDatePicker().setMinDate(minDate.getTime().getTime());

        dialog.setMessage("날짜 선택");
        dialog.show();
    }

    private TMapPolyLine strToPolyline(String lonStr, String latStr) {
        if (lonStr.equals("start") || lonStr.equals(""))
            return null;

        TMapPolyLine line = new TMapPolyLine();
        String[] lon = lonStr.split(",");
        String[] lat = latStr.split(",");
        for (int i = 0; i < lon.length; i++)
            line.addLinePoint(new TMapPoint(Double.parseDouble(lat[i]), Double.parseDouble(lon[i])));

        return line;
    }

    private String getDateString() {
        return String.format("%d  /  %d  / %d",year, month + 1, day); //ex) 2020  /  12  /  5
    }
    private void setDateDialogBtnText(String text) {
        showDateDialogBtn.setText(text);
    }
}