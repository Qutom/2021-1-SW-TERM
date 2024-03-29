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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    private FragmentTransaction ft;
    Button deleteButton;

    public ScheduleListAdapter(Context context, List<Data> dataList, Fragment parent, Activity activity, FragmentTransaction ft) {
        this.context = context;
        this.dataList = dataList;
        this.parent = parent;
        this.activity = activity;
        this.ft = ft;
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
        LinearLayout courseUp = (LinearLayout) v.findViewById(R.id.courseUp);
        if ((dataList.get(i).getcourseKind() < 0) || (dataList.get(i).getcourseKind() == 1)) {
            courseKind.setText("정기");
            courseUp.setBackgroundColor(context.getResources().getColor(R.color.common_gray));
        }
        else if ((dataList.get(i).getcourseKind() >= 0) && (dataList.get(i).getcourseKind() != 1)) {
            courseKind.setText("임시 : " + dataList.get(i).getcourseDate() + " / ");
            courseUp.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
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

        deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ControlSchedule controlSchedule = new ControlSchedule(MainActivity.helper, activity);

                System.out.println("SourceKind " + dataList.get(i).getcourseKind());

                if (dataList.get(i).getcourseKind() == 1) {
                    controlSchedule.removePeriodSchedule(dataList.get(i).getcourseDayofTheWeek(), dataList.get(i).getcourseTime());
                }
                else if (dataList.get(i).getcourseKind() != 1) {
                    controlSchedule.removeTemporalSchedule(dataList.get(i).getcourseDate(), dataList.get(i).getcourseDayofTheWeek(), dataList.get(i).getcourseTime());
                }

                ft.detach(parent).attach(parent).commit();

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                dialog = builder.setMessage("강의가 삭제되었습니다").setPositiveButton("확인", null).create();
                dialog.show();
            }
        });

        return v;
    }

    public void setDeleteButtonClickListener(View.OnClickListener listener) {
        if ( deleteButton != null)
            deleteButton.setOnClickListener(listener);
    }
}
