package com.example.pnuwalker.controlschedule;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.pnuwalker.DataBaseHelper;
import com.example.pnuwalker.FindPNUPath;
import com.skt.Tmap.TMapPoint;

import java.net.Inet4Address;
import java.util.ArrayList;

public class ControlSchedule {
    FindPNUPath pnuPath;
    Context context;

    SQLiteDatabase db;
    DataBaseHelper dataBaseHelper;

    String name;
    String desc;

    boolean[] periodDayofWeek = new boolean[7]; // 주기 일정에서 해당 요일이 사용되는가?
    int year; //임시일정 년도
    int month; //임시일정 월
    int day; //임시일정 일
    int temporalDayofWeek; //임시 일정 요일 (0 ~ 6이고 0을 월요일로 표현)

    int startHour; //일정 시작 시
    int startMin; //일정 시작 분
    int endHour; //일정 종료 시
    int endMin; //일정 종료 분

    boolean isTemporalSchedule; //임시 일정인가?

    String destName;
    double destLat;
    double destLon;

    //정규 일정인 경우 values 값에 startHour, startMin, endHour, endMin, destName, destLat, destLon 만 필요
    public ControlSchedule(DataBaseHelper helper, Activity context, PeriodScheduleData data) {
        pnuPath = new FindPNUPath();
        dataBaseHelper = helper;
        db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(db);

        this.context = context;

        setCommonData(data);
        for ( int i = 0; i < 7; i++ )
            this.periodDayofWeek[i] = data.periodDayofWeek[i];


    }

    //임시 일정인 경우
    public ControlSchedule(DataBaseHelper helper, Activity context, TemporalScheduleData data) {
        pnuPath = new FindPNUPath();
        dataBaseHelper = helper;
        db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(db);

        this.context = context;

        setCommonData(data);
        year = data.year;
        month = data.month;
        day = data.day;
        temporalDayofWeek = data.dayofWeek;

    }

    //두 타입이 공통적으로 가지는 데이터 설정
    private void setCommonData(ScheduleData data) {
        isTemporalSchedule = data.isTemporalSchedule;

        name = data.getScheduleName();
        desc = data.getScheduleDesc();

        startHour = data.startHour;
        startMin = data.startMin;
        endHour = data.endHour;
        endMin = data.endMin;

        destName = data.destName;
        destLat = data.destLat;
        destLon = data.destLon;
    }

    //추가하고자 하는 스케줄이 시간이 겹치는 경우가 생기는가?
    public boolean checkSchedule() {
        int targetStartTime = startHour * 60 + startMin;
        int targetEndTime = endHour * 60 + endMin;
        boolean pass = true;

        if (isTemporalSchedule) { // 임시일정인가?
            /*
                임시일정일 때,
                1. 같은 Date를 가지는 값에서 time이 겹치는 경우
                2. 정규 일정(cyclic = 1)을 비교하여 time 이 겹치는 경우 등록 불가능
             */

            //같은 date를 가지는(yyyy_mm_dd)를 비교
            String dateStr = String.format("%d_%02d_%02d",year, month + 1, day);
            Cursor c = db.rawQuery("select start_time,end_time from schedule1 where date = '" + dateStr + "'" , null );
            if(c.moveToFirst()) {
                int startTime;
                int endTime;
                while( !c.isAfterLast() ){
                    startTime = strTimetoMinute(c.getString(0)); //start_time
                    endTime = strTimetoMinute(c.getString(1)); //end_time
                    System.out.println(String.format("%s ~ %s\n",c.getString(0), c.getString(1)));

                    if ( checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime) ) { //시간이 겹침 -> 탈락
                        Toast.makeText(context, "시간이 겹치는 임시 일정이 있습니다" , Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    c.moveToNext();
                }
            }

            //Cyclic 일정을 비교
            c = db.rawQuery("select start_time,end_time from schedule1 where cyclic = 1 and day_week = " +
                    Integer.toString(temporalDayofWeek) , null); //같은 요일에 주기 일정들을 얻음
            if(c.moveToFirst()){
                int startTime;
                int endTime;
                while( !c.isAfterLast() ){
                    startTime = strTimetoMinute(c.getString(0)); //start_time
                    endTime = strTimetoMinute(c.getString(1)); //end_time
                    System.out.println(String.format("%d ~ %d\n",startTime, endTime));

                    if ( checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime) ) { //시간이 겹침 -> 탈락
                        Toast.makeText(context, "시간이 겹치는 정규 일정이 있습니다" , Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    c.moveToNext();
                }
            }
            c.close();
        } else {
            //정규일정일 때, 정규 일정끼리만 비교하여 time이 겹치는 경우 탈락
            Cursor c = db.rawQuery("select day_week,start_time,end_time from schedule1 where cyclic = 1", null);
            if(c.moveToFirst()){
                int startTime;
                int endTime;
                int dayOfWeek;
                while( !c.isAfterLast() ) {
                    dayOfWeek = c.getInt(0);

                    if ( periodDayofWeek[dayOfWeek] ) { //해당 요일에 정규일정이 실행이 되는 경우
                        startTime = strTimetoMinute(c.getString(1)); //start_time
                        endTime = strTimetoMinute(c.getString(2)); //end_time
                        if ( checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime) ) { //시간이 겹침 -> 탈락
                            Toast.makeText(context, "시간이 겹치는 정규 일정이 있습니다" , Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    c.moveToNext();
                }
            }
        }

        return true;
    }

    private int strTimetoMinute(String str) { //hh_mm
        String[] temp = str.split("_");
        return Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]);
    }

    private boolean checkTimeIsCrossed(int s1, int e1 , int s2, int e2) { //두 시간을 비교하여 겹칠시 True를 리턴
        if (e1 > s2)
            if ( s1 < e2 )
                return true;
        return false;
    }

    
    //DB에 추가
    public void addSchedule() {
        long id;

        //임시 일정인 경우 하나를 추가
        if ( isTemporalSchedule ) {
            id = db.insert("schedule1" , null , makeDBvalues(makeDateString(), temporalDayofWeek - 1));
            updateTemporalPolyLineData(id);
        } else {
            //정규 일정인 경우 date 가 -1 이고 cyclic이 1 인 여러개의 일정 추가
            for (int i = 0; i < 7; i++) {
                System.out.println(periodDayofWeek[i]);
                if ( periodDayofWeek[i] ) {
                    id = db.insert("schedule1" , null , makeDBvalues("cyclic" , i));
                    updatePeriodPolyLineData(id, i);
                }
            }
        }

        Toast.makeText(context, "추가가 완료되었습니다" , Toast.LENGTH_SHORT).show();
    }
    
    //DB의 tpolyline_x 와 tpolyline_y 값을 수정함
    private void updateTemporalPolyLineData(long targetId) {
        ArrayList<String> coords = new ArrayList<>();
        ArrayList<Long> id = new ArrayList<>();

        int targetIndex = 0;
        int i;

        //임시일정의 경우 같은 Date에 있는 Cyclic = 0 인 것과 Cyclic = 1 중 같은 day_week에 있는것들을 가져옴
        Cursor c = db.rawQuery("SELECT _id,end_location FROM schedule1 " +
                "WHERE cyclic=0 AND date = '" + makeDateString() + "' OR cyclic = 1 AND day_week =" + Integer.toString(temporalDayofWeek - 1) +
                " ORDER BY start_time ASC" , null);

        if(c.moveToFirst()) {
            i = 0;
            while (!c.isAfterLast()) {
                coords.add(c.getString(1));
                id.add(c.getLong(0));

                if (c.getLong(0) == targetId)
                    targetIndex = i;
                c.moveToNext();
                i++;
            }
        }

        c.close();
        System.out.println(targetIndex);
        System.out.println(id.get(targetIndex));
        System.out.println(coords.size() - 1);

        if ( targetIndex == 0 ) { //추가한 일정이 해당 날짜의 첫번째 일정인경우
            db.execSQL("UPDATE schedule1 SET tpolyline_x = 'start', tpolyline_y ='start' WHERE _id =" + Long.toString(targetId)); //추가된 일정의 polyline은 start로 지정함
            if ( coords.size() >= 2 )
                updateWithFindPath(targetIndex, targetIndex + 1, id.get(targetIndex + 1) , coords);

        } else if ( targetIndex < coords.size() - 1 ) { //추가한 일정이 해당 날짜의 일정 사이에 끼어있는 경우
            updateWithFindPath(targetIndex-1, targetIndex, id.get(targetIndex) , coords);
            updateWithFindPath(targetIndex, targetIndex + 1, id.get(targetIndex + 1) , coords);

        } else if ( targetIndex == coords.size() - 1 ) { //추가한 일정이 해당 날짜의 마지막 일정인 경우
            if ( coords.size() >= 2 )
                updateWithFindPath(targetIndex-1, targetIndex, id.get(targetIndex) , coords);
        }

    }

    private void updatePeriodPolyLineData(long targetId, int dayOfWeek) {
        ArrayList<String> coords = new ArrayList<>();
        ArrayList<Long> id = new ArrayList<>();

        int targetIndex = 0;
        int i;

        //정규일정의 경우 Cyclic = 1 이고 day_week가 겹치는 정규일정들만 체크
        Cursor c = db.rawQuery("SELECT _id,end_location FROM schedule1 " +
                "WHERE cyclic=1 AND day_week =" + Integer.toString(dayOfWeek) + " ORDER BY start_time ASC" , null);

        if(c.moveToFirst()) {
            i = 0;
            while (!c.isAfterLast()) {
                coords.add(c.getString(1));
                id.add(c.getLong(0));

                if (c.getLong(0) == targetId)
                    targetIndex = i;
                c.moveToNext();
                i++;
            }
        }
        c.close();

        if ( targetIndex == 0 ) {
            db.execSQL("UPDATE schedule1 SET tpolyline_x = 'start', tpolyline_y ='start' WHERE _id =" + Long.toString(targetId)); //추가된 일정의 polyline은 start로 지정함
            if ( coords.size() >= 2 )
                updateWithFindPath(targetIndex, targetIndex + 1, id.get(targetIndex + 1) , coords);

        } else if ( targetIndex < coords.size() - 1 ) {
            updateWithFindPath(targetIndex-1, targetIndex, id.get(targetIndex) , coords);
            updateWithFindPath(targetIndex, targetIndex + 1, id.get(targetIndex + 1) , coords);

        } else if ( targetIndex == coords.size() - 1 ) {
            if ( coords.size() >= 2 )
                updateWithFindPath(targetIndex-1, targetIndex, id.get(targetIndex) , coords);
        }

    }

    //길찾기를 coods 의 startIndex -> endIndex 로 수행하고 DB의 targetId에 해당하는 대상의 경로를 업데이트함
    private void updateWithFindPath(int startIndex, int endIndex, Long targetId,  ArrayList<String> coords) {
        TMapPoint startPoint = getTMapPointWithStr(coords.get(startIndex));
        TMapPoint endPoint = getTMapPointWithStr(coords.get(endIndex));
        pnuPath.findPath(startPoint, endPoint, true);

        String[] str = pnuPath.getPolyLineinStr();
        System.out.println(str[0]);
        System.out.println(str[1]);

        db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + str[0] + "', tpolyline_y = '" + str[1] +
                "'WHERE _id =" + Long.toString(targetId));
    }

    private TMapPoint getTMapPointWithStr(String coordStr) {
        String[] temp = coordStr.split(",");
        return new TMapPoint(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]));
    }

    private ContentValues makeDBvalues(String dateStr, int dayOfWeek) {
        ContentValues values = new ContentValues();
        values.put("date", dateStr);
        values.put("day_week", dayOfWeek);
        values.put("start_time", String.format("%02d_%02d", startHour, startMin));
        values.put("end_time", String.format("%02d_%02d", endHour, endMin));
        values.put("start_location", "0,0");
        values.put("end_location_name", destName);
        values.put("end_location", String.format("%f,%f", destLat, destLon));
        values.put("name", name);
        values.put("script", desc);
        values.put("cyclic" , isTemporalSchedule ? 0 : 1);
        values.put("tpolyline_x" , "temp"); //lat
        values.put("tpolyline_y" , "temp"); //lon

        return values;
    }

    private String makeDateString() {
        return String.format("%d_%02d_%02d",year,month+1,day);
    }
}
