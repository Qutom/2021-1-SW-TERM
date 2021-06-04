package com.example.pnuwalker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnuwalker.controlschedule.ControlSchedule;
import com.example.pnuwalker.controlschedule.PeriodScheduleData;
import com.example.pnuwalker.controlschedule.TemporalScheduleData;

import java.util.Calendar;

//AddScheduleActivity를 호출하기 위해서, 반드시 Intent를 통해  endPoint정보를 보내야함
//AddSchedule이 성공적으로 종료되었을때, 일정 이름, 일정 설명, 주기 일정여부(boolean), 시간, EndPoint 을 종료와 함께 보냄
public class AddScheduleActivity extends AppCompatActivity {

    final static int MON = 0;
    final static int TUE = 1;
    final static int WEN = 2;
    final static int THR = 3;
    final static int FRI = 4;
    final static int SAT = 5;
    final static int SUN = 6;

    Button showDateDialogBtn;
    Button showStartTimeDialogBtn;
    Button showEndTimeDialogBtn;
    Button showPeriodLayoutBtn;
    Button showTemporaryLayoutBtn;
    Button cancelBtn;
    Button doneBtn;
    Button periodDayOfWeekBtnArr[] = new Button[7];

    LinearLayout periodScheduleLayout;
    LinearLayout temporaryScheduleLayout;
    LinearLayout setTimeLayout;
    RelativeLayout relativeLayout;

    EditText scheduleNameEditText;
    EditText scheduleDescEditText;
    EditText scheduleRoomEditText;

    TextView destNameText;

    boolean periodDayofWeek[] = {false, false, false, false, false, false, false}; // 주기 일정에서 해당 요일이 사용되는가?
    int year;
    int month;
    int day;
    int temporalDayofWeek;

    int startHour;
    int startMin;
    int endHour;
    int endMin;


    boolean isTemporalSchedule = false;

    String destName;
    double destLat;
    double destLon;

    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        showDateDialogBtn = (Button)findViewById(R.id.show_date_dialog_btn);
        showStartTimeDialogBtn = (Button)findViewById(R.id.show_start_time_dialog_btn);
        showEndTimeDialogBtn = (Button)findViewById(R.id.show_end_time_dialog_btn);
        showPeriodLayoutBtn = (Button)findViewById(R.id.show_period_layout_btn);
        showTemporaryLayoutBtn = (Button)findViewById(R.id.show_temporary_layout_btn);
        periodDayOfWeekBtnArr[MON] = (Button)findViewById(R.id.period_day_of_week_mon_btn);
        periodDayOfWeekBtnArr[TUE] = (Button)findViewById(R.id.period_day_of_week_tue_btn);
        periodDayOfWeekBtnArr[WEN] = (Button)findViewById(R.id.period_day_of_week_wen_btn);
        periodDayOfWeekBtnArr[THR] = (Button)findViewById(R.id.period_day_of_week_thr_btn);
        periodDayOfWeekBtnArr[FRI] = (Button)findViewById(R.id.period_day_of_week_fri_btn);
        periodDayOfWeekBtnArr[SAT] = (Button)findViewById(R.id.period_day_of_week_sat_btn);
        periodDayOfWeekBtnArr[SUN] = (Button)findViewById(R.id.period_day_of_week_sun_btn);

        scheduleNameEditText = (EditText)findViewById(R.id.schedule_name_edittext);
        scheduleDescEditText = (EditText)findViewById(R.id.schedule_description_edittext);
        scheduleRoomEditText = (EditText)findViewById(R.id.schedule_room_edittext);

        cancelBtn = (Button)findViewById(R.id.cancel_btn);
        doneBtn = (Button)findViewById(R.id.done_btn);


        destNameText = (TextView)findViewById(R.id.dest_name_text);
        temporaryScheduleLayout = (LinearLayout)findViewById(R.id.temporary_schedule_layout);
        periodScheduleLayout = (LinearLayout)findViewById(R.id.period_schedule_layout);
        setTimeLayout = (LinearLayout)findViewById(R.id.set_time_layout);
        relativeLayout = (RelativeLayout)findViewById(R.id.root_layout);

        showDateDialogBtn.setOnClickListener((v -> {showDateDialog();}));
        showStartTimeDialogBtn.setOnClickListener(( v -> {showTimeDialog("start");}));
        showEndTimeDialogBtn.setOnClickListener(( v -> {showTimeDialog("end");}));
        showPeriodLayoutBtn.setOnClickListener((v -> {showPeriodLayout();}));
        showTemporaryLayoutBtn.setOnClickListener((v -> {showTemporaryLayout();}));
        cancelBtn.setOnClickListener((v -> { finish(); }));
        doneBtn.setOnClickListener((v -> { addSchedule(); }));

        periodDayOfWeekBtnArr[MON].setOnClickListener((v-> {setPeriodDayOfWeek(MON);}));
        periodDayOfWeekBtnArr[TUE].setOnClickListener((v-> {setPeriodDayOfWeek(TUE);}));
        periodDayOfWeekBtnArr[WEN].setOnClickListener((v-> {setPeriodDayOfWeek(WEN);}));
        periodDayOfWeekBtnArr[THR].setOnClickListener((v-> {setPeriodDayOfWeek(THR);}));
        periodDayOfWeekBtnArr[FRI].setOnClickListener((v-> {setPeriodDayOfWeek(FRI);}));
        periodDayOfWeekBtnArr[SAT].setOnClickListener((v-> {setPeriodDayOfWeek(SAT);}));
        periodDayOfWeekBtnArr[SUN].setOnClickListener((v-> {setPeriodDayOfWeek(SUN);}));

        intent = getIntent();
        unpackIntent();
        setDestNameText(destName);

        //현재 시간을 기본으로 사용함
        getDateTime();
        setDateDialogBtnText(getDateString());
        setStartTimeDialogBtnText(getTimeString(startHour, startMin));
        setEndTimeDialogBtnText(getTimeString(endHour, endMin));

        showTemporaryLayout();
    }

    private void unpackIntent() {
        destName = intent.getExtras().getString("destName");
        destLat = intent.getExtras().getDouble("destLat");
        destLon = intent.getExtras().getDouble("destLon");
    }

    private void showDateDialog() {
        Calendar cal = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        int currYear = cal.get(cal.YEAR);
        int currMonth = cal.get(cal.MONTH);
        int currDay = cal.get(cal.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int _year, int _month, int _dayOfMonth) {
                year = _year;
                month = _month;
                day = _dayOfMonth;

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DATE, day);
                temporalDayofWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
                if (temporalDayofWeek == 0)
                    temporalDayofWeek = 6;

                setDateDialogBtnText(getDateString());
            }
        }, year ,month, day);

        minDate.set(currYear,currMonth,currDay);
        dialog.getDatePicker().setMinDate(minDate.getTime().getTime());

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
                int currMonth = cal.get(cal.MONTH);
                int currYear = cal.get(cal.YEAR);
                if ( currDay == day && isTemporalSchedule ) { //같은 날짜에는 현재보다 과거의 시간을 선택할 수 없음.
                    if ( cal.get(cal.HOUR_OF_DAY) * 60 + cal.get(cal.MINUTE) <= hourOfDay *60 + minute ) {
                        change = true;
                    } else { // 같은 날자에서 과거의 시간이 선택되었을시 현재 시간으로 표기함
                        change = true;
                        hourOfDay = cal.get(cal.HOUR_OF_DAY);
                        minute = cal.get(cal.MINUTE);
                    }
                } else if ( currDay < day || currMonth < month || currYear < year ) {
                    change = true;
                }

                if ( !isTemporalSchedule ) {
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
        temporalDayofWeek = cal.get(cal.DAY_OF_WEEK) - 1;

        startHour = cal.get (cal.HOUR_OF_DAY) ;
        startMin = cal.get (cal.MINUTE);

        endHour = startHour;
        endMin = startMin;

    }

    private void setPeriodDayOfWeek(int day) {
        periodDayofWeek[day] = !periodDayofWeek[day];
        if (periodDayofWeek[day]) {
            periodDayOfWeekBtnArr[day].setBackground(getDrawable(R.drawable.gray_button_selected));
        } else {
            periodDayOfWeekBtnArr[day].setBackground(getDrawable(R.drawable.gray_button_unselected));
        }
    }

    private void showPeriodLayout() {
        temporaryScheduleLayout.setVisibility(LinearLayout.GONE);
        periodScheduleLayout.setVisibility(LinearLayout.VISIBLE);
        showTemporaryLayoutBtn.setBackground(getDrawable(R.drawable.blue_button_unselected));
        showPeriodLayoutBtn.setBackground(getDrawable(R.drawable.blue_button_selected));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) setTimeLayout.getLayoutParams();
        params.addRule(RelativeLayout.BELOW ,periodScheduleLayout.getId());
        setTimeLayout.setLayoutParams(params);

        isTemporalSchedule = false;
    }

    private void showTemporaryLayout() {
        temporaryScheduleLayout.setVisibility(LinearLayout.VISIBLE);
        periodScheduleLayout.setVisibility(LinearLayout.GONE);
        showTemporaryLayoutBtn.setBackground(getDrawable(R.drawable.blue_button_selected));
        showPeriodLayoutBtn.setBackground(getDrawable(R.drawable.blue_button_unselected));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) setTimeLayout.getLayoutParams();
        params.addRule(RelativeLayout.BELOW ,temporaryScheduleLayout.getId());
        setTimeLayout.setLayoutParams(params);

        isTemporalSchedule = true;
    }


    //중복되는 것이 있는지 체크, 필수 값들이 입력되어 있는지 체크후 스케줄 추가
    private void addSchedule() {
        if ( String.valueOf(scheduleNameEditText.getText()).equals("") ) {
            Toast.makeText(this, "일정 이름을 설정해 주십시오" , Toast.LENGTH_SHORT).show();
            return;
        }

        if ( !isTemporalSchedule ) {
            int count = 0;
            for ( boolean b : periodDayofWeek )
                if (b)
                    count++;
            if ( count == 0 ) {
                Toast.makeText(this, "하나 이상의 요일을 선택해 주십시오" , Toast.LENGTH_SHORT).show();
                return;
            }
        }

        System.out.println(periodDayofWeek[0]);
        ControlSchedule add;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "schedule1.db", null, 1);
        System.out.println("TemporalDayOfWeek : " + temporalDayofWeek);
        if (isTemporalSchedule)
            add = new ControlSchedule(dataBaseHelper, this ,
                    new TemporalScheduleData(scheduleNameEditText.getText().toString(), scheduleDescEditText.getText().toString(), year, month, day,
                    temporalDayofWeek, startHour, startMin, endHour, endMin, destName, destLat, destLon, scheduleRoomEditText.getText().toString()));
        else
            add = new ControlSchedule(dataBaseHelper, this ,
                    new PeriodScheduleData(scheduleNameEditText.getText().toString(), scheduleDescEditText.getText().toString(), periodDayofWeek,
                            startHour, startMin, endHour, endMin, destName, destLat, destLon, scheduleRoomEditText.getText().toString()));

        if (add.checkSchedule()) {
            add.addSchedule();
        } else {
            return;
        }

        finish();
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
    private void setDestNameText(String text) { destNameText.setText(text); }
}
