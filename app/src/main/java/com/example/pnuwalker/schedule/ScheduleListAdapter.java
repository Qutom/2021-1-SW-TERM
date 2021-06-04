package com.example.pnuwalker.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.controlschedule.ControlSchedule;
import com.example.pnuwalker.controlschedule.PeriodScheduleData;

import java.util.List;

public class ScheduleListAdapter extends BaseAdapter {

    private Context context;
    private List<Data> dataList;
    private Fragment parent;
    private Activity activity;

    public ScheduleListAdapter(Context context, List<Data> dataList, Fragment parent, Activity activity) {
        this.context = context;
        this.dataList = dataList;
        this.parent = parent;
        this.activity = activity;
    }

    @Override
    public int getCount() { return dataList.size(); }

    @Override
    public Object getItem(int i) { return dataList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.schedule, null);
        TextView courseKind = (TextView) v.findViewById(R.id.courseKind);
        TextView courseID = (TextView) v.findViewById(R.id.courseID);
        TextView courseName = (TextView) v.findViewById(R.id.courseName);
        TextView courseDayofTheWeek = (TextView) v.findViewById(R.id.courseDayofTheWeek);
        TextView courseTime = (TextView) v.findViewById(R.id.courseTime);
        TextView courseTime3 = (TextView) v.findViewById(R.id.courseTime3);

        if (dataList.get(i).getcourseKind() == 1) {
            courseKind.setText("정기");
        }
        else if (dataList.get(i).getcourseKind() == 0) {
            courseKind.setText("임시");
        }

        courseID.setText(dataList.get(i).getCourseID() + ".");
        courseName.setText(dataList.get(i).getcourseName());

        switch(dataList.get(i).getcourseDayofTheWeek()) {
            case 0 : courseDayofTheWeek.setText("월요일"); break;
            case 1 : courseDayofTheWeek.setText("화요일"); break;
            case 2 : courseDayofTheWeek.setText("수요일"); break;
            case 3 : courseDayofTheWeek.setText("목요일"); break;
            case 4 : courseDayofTheWeek.setText("금요일"); break;
            case 5 : courseDayofTheWeek.setText("토요일"); break;
            case 6 : courseDayofTheWeek.setText("일요일"); break;
        }

        courseTime.setText(dataList.get(i).getcourseTime());
        courseTime3.setText(dataList.get(i).getcourseTime3());

        v.setTag(dataList.get(i).getCourseID());

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlSchedule controlSchedule = new ControlSchedule(MainActivity.helper, context);

                if (dataList.get(i).getcourseKind() == 1) {
                    controlSchedule.removePeriodSchedule(dataList.get(i).getcourseDayofTheWeek(), dataList.get(i).getcourseTime());
                }
                else if (dataList.get(i).getcourseKind() == 0) {
                    controlSchedule.removeTemporalSchedule(dataList.get(i).getcourseDate(), dataList.get(i).getcourseDayofTheWeek(), dataList.get(i).getcourseTime());
                }


            }
        });

        return v;
    }
}
