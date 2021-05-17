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
import com.example.pnuwalker.Pair;
import com.example.pnuwalker.pathfind.Coordinate;
import com.example.pnuwalker.pathfind.FindPath;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

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

    String destName;
    double destLat;
    double destLon;

    String room;
    ArrayList<Long> overrideId;

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
        room = data.room;

        overrideId = new ArrayList<>();
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
        ArrayList<DaySchedule> overrideSchedule = new ArrayList<>();
        
        //같은 요일의 주기 일정 + 같은 날짜의 가상 임시일정을 얻어서 비교
        Cursor c = db.rawQuery("SELECT * FROM schedule1 WHERE (cyclic = 1 AND day_week = " + Integer.toString(temporalDayofWeek - 1) +
                ") OR (cyclic < 0 AND date = '" + makeDateString() + "') ORDER BY start_time ASC", null);

        if(c.moveToFirst()) {
            int startTime;
            int endTime;

            do {
                startTime = strTimetoMinute(c.getString(3)); //start_time
                endTime = strTimetoMinute(c.getString(4)); //end_time
                System.out.println(String.format("%d ~ %d\n", startTime, endTime));
                if (checkTimeIsCrossed(startTime, endTime, targetStartTime, targetEndTime)) {
                    long cyclic = c.getInt(10);
                    long id = c.getLong(0);
                    if (cyclic < 0) { //가상 임시일정이 겹칠시 해당 가상 임시일정을 지움1
                        db.delete("schedule1", "cyclic = " + Long.toString(cyclic), null);
                    } else { //겹치는 정규일정을 한꺼번에 처리하기 위해, List에 넣음
                        overrideSchedule.add((new DaySchedule( id,
                                c.getString(1),
                                c.getInt(2),
                                c.getString(3),
                                c.getString(4),
                                c.getString(5),
                                c.getString(6),
                                c.getString(7),
                                c.getString(8),
                                c.getString(9),
                                c.getInt(10),
                                c.getString(11),
                                c.getString(12),
                                c.getString(13),
                                c.getString(14)
                        )));
                        overrideId.add(id);
                    }
                }

                c.moveToNext();
            } while (!c.isAfterLast());
            System.out.println(overrideId);

            c.close();
        }
        
        //겹치는 일정이 있다면, override 여부를 물음
        if ( overrideSchedule.size() >= 1 && checkTempOverridePeriod(overrideSchedule) ) {
            return isTempOverridePeriod;
        } else {
            return true;
        }

    }

    //AlertDialog 의 확인, 취소를 사용해서 임시일정이 (1개 이상의)정규일정을 덮어씌울지 결정
    private boolean checkTempOverridePeriod(ArrayList<DaySchedule> overrideSchedule) {
        final Handler handler = new Handler() { @Override public void handleMessage(Message msg) {throw new RuntimeException(); }};

        String msg = "추가하고자 하는 일정이 정규 일정\n";

        for (int i = 0; i < overrideSchedule.size(); i++) {
            String dayOfWeek = intDayOfWeekToString(overrideSchedule.get(i).getDayOfWeek());
            String startTime = String.format("%02d:%02d", overrideSchedule.get(i).getStartHour(), overrideSchedule.get(i).getStartMin());
            String endTime = String.format("%02d:%02d", overrideSchedule.get(i).getEndHour(), overrideSchedule.get(i).getEndMin());
            msg += "(" + dayOfWeek + ") " + startTime + " ~ " + endTime + "\n";
        }
        msg += "\n과 겹칩니다. 해당 날짜에 대해 이 일정들을 덮어씌우시겠습니까?";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
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
        Cursor c = db.rawQuery("select start_time,end_time from schedule1 where date = '" + dateStr + "' AND cyclic >= 0" , null );

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
        if ( isTemporalSchedule ) {
            addTemporalSchedule();
        } else {
            addPeriodSchedule();
        }

        Toast.makeText(context, "추가가 완료되었습니다" , Toast.LENGTH_SHORT).show();
    }

    private void addTemporalSchedule() {
        long targetId = db.insert("schedule1" , null , makeDBvalues(makeDateString(), temporalDayofWeek - 1));
        int targetIndex = 0;
        ArrayList<DaySchedule> schedules = new ScheduleReader(dataBaseHelper, year, month+1, day,
                temporalDayofWeek - 1, 0, 0).schedules;

        System.out.println(schedules);

        for (int i = 0; i < schedules.size(); i++) {
            if ( targetId == schedules.get(i).getId() )
                targetIndex = i;
        }

        //index + 1에 있는 일정을 확인
        if ( targetIndex != schedules.size() -1 ) //마지막 일정이 아닌경우
            if ( schedules.get(targetIndex + 1).getCyclic() == 1 ) {
                addVirtualTemporalSchedule(schedules.get(targetIndex+1), schedules.get(targetIndex), false);
            } else {
                //다음 polyline 값을 자신의 일정 -> 다음 일정으로 수정
                long nextId = schedules.get(targetIndex+1).getId();
                Pair<String> pair = getPolyLineStr(schedules.get(targetIndex).getDestLon() , schedules.get(targetIndex).getDestLat(),
                        schedules.get(targetIndex + 1).getDestLon() , schedules.get(targetIndex + 1).getDestLat());
                db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                        "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(nextId));
            }
        
        //index - 1에 있는 일정을 확인
        if ( targetIndex == 0 ) { //처음 일정인 경우
            db.execSQL("UPDATE schedule1 SET tpolyline_x = 'start', tpolyline_y ='start' WHERE _id =" + Long.toString(targetId));
        } else {
            //자신의 polyline 값을 이전 일정 -> 자신의 일정으로 수정
            Pair<String> pair = getPolyLineStr(schedules.get(targetIndex - 1).getDestLon() , schedules.get(targetIndex - 1).getDestLat(),
                    schedules.get(targetIndex).getDestLon() , schedules.get(targetIndex).getDestLat());
            db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                    "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(targetId));
        }
    }

    private void addPeriodSchedule() {
        long targetId;
        Cursor c;
        Pair<String> pair;
        String startTimeStr = String.format("%02d_%02d", startHour, startMin);
        String endTimeStr = String.format("%02d_%02d", endHour, endMin);

        HashMap<String, Pair<String>> tempPolylinePair = new HashMap<>(); //key : "startCoord:endCoord" value : "tpolylineX:tpolylineY"을 가지는 Pair

        for (int i = 0; i < 7; i++) {
            if (periodDayofWeek[i]) {
                int j = 0;
                targetId = db.insert("schedule1" , null , makeDBvalues("cyclic" , i));

                c = db.rawQuery("SELECT _id,end_location FROM schedule1 " +
                        "WHERE cyclic=1 AND day_week =" + Integer.toString(i) +
                        " ORDER BY start_time ASC" , null);

                System.out.println(c.getCount());

                System.out.println("SELECT _id,end_location FROM schedule1 " +
                        "WHERE cyclic=1 AND day_week =" + Integer.toString(i) +
                        " ORDER BY start_time ASC");

                if(c.moveToFirst()) {

                    while (!c.isAfterLast()) {
                        if ( c.getLong(0) == targetId ) {

                            String currEndPos = c.getString(1);

                            if ( j == 0 ) { //처음 일정인 경우 "start" 로 추가한 일정의 polyline 설정
                                db.execSQL("UPDATE schedule1 SET tpolyline_x = 'start', tpolyline_y ='start' WHERE _id =" + Long.toString(targetId));
                            } else if ( j > 0 ) { //아닌 경우, 이전 일정 -> 현재 일정으로 추가한 일정의 polyline 설정
                                c.moveToPrevious();

                                String key = c.getString(1) + ":" + currEndPos;
                                if ( tempPolylinePair.containsKey(key) )
                                    pair = tempPolylinePair.get(key);
                                else
                                    pair = getPolyLineStr(c.getString(1), currEndPos);
                                tempPolylinePair.put(key, pair);

                                System.out.println("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                                        "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(targetId));

                                db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                                        "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(targetId));

                                c.moveToNext();
                            }

                            if ( c.moveToNext() ) { //다음 정규 일정이 있다면, polyline을 변경
                                String key = currEndPos + ":" + c.getString(0);
                                if ( tempPolylinePair.containsKey(key) )
                                    pair = tempPolylinePair.get(key);
                                else
                                    pair = getPolyLineStr(currEndPos, c.getString(1));
                                tempPolylinePair.put(key, pair);

                                db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                                        "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(c.getLong(0)));
                            }
                            break;
                        }
                        j++;
                        c.moveToNext();
                    }
                }

                //Override 수정
                c = db.rawQuery("SELECT _id, cyclic, additional_override_id, date, day_week FROM schedule1 " +
                                    "WHERE (cyclic >= 0 AND cyclic != 1) AND day_week = " + Integer.toString(i) +
                                    " AND ('" + endTimeStr + "' >= start_time" +
                                    " AND '" + startTimeStr + "' <= end_time)", null);

                ArrayList<String> dateStr = new ArrayList<>();
                ArrayList<Integer> dayWeek = new ArrayList<>();
                if(c.moveToFirst()) {
                    while( c.isAfterLast() ) {
                        if ( c.getLong(1) == 0 ) {
                            db.rawQuery("UPDATE schedule1 SET cyclic = " + Long.toString(targetId) +
                                            " WHERE _id = " + Long.toString(c.getLong(0)), null);
                        } else {
                            db.rawQuery("UPDATE schedule1 SET additional_override_id = '" + c.getString(2) + "," + Long.toString(targetId) + "' " +
                                    "WHERE _id = " + Long.toString(c.getLong(0)), null);
                        }

                        dateStr.add(c.getString(3));
                        dayWeek.add(c.getInt(4));
                        c.moveToNext();
                    }
                }

                if (dateStr.size() > 0 ) {
                    //Schedule을 받음
                    String[] temp = dateStr.get(i).split("_");
                    int year = Integer.parseInt(temp[0]);
                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[2]);
                    ArrayList<DaySchedule> schedules = new ScheduleReader(dataBaseHelper, year, month, day, dayWeek.get(i), 0 ,0).schedules;

                    int targetIndex = 0;
                    for (int k = 0; k < schedules.size(); k++) {
                        if ( schedules.get(k).getId() == targetId )
                            targetIndex = k;
                    }

                    //index + 1에 있는 일정을 확인
                    if ( targetIndex != schedules.size() -1 ) //마지막 일정이 아닌경우
                        if ( schedules.get(targetIndex + 1).getCyclic() != 1 ) {
                            //다음 polyline 값을 자신의 일정 -> 다음 일정으로 수정
                            long nextId = schedules.get(targetIndex+1).getId();
                            pair = getPolyLineStr(schedules.get(targetIndex).getDestLon() , schedules.get(targetIndex).getDestLat(),
                                    schedules.get(targetIndex + 1).getDestLon() , schedules.get(targetIndex + 1).getDestLat());
                            db.execSQL("UPDATE schedule1 SET tpolyline_x = '" + pair.getFirst() +
                                    "', tpolyline_y ='" + pair.getSecond() + "' WHERE _id =" + Long.toString(nextId));
                        }

                    //index - 1에 있는 일정을 확인
                    if ( targetIndex > 0 ) { //처음 일정이 아닌경우
                        long prevCyclic = schedules.get(targetIndex - 1).getCyclic();
                        if ( prevCyclic >= 0 && prevCyclic != 1 )
                            addVirtualTemporalSchedule(schedules.get(targetIndex) , schedules.get(targetIndex-1) , false);
                    }
                }
            }
        }
    }

    //가상 임시일정을 만들고 cyclic과 polyline수정후 db에 추가하여 추가된 일정의 id를 리턴
    //isPeriodFirst = true이면 (가상 임시일정) -> (임시일정) 으로 경로값을 집어넣음, false이면 반대
    private long addVirtualTemporalSchedule(DaySchedule period, DaySchedule temporal, boolean isPeriodStart) {
        DaySchedule virtual = new DaySchedule(period);
        virtual.setCyclic(-period.getId());
        virtual.setDate(temporal.getYear(), temporal.getMonth(), temporal.getDay(), temporal.getDayOfWeek());

        TMapPolyLine line;
        if ( isPeriodStart )
            line = getPolyLine(period.getDestLon(), period.getDestLat(), temporal.getDestLon(), temporal.getDestLat());
        else
            line = getPolyLine(temporal.getDestLon(), temporal.getDestLat(), period.getDestLon(), period.getDestLat());
        virtual.setPolyLine(line);
        System.out.println(virtual.getPolyLine());

        return db.insert("schedule1", null, virtual.getContentValues());
    }

    public void deleteSchedule() {
    }

    //두개의 end_position String을 받아, tpolyline_x:tpolyline_y 의 String으로 리턴 두 위치가 같은 경우에는 빈문자열 리턴
    private Pair<String> getPolyLineStr(String start, String end) {
        TMapPoint startPoint = getTMapPointWithStr(start);
        TMapPoint endPoint = getTMapPointWithStr(end);

        System.out.println("getPolyLineStr = start : " + start + " , end : " + end);
        if ( startPoint.getLongitude() == endPoint.getLongitude() &&
                startPoint.getLatitude() == endPoint.getLatitude() ) {
            return new Pair<String>("", "");
        }

        pnuPath.findPath(startPoint, endPoint , true);
        Pair<String> p = pnuPath.getPolyLineinStr();
        System.out.println(p);
        return p;
    }

    private Pair<String> getPolyLineStr(double startLon, double startLat, double endLon, double endLat) {
        TMapPoint startPoint = new TMapPoint(startLat, startLon);
        TMapPoint endPoint = new TMapPoint(endLat, endLon);
        if ( startPoint.getLongitude() == endPoint.getLongitude() &&
                startPoint.getLatitude() == endPoint.getLatitude() ) {
            return new Pair<String>("", "");
        }

        pnuPath.findPath(startPoint, endPoint , true);
        Pair<String> p = pnuPath.getPolyLineinStr();
        System.out.println(p);
        return p;
    }

    private TMapPolyLine getPolyLine(double startLon, double startLat, double endLon, double endLat) {
        TMapPoint startPoint = new TMapPoint(startLat, startLon);
        TMapPoint endPoint = new TMapPoint(endLat, endLon);
        if ( startPoint.getLongitude() == endPoint.getLongitude() &&
                startPoint.getLatitude() == endPoint.getLatitude() ) {
            return null;
        }

        pnuPath.findPath(startPoint, endPoint , true);
        return pnuPath.getPolyLine();
    }


    private TMapPoint getTMapPointWithStr(String coordStr) {
        System.out.println("getTMapPointWithStr : " + coordStr);
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
        if ( isTempOverridePeriod ) { //일정이 override 된다면, cyclic과 추가 override 정보를 설정
            values.put("cyclic" , overrideId.get(0));
            if ( overrideId.size() >= 2 ) {
                String str = "";
                for (int i = 1; i < overrideId.size(); i++) {
                    str += Long.toString(overrideId.get(i));
                    if ( i != overrideId.size() - 1 )
                        str += ",";
                }
                values.put("additional_override_id" , str);
            }
        } else { //override 하지 않는경우, cyclic이 0또는 1 이고 addtional_cyclic_id가 빈문자열임
            values.put("cyclic" , isTemporalSchedule ? 0 : 1);
            values.put("additional_override_id", "");
        }
        values.put("tpolyline_x" , ""); //lat
        values.put("tpolyline_y" , ""); //lon
        values.put("room", room);
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

    private String intDayOfWeekToString(int dayOfWeek) {
        String[] arr = {"월","화","수","목","금","토","일"};

        if (dayOfWeek >= 7) { return arr[6]; }
        else if ( dayOfWeek < 0 ) { return arr[0]; }

        return arr[dayOfWeek];
    }

}
