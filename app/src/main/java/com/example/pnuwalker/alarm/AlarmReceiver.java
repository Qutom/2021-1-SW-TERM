package com.example.pnuwalker.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.main.MainFragment;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver(){ }

    NotificationManager manager2;
    NotificationCompat.Builder builder2;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel2";
    private static String CHANNEL_NAME = "Channel2";


    @Override
    public void onReceive(Context context, Intent intent) {
        builder2 = null;
        manager2 = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager2.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder2 = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder2 = new NotificationCompat.Builder(context);
        }

        long id = intent.getLongExtra("id", 0);
        String startTime = intent.getStringExtra("start_time");
        String endTime = intent.getStringExtra("end_time");
        String name = intent.getStringExtra("name");
        String destName = intent.getStringExtra("dest_name");
        boolean isPeriod = intent.getBooleanExtra("is_period", false);

        if ( isPeriod ) {
            builder2.setContentTitle("25분뒤에 다음 일정이 있습니다");
            builder2.setContentText(startTime + " ~ " + endTime + "| " + name + "(" + destName + ")");

            intent.putExtra("id", -id);
            int requestCode = (int) id % Integer.MAX_VALUE;

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());

            //25분 뒤를 위한 알람
            cal.add(cal.MINUTE, 25);
            intent.putExtra("id", -id);
            intent.putExtra("is_period", false);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, -requestCode, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

            //다음 주를 위한 알람
            cal.add(cal.MINUTE, -25);
            cal.add(cal.DAY_OF_YEAR, 7);
            intent.putExtra("id", id);
            intent.putExtra("is_period", true);
            alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

        } else { //임시 일정 또는 일정 시작 알림
            if ( id > 0 ) { //25분전 알림인 경우
                builder2.setContentTitle("25분뒤에 다음 일정이 있습니다");
                builder2.setContentText(startTime + " ~ " + endTime + "| " + name + "(" + destName + ")");

                intent.putExtra("id", -id);
                int requestCode = (int) id % Integer.MAX_VALUE;

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(cal.MINUTE, 1);

                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, -requestCode, intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
            } else if ( id < 0 ) {
                builder2.setContentTitle("다음 일정이 시작되었습니다");
                builder2.setContentText(startTime + " ~ " + endTime + "| " + name + "(" + destName + ")");
            }
        }


        //알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setAction(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 아이콘
        builder2.setSmallIcon(R.drawable.ic_baseline_directions_walk_24);
        //알림창 터치시 자동 삭제
        builder2.setAutoCancel(true);

        builder2.setContentIntent(pendingIntent);

        Notification notification = builder2.build();
        manager2.notify(1,notification);

    }
}
