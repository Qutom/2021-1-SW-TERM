package com.example.pnuwalker.controlschedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.pnuwalker.DataBaseHelper;
import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.pathfind.FindPath;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ControlSchedule {
    FindPath pnuPath;
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
    boolean isTempOverridePeriod = false; //임시 일정이 정규일정을 덮어 씌우는가?
    long overrideId; //만약 덮어 씌운다면 어떤 id를 가지는 일정을 덮어씌우는가?

    String destName;
    double destLat;
    double destLon;

    //정규 일정인 경우 values 값에 startHour, startMin, endHour, endMin, destName, destLat, destLon 만 필요
    public ControlSchedule(DataBaseHelper helper, Activity context, PeriodScheduleData data) {
        pnuPath = new FindPath(context);
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
        pnuPath = new FindPath(context);
        dataBaseHelper = helper;
        db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(db);

        this.context = context;

        setCommonData(data);
        year = data.year;
        month = data.month;
        day = data.day;
        temporalDayofWeek = data.dayofWeek;
        overrideId = 0;
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

        if ( targetStartTime == targetEndTime ) {
            Toast.makeText(context, "시작시간이 종료시간과 같습니다" , Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean pass = true;

        if (isTemporalSchedule) { // 임시일정인가?
            /*
                임시일정일 때,
                1. 같은 Date를 가지는 값에서 time이 겹치는 경우
                2. 정규 일정(cyclic = 1)을 비교하여 time 이 겹치는 경우 등록 불가능
             */

            //같은 date를 가지는(yyyy_mm_dd)를 비교
            if ( checkTempToTempSchedule(targetStartTime, targetEndTime) ) {
                //정규 일정 비교
                return checkTempToPeriodSchedule(targetStartTime, targetEndTime);
            } else {
                return false;
            }

        } else {
            //정규일정일 때, 정규 일정끼리만 비교하여 time이 겹치는 경우 탈락
            return checkPeriodSchedule(targetStartTime, targetEndTime);
        }

    }

    private boolean checkTempToPeriodSchedule(int targetStartTime, int targetEndTime) {
        System.out.println("Check Temp to Period");
        //Cyclic 일정을 비교
        Cursor c = db.rawQuery("select start_time,end_time,day_week,end_location_name,name,script,_id " +
                                    "from schedule1 where cyclic = 1 and day_week = " +
                                    Integer.toString(temporalDayofWeek - 1) , null); //같은 요일에 주기 일정들을 얻음

        if(c.moveToFirst()){
            System.out.println("1111111");
            int startTime;
            int endTime;
            do {
                startTime = strTimetoMinute(c.getString(0)); //start_time
                endTime = strTimetoMinute(c.getString(1)); //end_time
                System.out.println(String.format("%d ~ %d\n",startTime, endTime));

                if ( checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime) ) { //시간이 겹침 -> 겹치기 여부 묻기
                    if ( checkTempOverridePeriod(c.getString(4) , c.getString(5), c.getString(0) ,
                            c.getString(1), c.getInt(2) , c.getString(3)) ) {
                        overrideId = c.getLong(6);  // 겹치는 ID를 저장
                        c.close();

                        System.out.println(overrideId);
                        return true;
                    } else {
                        return false;
                    }
                }
                c.moveToNext();
            } while( !c.isAfterLast() );
        }
        c.close();
        return true;
    }

    //AlertDialog 의 확인, 취소를 사용해서 임시일정이 정규일정을 덮어씌울지 결정
    private boolean checkTempOverridePeriod(String _name, String _desc, String _startTime, String _endTime, int _dayOfWeek, String _destName) {
        final Handler handler = new Handler() { @Override public void handleMessage(Message mesg) {throw new RuntimeException(); }};

        String descDisplay = "";
        if (_desc != "") {
            descDisplay = "[" + _desc + "]";
        }
        _startTime = _startTime.replace("_", " : ");
        _endTime = _endTime.replace("_", " : ");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("정규 일정\n" + _name + "\n" + descDisplay + "\n" + _startTime + " ~ " + _endTime +
                " (" + intDayOfWeekToString(_dayOfWeek) + ")\n" + _destName + "\n\n" + "과 해당 일정이 겹칩니다. 이 일정을 위의 일정에 덮어 씌우시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("예",(dialog,which)-> {isTempOverridePeriod = true; handler.sendMessage(handler.obtainMessage());});
        builder.setNegativeButton("아니오",(dialog,which)-> {isTempOverridePeriod = false; handler.sendMessage(handler.obtainMessage());});
        builder.create().show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        System.out.println("Result: " + isTempOverridePeriod);
        return isTempOverridePeriod;
    }



    private boolean checkTempToTempSchedule(int targetStartTime, int targetEndTime) {
        //같은 date를 가지는(yyyy_mm_dd)를 비교
        System.out.println("Check Temp to Temp");
        String dateStr = String.format("%d_%02d_%02d",year, month + 1, day);
        Cursor c = db.rawQuery("select start_time,end_time from schedule1 where date = '" + dateStr + "'" , null );

        if(c.moveToFirst()) {
            int startTime;
            int endTime;
            do {
                startTime = strTimetoMinute(c.getString(0)); //start_time
                endTime = strTimetoMinute(c.getString(1)); //end_time
                System.out.println(String.format("%d ~ %d\n",startTime , endTime));

                if ( checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime) ) { //시간이 겹침 -> 탈락
                    Toast.makeText(context, "시간이 겹치는 임시 일정이 있습니다" , Toast.LENGTH_SHORT).show();
                    c.close();
                    return false;
                }
                c.moveToNext();
            } while( !c.isAfterLast() );
        }
        c.close();
        return true;
    }

    private boolean checkPeriodSchedule(int targetStartTime, int targetEndTime) {
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
                        c.close();
                        return false;
                    }
                }
                c.moveToNext();
            }
        }
        c.close();
        return true;
    }

    private int strTimetoMinute(String str) { //hh_mm
        String[] temp = str.split("_");
        return Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]);
    }

    private boolean checkTimeIsCrossed(int s1, int e1 , int s2, int e2) { //두 시간을 비교하여 겹칠시 True를 리턴
        if (e1 == s2 || s1 == e2)
            return true;
        if (e1 > s2)
            if (s1 < e2)
                return true;
        return false;
    }

    
    //DB에 추가
    public void addSchedule() {
        long id;

        //임시 일정인 경우 하나를 추가
        if ( isTemporalSchedule ) {
            id = db.insert("schedule1" , null , makeDBvalues(makeDateString(), temporalDayofWeek - 1));
            updateTemporalPolyLineData(id , makeDateString() , overrideId , Integer.toString(temporalDayofWeek - 1));
        } else {
            //정규 일정인 경우 date 가 -1 이고 cyclic이 1 인 여러개의 일정 추가
            for (int i = 0; i < 7; i++) {
                System.out.println(periodDayofWeek[i]);
                if ( periodDayofWeek[i] ) {
                    id = db.insert("schedule1" , null , makeDBvalues("cyclic" , i));
                    //updateTemporalCyclic(id);
                    updatePeriodPolyLineData(id, i);
                }
            }
        }

        Toast.makeText(context, "추가가 완료되었습니다" , Toast.LENGTH_SHORT).show();
    }


    public void deleteSchedule() {
        //DB에서 찾음
        HashMap<Integer, Long> targetId = new HashMap<>(7);
        Cursor c;
        if ( isTemporalSchedule ) {
            //임시 일정의 경우, 같은 date,start_time, end_time을 가지고 cyclic != 1 인 일정 하나를 가져옴
            String[] selectionArgs = {makeDateString(), String.format("%02d_%02d", startHour, startMin) , String.format("%02d_%02d", endHour, endMin)};
            c = db.rawQuery("SELECT _id FROM schedule1 WHERE date = ? AND cyclic != 1 " +
                                 "AND start_time = ? AND end_time = ? ORDER BY start_time ASC", selectionArgs);

            if (c.moveToFirst()) {
                targetId.put(-1 ,c.getLong(0)); //Temporal 일정은 -1 로 추가
            } else {
                Toast.makeText(context, "삭제하고자 하는 일정이 없습니다" , Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            //정규 일정의 경우, 7개의 day_week에 대해 같은 start_time , end_time 을 가지고 cyclic = 1 인 일정을 모두 target으로 가져옴
            String[] selectionArgs = {String.format("%02d_%02d", startHour, startMin) , String.format("%02d_%02d", endHour, endMin)};
            c = db.rawQuery("SELECT _id,day_week FROM schedule1 WHERE cyclic = 1 " +
                                 "AND start_time = ? AND end_time = ? ORDER BY start_time ASC", selectionArgs);

            if (c.moveToFirst()) {
                while (!c.isAfterLast())
                    targetId.put(c.getInt(1) , c.getLong(0)); //targetId[day_week] 로 추가
            } else {
                Toast.makeText(context, "삭제하고자 하는 일정이 없습니다" , Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ArrayList<Long> id = new ArrayList<>();
        ArrayList<Integer> cyclic = new ArrayList<>();
        ArrayList<String> coords = new ArrayList<>();

        //polyLine 재설정
        String[] selectionArgs = {};
        for ( int key : targetId.keySet() ) {
            if ( key == - 1 ) {//임시일정
                c = db.rawQuery("SELECT _id,cyclic,end_location FROM schedule1 " +
                                     "WHERE date = ? AND cyclic != 1"+
                                     " OR cyclic = 1 AND _id != ? AND day_week = ?" +
                                     " ORDER BY start_time ASC" , selectionArgs);
            }
        }

    }

    //DB의 tpolyline_x 와 tpolyline_y 값을 수정함
    private void updateTemporalPolyLineData(long targetId, String dateString, long overrideId, String dayWeekString) {
        ArrayList<String> coords = new ArrayList<>();
        ArrayList<Long> id = new ArrayList<>();

        int targetIndex = 0;
        int i;

        String[] selectionArgs = {dateString, Long.toString(overrideId), dayWeekString};
        //임시일정의 경우 같은 Date에 있는 다른 임시일정과 같은 day_week를 가지고 override 되지 않은(cyclic != _id) 정규일정들을 고려하여 경로를 설정 
        Cursor c = db.rawQuery("SELECT _id,end_location FROM schedule1 " +
                                    "WHERE date = ? AND cyclic != 1"+
                                    " OR cyclic = 1 AND _id != ? AND day_week = ?" +
                                    " ORDER BY start_time ASC" , selectionArgs);

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
                                    "WHERE cyclic=1 AND day_week =" + Integer.toString(dayOfWeek) +
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
        if ( startPoint.getLongitude() == endPoint.getLongitude() &&
                startPoint.getLatitude() == endPoint.getLatitude() ) {
            return;
        }
        pnuPath.findPath(startPoint, endPoint , true);

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
        if ( isTempOverridePeriod )
            values.put("cyclic" , overrideId );
        else
            values.put("cyclic" , isTemporalSchedule ? 0 : 1);
        values.put("tpolyline_x" , ""); //lat
        values.put("tpolyline_y" , ""); //lon

        return values;
    }

    private String makeDateString() {
        return String.format("%d_%02d_%02d",year,month+1,day);
    }

    private int[] timeStringToInt(String str) {
        String temp[] = str.split("_");

        int[] result = new int[2];
        for (int i = 0; i < 2; i++)
            result[i] = Integer.parseInt(temp[i]);

        return result;
    }

    private String intDayOfWeekToString(int dayOfWeeK) {
        String[] arr = {"월","화","수","목","금","토","일"};

        if (dayOfWeeK >= 7) { return arr[6]; }
        else if ( dayOfWeeK < 0 ) { return arr[0]; }

        return arr[dayOfWeeK];
    }

}
