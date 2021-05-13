package com.example.pnuwalker.controlschedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pnuwalker.DataBaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        ArrayList<DaySchedule> periodSchedule = new ArrayList<>();
        Log.d("1", "정규일정 읽기");
        //정규일정 읽기
        String[] args = {Integer.toString(dayOfWeek), String.format("%02d_%02d", hour, minute)};
        Cursor c = db.rawQuery("SELECT * FROM 'schedule1' WHERE day_week = ? " +
                                    "AND ((? < start_time) OR (? > start_time AND ? < end_time)) " +
                                    "AND cyclic = 1 ORDER BY start_time ASC", args);

        if ( c.moveToFirst() ) {
            while( !c.isAfterLast() ) {
                periodSchedule.add(new DaySchedule( c.getLong(0),
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
                        c.getString(12)
                ));
                c.moveToNext();
            }

                Log.d("1" , Integer.toString(periodSchedule.size()));
        }

        Log.d("1", "임시일정 읽기");
        //임시일정 읽기
        ArrayList<DaySchedule> temporalSchedule = new ArrayList<>();
        ArrayList<DaySchedule> overrideSchedule = new ArrayList<>();
        ArrayList<Long> overrideId = new ArrayList<>();

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
                    temporalSchedule.add(new DaySchedule( c.getLong(0),
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
                            c.getString(12)
                    ));
                } else if ( cyclic > 1 || cyclic < 0 ) { //Override 한 임시일정
                    overrideSchedule.add(new DaySchedule( c.getLong(0),
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
                            c.getString(12)
                    ));

                    if (!overrideId.contains(Math.abs(cyclic))) {
                        overrideId.add(Math.abs(cyclic));
                    }
                }
                c.moveToNext();
            }
        }

        Log.d("1", "override 설정");
        //periodSchedule에 Override된 일정 넣기
        for (int i = 0; i < periodSchedule.size(); i++) {
            long periodId = periodSchedule.get(i).getId();

            if ( overrideId.contains(periodId) ) {
                periodSchedule.remove(i);
                int offset = 0;

                for (int j = 0; j < overrideSchedule.size(); j++) {
                    if ( overrideSchedule.get(j).getCyclic() == periodId ) {
                        periodSchedule.add( i + offset, overrideSchedule.get(j) );
                        offset++;
                    }
                }
            }
        }

        Log.d("1", "merge");
        //periodSchedule 와 temporalSchedule 을 start_time 순으로 Merge
        int i = 0;
        int j = 0;

        while( i < periodSchedule.size() && j < temporalSchedule.size() ) {
            if ( periodSchedule.get(i).getStartHour() * 100 + periodSchedule.get(i).getStartMin() >
                    temporalSchedule.get(j).getStartHour() * 100 + temporalSchedule.get(j).getStartMin() ) {
                schedules.add(temporalSchedule.get(j));
                j++;
            } else {
                schedules.add(periodSchedule.get(i));
                i++;
            }
        }

        if (i == periodSchedule.size())
            while( j < temporalSchedule.size() ) {
                schedules.add(temporalSchedule.get(j));
                j++;
            }
        else
            while( i < periodSchedule.size() ) {
                schedules.add(periodSchedule.get(i));
                i++;
            }
    }
    
}
