package com.example.pnuwalker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {

    Button showDateDialogBtn;
    Button showStartTimeDialogBtn;
    Button showEndTimeDialogBtn;

    int year;
    int month;
    int day;
    int startHour;
    int startMin;
    int endHour;
    int endMin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        showDateDialogBtn = (Button)findViewById(R.id.show_date_dialog_btn);

        showDateDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        //현재 시간을 기본으로 사용함
        getDateTime();
        setDateDialogBtnText(getDateString());

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
                System.out.println(getDateString());
                System.out.println("Mo");
            }
        }, year ,month , day);

        dialog.setMessage("날짜 선택");
        dialog.show();
    }

    private void showTimeLog(String type) {

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
    
    private String getDateString() {
        return String.format("%d  /  %d  / %d",year, month, day); //ex) 2020  /  12  /  5
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
