package com.example.pnuwalker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

//AddScheduleActivity를 호출하기 위해서, 반드시 Intent를 통해 startPoint , endPoint정보를 보내야함
//AddSchedule이 성공적으로 종료되었을때, 일정 이름, 일정 설명, 주기 일정여부(boolean), 시간, StartPoint, EndPoint 을 종료와 함께 보냄
public class AddScheduleActivity extends AppCompatActivity {

    Button showDateDialogBtn;
    Button showStartTimeDialogBtn;
    Button showEndTimeDialogBtn;
    Button showPeriodLayoutBtn;
    Button showTemporaryLayoutBtn;
    Button cancelBtn;
    Button doneBtn;

    LinearLayout periodScheduleLayout;
    LinearLayout temporaryScheduleLayout;

    int year;
    int month;
    int day;
    int startHour;
    int startMin;
    int endHour;
    int endMin;

    boolean isTemporalSchedule = false;
    String scheduleName;
    double startLat;
    double startLon;

    double endLat;
    double endLon;

    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        showDateDialogBtn = (Button)findViewById(R.id.show_date_dialog_btn);
        showStartTimeDialogBtn = (Button)findViewById(R.id.show_start_time_dialog_btn);
        showEndTimeDialogBtn = (Button)findViewById(R.id.show_end_time_dialog_btn);
        showPeriodLayoutBtn = (Button)findViewById(R.id.show_period_layout_btn);
        showTemporaryLayoutBtn = (Button)findViewById(R.id.show_temporary_layout_btn);
        cancelBtn = (Button)findViewById(R.id.cancel_btn);
        doneBtn = (Button)findViewById(R.id.done_btn);

        temporaryScheduleLayout = (LinearLayout)findViewById(R.id.temporary_schedule_layout);
        periodScheduleLayout = (LinearLayout)findViewById(R.id.period_schedule_layout);

        showDateDialogBtn.setOnClickListener((v -> {showDateDialog();}));
        showStartTimeDialogBtn.setOnClickListener(( v -> {showTimeDialog("start");}));
        showEndTimeDialogBtn.setOnClickListener(( v -> {showTimeDialog("end");}));
        showPeriodLayoutBtn.setOnClickListener((v -> {showPeriodLayout();}));
        showTemporaryLayoutBtn.setOnClickListener((v -> {showTemporaryLayout();}));
        cancelBtn.setOnClickListener((v -> { finish(); }));
        doneBtn.setOnClickListener((v -> { checkInput(); }));

        intent = getIntent();

        //현재 시간을 기본으로 사용함
        getDateTime();
        setDateDialogBtnText(getDateString());
        setStartTimeDialogBtnText(getTimeString(startHour, startMin));
        setEndTimeDialogBtnText(getTimeString(endHour, endMin));

        showTemporaryLayout();
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int _year, int _month, int _dayOfMonth) {
                boolean change = false;

                Calendar cal = Calendar.getInstance();
                int currYear = cal.get(cal.YEAR);
                int currMonth = cal.get(cal.MONTH);
                int currDay = cal.get(cal.DAY_OF_MONTH);

                if (_year > currYear ) {
                    change = true;
                } else if ( _year == currYear ) {
                    if ( _month > currMonth ) {
                        change = true;
                    } else if ( _month == currMonth ) {
                        if ( _dayOfMonth >= currDay ) {
                            change = true;
                        }
                    }
                }

                if ( change ) {
                    year = _year;
                    month = _month;
                    day = _dayOfMonth;
                }

                setDateDialogBtnText(getDateString());
            }
        }, year ,month, day);

        dialog.setMessage("날짜 선택");
        dialog.show();
    }

    private void showTimeDialog(String type) {
        TimePickerDialog dialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar ,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                boolean change = false;

                Calendar cal = Calendar.getInstance();
                int currDay = cal.get(cal.DAY_OF_MONTH);

                if ( currDay == day ) { //같은 날짜에는 현재보다 과거의 시간을 선택할 수 없음.
                    if ( cal.get(cal.HOUR_OF_DAY) * 60 + cal.get(cal.MINUTE) <= hourOfDay *60 + minute ) {
                        change = true;
                    } else { // 같은 날자에서 과거의 시간이 선택되었을시 현재 시간으로 표기함
                        change = true;
                        hourOfDay = cal.get(cal.HOUR_OF_DAY);
                        minute = cal.get(cal.MINUTE);
                    }
                } else if ( currDay < day ) {
                    change = true;
                }

                if ( change )
                    switch(type.toLowerCase()) {
                        case "start" : {
                            startHour = hourOfDay;
                            startMin = minute;
                            setStartTimeDialogBtnText(getTimeString(startHour, startMin));
                            break;
                        }
                        case "end" : {
                            endHour = hourOfDay;
                            endMin = minute;
                            setEndTimeDialogBtnText(getTimeString(endHour, endMin));
                            break;
                        }
                    }

                if (startHour *60 + startMin > endHour * 60 + endMin) {
                    endHour = startHour;
                    endMin = startMin;
                    setEndTimeDialogBtnText(getTimeString(endHour, endMin));
                }
            }
        }, startHour, startMin , true);

        switch ( type.toLowerCase() ) {
            case "start" : {
                dialog.setMessage("시작 시간설정");
                break;
            }
            case "end" : {
                dialog.setMessage("종료 시간설정");
                break;
            }
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
    
    //현재 날짜와 시간을 얻음
    private void getDateTime() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        day = cal.get(cal.DATE);

        startHour = cal.get (cal.HOUR_OF_DAY) ;
        startMin = cal.get (cal.MINUTE);

        endHour = startHour;
        endMin = startMin;

    }

    private void showPeriodLayout() {
        temporaryScheduleLayout.setVisibility(LinearLayout.GONE);
        periodScheduleLayout.setVisibility(LinearLayout.VISIBLE);
        showTemporaryLayoutBtn.setBackground(getDrawable(R.drawable.button_unselected));
        showPeriodLayoutBtn.setBackground(getDrawable(R.drawable.button_selected));
    }

    private void showTemporaryLayout() {
        temporaryScheduleLayout.setVisibility(LinearLayout.VISIBLE);
        periodScheduleLayout.setVisibility(LinearLayout.GONE);
        showTemporaryLayoutBtn.setBackground(getDrawable(R.drawable.button_selected));
        showPeriodLayoutBtn.setBackground(getDrawable(R.drawable.button_unselected));
    }
    
    //Input을 체크
    private void checkInput() {

    }
    
    //스케줄을 DB에 저장
    private void addSchedule() {

    }

    private void endActivity() {
        
    }
    private String getDateString() {
        return String.format("%d  /  %d  / %d",year, month + 1, day); //ex) 2020  /  12  /  5
    }

    private String getTimeString(int hour, int min) {
        return String.format("%d  :  %d",hour, min); // ex) 12  :  5
    }

    private void setDateDialogBtnText(String text) {
        showDateDialogBtn.setText(text);
    }

    private void setStartTimeDialogBtnText(String text) {
        showStartTimeDialogBtn.setText(text);
    }

    private void setEndTimeDialogBtnText(String text) {
        showEndTimeDialogBtn.setText(text);
    }
}
