package com.example.pnuwalker.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pnuwalker.DataBaseHelper;
import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.Pair;

import java.net.Inet4Address;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.pnuwalker.MainActivity.db;

public class BootService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseHelper helper = new DataBaseHelper(this, "schedule1.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        Toast.makeText(this, "Booted" , Toast.LENGTH_SHORT).show();
        Log.d("BOOT Service", "Set Alarm");

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //현재 시간을 읽어옴
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1;
        int day = cal.get(cal.DAY_OF_MONTH);
        int hour = cal.get(cal.HOUR_OF_DAY);
        int min = cal.get(cal.MINUTE);
        int dayOfWeek = (cal.get(cal.DAY_OF_WEEK) + 5) % 7;


        cal.set(cal.SECOND, 0);

        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss (E) ");
        String strDate = dateFormat.format(cal.getTime());
        Log.d("alarm", "CurrTime : " + strDate + "Day Of Week : " + dayOfWeek );
        Intent intent = new Intent(this , AlarmReceiver.class);

        //정규일정은 모두 읽어옴
        for (int i = 0; i < 7; i++) {
            Cursor c = db.rawQuery("SELECT _id, start_time, end_time, name, end_location_name, day_week " +
                    "FROM schedule1 WHERE cyclic = 1 AND day_week = " + i + " ORDER BY start_time ASC", null);
            cal = Calendar.getInstance();
            cal.set(cal.SECOND, 0);
            cal.set(cal.DAY_OF_WEEK, dayOfWeek + 2);
            cal.add(cal.DAY_OF_YEAR, i);
            strDate = dateFormat.format(cal.getTime());

            Log.d("alarm", "PeriodTime : " + strDate );

            if (c.moveToFirst()) {
                while( !c.isAfterLast() ) {
                    long id = c.getLong(0);
                    boolean isPeriod = true;
                    boolean setStartAlarm = false;
                    int requestCode = (int)c.getLong(0) % Integer.MAX_VALUE;
                    Pair<Integer> startTime = strTimeToInt(c.getString(1));

                    cal = Calendar.getInstance();
                    cal.set(cal.SECOND, 0);
                    cal.set(cal.DAY_OF_WEEK, dayOfWeek + 2);
                    cal.add(cal.DAY_OF_YEAR, i);
                    cal.set(cal.HOUR, startTime.getFirst());
                    cal.set(cal.MINUTE, startTime.getSecond());
                    cal.add(cal.MINUTE, -25);

                    if ( i == dayOfWeek ) { //요일이 같은경우, 시간을 확인하여 일정 시작 알림을 추가하고 다음주에 (25분전 알람추가)
                        if (hour * 60 + min < startTime.getFirst() * 60 + startTime.getSecond()) { //다음주에 알람을 추가하고 일정 실행 알람 추가
                            cal.add(cal.DAY_OF_YEAR, 7);
                            setStartAlarm = true;
                        } else { //시간이 지난경우 다음주에 알람을 추가
                            cal.add(cal.DAY_OF_YEAR, 7);
                        }
                    }

                    intent.putExtra("start_time", c.getString(1).replace("_",":"));
                    intent.putExtra("end_time", c.getString(2).replace("_",":"));
                    intent.putExtra("name", c.getString(3));
                    intent.putExtra("dest_name", c.getString(4));
                    intent.putExtra("is_period", isPeriod);
                    intent.putExtra("id", id);


                    strDate = dateFormat.format(cal.getTime());
                    Log.d("alarm", strDate + " :: RequestCode : " + requestCode);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                    if ( setStartAlarm ) { //이번주에 일정을 추가
                        cal.add(cal.DAY_OF_YEAR, -7);
                        cal.add(cal.MINUTE, 25);
                        intent.putExtra("id", -id);
                        intent.putExtra("is_period", false);
                        pendingIntent = PendingIntent.getBroadcast(this, -requestCode, intent, 0);
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                        strDate = dateFormat.format(cal.getTime());
                        Log.d("alarm", strDate + " :: RequestCode : " + -requestCode);
                    }

                    c.moveToNext();
                }
            }

            c.close();
        }

        Log.d("alarm", "==================================================================");
        //임시일정은 같은 date일정 ~ 만 읽음
        String date = String.format("%d_%02d_%02d",year, month, day); //현재 날짜에 대한 date
        cal = Calendar.getInstance(); //
        cal.set(cal.SECOND, 0);

        strDate = dateFormat.format(cal.getTime());
        Log.d("alarm", "currTime : " + strDate);

        Cursor c = db.rawQuery("SELECT _id, start_time, end_time, name, end_location_name, date " +
                "FROM schedule1 WHERE cyclic >= 0 AND cyclic != 1 AND date >= '" + date +
                 "' ORDER BY date ASC, start_time ASC", null);

        Log.d("alarm", "Size : " + c.getCount());

        if ( c.moveToFirst() ) {
            while ( !c.isAfterLast() )  {
                long id = c.getLong(0);
                int requestCode = (int)id % Integer.MAX_VALUE;
                boolean addAlarm = true;
                String[] temp = c.getString(5).split("_");
                int[] d = new int[3];

                Pair<Integer> startTime = strTimeToInt(c.getString(1));

                for (int i = 0; i < 3; i++)
                    d[i] = Integer.parseInt(temp[i]);

                Log.d("alarm", "date : " + d[0] + "/" + d[1] + "/" + d[2] + startTime);
                Calendar calTemp = Calendar.getInstance(); //일정의 시작시간
                calTemp.set(cal.YEAR, d[0]);
                calTemp.set(cal.MONTH, d[1] - 1);
                calTemp.set(cal.DAY_OF_MONTH, d[2]);
                calTemp.set(cal.HOUR_OF_DAY, startTime.getFirst());
                calTemp.set(cal.MINUTE, startTime.getSecond());
                calTemp.set(cal.SECOND, 0);

                calTemp.add(cal.MINUTE, -25);

                int compareCal = calTemp.compareTo(cal);
                Log.d("alarm", "compareCal" + compareCal);
                if ( compareCal >= 0 ) {
                    //25분전 일정 추가
                } else if ( compareCal < 0 ) {
                    calTemp.add(cal.MINUTE, 25);
                    if ( calTemp.compareTo(cal) >= 0 ) {
                        requestCode = -requestCode;
                        id = -id;
                    } else {
                        addAlarm = false;
                    }
                }

                if ( addAlarm ) {
                    intent.putExtra("start_time", c.getString(1).replace("_",":"));
                    intent.putExtra("end_time", c.getString(2).replace("_",":"));
                    intent.putExtra("name", c.getString(3));
                    intent.putExtra("dest_name", c.getString(4));
                    intent.putExtra("is_period", false);
                    intent.putExtra("id", id);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calTemp.getTimeInMillis(), pendingIntent);

                    strDate = dateFormat.format(calTemp.getTime());
                    Log.d("alarm", strDate + " :: RequestCode : " + requestCode + " :: id " + id);
                }

                c.moveToNext();
            }
        }
    }

    //Pair = (hour, minute)
    private Pair<Integer> strTimeToInt(String str) {
        String[] temp = str.split("_");
        return new Pair<Integer>(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
    }

}
