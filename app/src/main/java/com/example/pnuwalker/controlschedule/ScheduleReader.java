package com.example.pnuwalker.controlschedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pnuwalker.DataBaseHelper;
import com.example.pnuwalker.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScheduleReader {
    public ArrayList<DaySchedule> schedules;
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;
    
    //특정 날짜의 특정 시간이후 남은 일정을 읽어옴
    public ScheduleReader(DataBaseHelper helper, int year, int month, int day, int dayOfWeek, int hour, int minute) {
        schedules = new ArrayList<>();

        dataBaseHelper = helper;
        db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(db);

        HashMap<Long, DaySchedule> periodSchedule = new HashMap<>();
        //정규일정 읽기
        String[] args = {Integer.toString(dayOfWeek), String.format("%02d_%02d", hour, minute)};
        Cursor c = db.rawQuery("SELECT * FROM 'schedule1' WHERE day_week = ? " +
                                    "AND ((? < start_time) OR (? > start_time AND ? < end_time)) " +
                                    "AND cyclic = 1 ORDER BY start_time ASC", args);

        if ( c.moveToFirst() ) {
            while( !c.isAfterLast() ) {
                periodSchedule.put(c.getLong(0),
                                new DaySchedule( c.getLong(0),   //id
                                    c.getString(1), //date
                                    c.getInt(2),    //day_week
                                    c.getString(3), //start_time
                                    c.getString(4), //end_time
                                    c.getString(5), //start_pos
                                    c.getString(6), //end_pos
                                    c.getString(7), //end_name
                                    c.getString(8), //name
                                    c.getString(9), //desc
                                    c.getInt(10),   //cyclic
                                    c.getString(11),//addtional_override_id
                                    c.getString(12),//polyline_x
                                    c.getString(13),//polyline_y
                                    c.getString(14), //room
                                    c.getLong(15) //period_head
                ));
                c.moveToNext();
            }
        }
        //임시일정 읽기
        HashMap<Long, DaySchedule> temporalSchedule = new HashMap<>(); //Override를 수행하지 않는 임시일정들을 저장
        ArrayList<DaySchedule> overrideSchedule = new ArrayList<>(); //Override를 수행하는 임시일정들을 저장
        ArrayList<Long> overrideId = new ArrayList<>(); //Override 당하는 정규일정들의 Id를 저장

        args[0] = String.format("%d_%02d_%02d",year, month, day);
        System.out.println(args[0]);
        args[1] = String.format("%02d_%02d", hour, minute);
        c = db.rawQuery("SELECT * FROM 'schedule1' WHERE date = ? " +
                "AND ((? < start_time) OR (? > start_time AND ? < end_time)) " +
                "AND cyclic != 1 ORDER BY start_time ASC", args);

        if ( c.moveToFirst() ) {
            while( !c.isAfterLast() ) {
                long cyclic = c.getInt(10);

                if ( cyclic == 0) { //Override 하지 않은 임시일정
                    temporalSchedule.put(c.getLong(0),
                                new DaySchedule( c.getLong(0),   //id
                                    c.getString(1), //date
                                    c.getInt(2),    //day_week
                                    c.getString(3), //start_time
                                    c.getString(4), //end_time
                                    c.getString(5), //start_pos
                                    c.getString(6), //end_pos
                                    c.getString(7), //end_name
                                    c.getString(8), //name
                                    c.getString(9), //desc
                                    c.getInt(10),   //cyclic
                                    c.getString(11),//addtional_override_id
                                    c.getString(12),//polyline_x
                                    c.getString(13),//polyline_y
                                    c.getString(14), //room
                                    c.getLong(15) //period_head
                            ));
                } else if ( cyclic > 1 || cyclic < 0 ) { //Override 한 임시일정
                    overrideSchedule.add(new DaySchedule( c.getLong(0),   //id
                                            c.getString(1), //date
                                            c.getInt(2),    //day_week
                                            c.getString(3), //start_time
                                            c.getString(4), //end_time
                                            c.getString(5), //start_pos
                                            c.getString(6), //end_pos
                                            c.getString(7), //end_name
                                            c.getString(8), //name
                                            c.getString(9), //desc
                                            c.getInt(10),   //cyclic
                                            c.getString(11),//addtional_override_id
                                            c.getString(12),//polyline_x
                                            c.getString(13),//polyline_y
                                            c.getString(14), //room
                                            c.getLong(15) //period_head
                    ));

                    if (!overrideId.contains(Math.abs(cyclic)))
                        overrideId.add(cyclic);

                    for (long v : overrideSchedule.get(overrideSchedule.size() - 1).getAdditionalOverrideId())
                        if (!overrideId.contains(v))
                            overrideId.add(v);
                }
                c.moveToNext();
            }

            System.out.println(overrideId);
        }
        /*
        System.out.println("Period");
        System.out.println(periodSchedule);
        System.out.println("Temporal");
        System.out.println(temporalSchedule);
        System.out.println("Override");
        System.out.println(overrideSchedule);
        Log.d("1", "override 설정");
        */

        //periodSchedule에 Override된 일정 넣기
        ArrayList<Long> periodKeySet = new ArrayList<>(periodSchedule.size());

        for (long v : periodSchedule.keySet())
            periodKeySet.add(v);

        for (Long periodKey : periodKeySet) {
            if (periodSchedule.get(periodKey) == null)
                continue;

            long periodId = periodSchedule.get(periodKey).getId();
            boolean isOverride = false;
            
            //Override된 정규일정들의 id들을 overrideId에서 받아와 periodSchedule에서 제거
            //overrideId에는 이미 additional_override_id가 들어가 있으므로 overrideId만 참조해서 제거만 수행하면 됨
            if ( overrideId.contains(periodId) || overrideId.contains(-periodId) ) {
                periodSchedule.remove(periodId);
                isOverride = true;
            }

            if ( isOverride ) {
                for (int j = 0; j < overrideSchedule.size(); j++) {
                    if ( overrideSchedule.get(j).getCyclic() == periodId ) {
                        periodSchedule.put( overrideSchedule.get(j).getId(), overrideSchedule.get(j) );
                    } else if ( -overrideSchedule.get(j).getCyclic() == periodId ) {
                        periodSchedule.put( overrideSchedule.get(j).getId(), overrideSchedule.get(j) );
                    }
                }
            }
        }

        //periodSchedule 와 temporalSchedule 을 start_time 순으로 Merge
        ArrayList<DaySchedule> periodList = new ArrayList<>(periodSchedule.values());
        Collections.sort(periodList, (v1, v2) -> compareInt(v1.getStartHour()*100 + v1.getStartMin(), v2.getStartHour()*100 + v2.getStartMin()) );

        ArrayList<DaySchedule> temporalList = new ArrayList<>(temporalSchedule.values());
        Collections.sort(temporalList, (v1, v2) -> compareInt(v1.getStartHour()*100 + v1.getStartMin(), v2.getStartHour()*100 + v2.getStartMin()) );
        int i = 0;
        int j = 0;

        while( i < periodList.size() && j < temporalList.size() ) {
            if ( periodList.get(i).getStartHour() * 100 + periodList.get(i).getStartMin() >
                    temporalList.get(j).getStartHour() * 100 + temporalList.get(j).getStartMin() ) {
                schedules.add(temporalList.get(j));
                j++;
            } else {
                schedules.add(periodList.get(i));
                i++;
            }
        }

        if (i == periodList.size())
            while( j < temporalList.size() ) {
                schedules.add(temporalList.get(j));
                j++;
            }
        else
            while( i < periodList.size() ) {
                schedules.add(periodList.get(i));
                i++;
            }
    }

    public String toString() {
        String s = "";
        for (DaySchedule sch : schedules) {
            s+= sch + "\n";
        }
        return s;
    }

    private int compareInt(int t1, int t2) {
        if ( t1 > t2 )
            return 1;
        else if ( t1 == t2 )
            return 0;
        else
            return -1;
    }
}
