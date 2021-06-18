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
import com.example.pnuwalker.schedule.Course;

import java.util.List;

public class CourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private Activity activity;

    public CourseListAdapter(Context context, List<Course> courseList, Fragment parent, Activity activity) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
        this.activity = activity;
    }

    @Override
    public int getCount() { return courseList.size(); }

    @Override
    public Object getItem(int i) { return courseList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.course, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseCredit = (TextView) v.findViewById(R.id.courseCredit);
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);
        TextView coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
        TextView courseTime = (TextView) v.findViewById(R.id.courseTime);

        courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");

        courseTitle.setText(courseList.get(i).getCourseTitle());
        courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");
        courseDivide.setText(courseList.get(i).getCourseDivide() + "분반");

        if(courseList.get(i).getCoursePersonnel() == "") {
            coursePersonnel.setText("인원 제한 없음");
        }
        else {
            coursePersonnel.setText("제한 인원 : " + courseList.get(i).getCoursePersonnel() + "명");
        }

        courseProfessor.setText(courseList.get(i).getCourseProfessor() + " 교수님");
        courseTime.setText(courseList.get(i).getCourseTime());

        v.setTag(courseList.get(i).getCourseID());

        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                ContentValues values2 = new ContentValues();
                StringBuffer courseString = new StringBuffer(courseList.get(i).getCourseTime());
                String courseTime = "";
                String courseStartTime = "";
                String courseLocation = "";
                String courseTime2 = "";
                String courseStartTime2 = "";
                Cursor cursor;

                int dayweek = 6;

                values.put("date", "");
                values2.put("date", "");
                if (courseList.get(i).getCourseTime().indexOf("(") > -1) {
                    if (courseString.indexOf("월") > -1) {
                        if (dayweek == 6) {
                            dayweek = 0;
                        }
                        else {
                            dayweek = dayweek + 10;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("월") > -1) {
                        if (dayweek == 6) {
                            dayweek = 0;
                        }
                        else {
                            dayweek = dayweek + 10;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("화") > -1) {
                        if (dayweek == 6) {
                            dayweek = 1;
                        }
                        else {
                            dayweek = dayweek + 20;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("화") > -1) {
                        if (dayweek == 6) {
                            dayweek = 1;
                        }
                        else {
                            dayweek = dayweek + 20;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("수") > -1) {
                        if (dayweek == 6) {
                            dayweek = 2;
                        }
                        else {
                            dayweek = dayweek + 30;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("수") > -1) {
                        if (dayweek == 6) {
                            dayweek = 2;
                        }
                        else {
                            dayweek = dayweek + 30;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("목") > -1) {
                        if (dayweek == 6) {
                            dayweek = 3;
                        }
                        else {
                            dayweek = dayweek + 40;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("목") > -1) {
                        if (dayweek == 6) {
                            dayweek = 3;
                        }
                        else {
                            dayweek = dayweek + 40;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("금") > -1) {
                        if (dayweek == 6) {
                            dayweek = 4;
                        }
                        else {
                            dayweek = dayweek + 50;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("금") > -1) {
                        if (dayweek == 6) {
                            dayweek = 4;
                        }
                        else {
                            dayweek = dayweek + 50;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(courseStartTime.substring(courseStartTime.indexOf("_")+1)) + Integer.parseInt(courseTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a) < 10) {
                                courseTime = "0" + Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                courseTime = Integer.toString(Integer.parseInt(courseStartTime.substring(0, courseStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                        }
                        else {
                            String dummyStartTime = "";
                            String dummyTime = "";

                            dummyStartTime = dummyStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            dummyStartTime = dummyStartTime + "_";
                            courseString.delete(0, 1);
                            dummyStartTime = dummyStartTime + courseString.substring(0, courseString.indexOf("("));
                            courseString.delete(0, courseString.indexOf("(") + 1);
                            dummyTime = dummyTime + courseString.substring(0, courseString.indexOf(")"));
                            courseString.delete(0, courseString.indexOf(" ") + 1);

                            int a = 0;
                            int b = Integer.parseInt(dummyStartTime.substring(dummyStartTime.indexOf("_")+1)) + Integer.parseInt(dummyTime);

                            while (b > 60) {
                                b = b - 60;
                                a = a + 1;
                            }

                            if((Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a) < 10) {
                                dummyTime = "0" + Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }
                            else {
                                dummyTime = Integer.toString(Integer.parseInt(dummyStartTime.substring(0, dummyStartTime.indexOf("_"))) + a)
                                        + "_"
                                        + Integer.toString(b);
                            }

                            courseStartTime2 = courseStartTime2 + dummyStartTime;
                            courseTime2 = courseTime2 + dummyTime;
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }
                }
                else if (courseList.get(i).getCourseTime().indexOf("-") > -1) {
                    if (courseString.indexOf("월") > -1) {
                        if (dayweek == 6) {
                            dayweek = 0;
                        }
                        else {
                            dayweek = dayweek + 10;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("월") > -1) {
                        if (dayweek == 6) {
                            dayweek = 0;
                        }
                        else {
                            dayweek = dayweek + 10;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("화") > -1) {
                        if (dayweek == 6) {
                            dayweek = 1;
                        }
                        else {
                            dayweek = dayweek + 20;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("화") > -1) {
                        if (dayweek == 6) {
                            dayweek = 1;
                        }
                        else {
                            dayweek = dayweek + 20;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }


                    if (courseString.indexOf("수") > -1) {
                        if (dayweek == 6) {
                            dayweek = 2;
                        }
                        else {
                            dayweek = dayweek + 30;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("수") > -1) {
                        if (dayweek == 6) {
                            dayweek = 2;
                        }
                        else {
                            dayweek = dayweek + 30;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("목") > -1) {
                        if (dayweek == 6) {
                            dayweek = 3;
                        }
                        else {
                            dayweek = dayweek + 40;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("목") > -1) {
                        if (dayweek == 6) {
                            dayweek = 3;
                        }
                        else {
                            dayweek = dayweek + 40;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("금") > -1) {
                        if (dayweek == 6) {
                            dayweek = 4;
                        }
                        else {
                            dayweek = dayweek + 50;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }

                    if (courseString.indexOf("금") > -1) {
                        if (dayweek == 6) {
                            dayweek = 4;
                        }
                        else {
                            dayweek = dayweek + 50;
                        }
                        courseString.delete(0, 2);
                        if (courseTime.length() == 0) {
                            courseStartTime = courseStartTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime = courseStartTime + "_";
                            courseString.delete(0, 1);
                            courseStartTime = courseStartTime + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime = courseTime + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime = courseTime + "_";
                            courseString.delete(0, 1);
                            courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        else {
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseStartTime2 = courseStartTime2 + "_";
                            courseString.delete(0, 1);
                            courseStartTime2 = courseStartTime2 + courseString.substring(0, courseString.indexOf("-"));
                            courseString.delete(0, courseString.indexOf("-") + 1);
                            courseTime2 = courseTime2 + courseString.substring(0, 2);
                            courseString.delete(0, 2);
                            courseTime2 = courseTime2 + "_";
                            courseString.delete(0, 1);
                            courseTime2 = courseTime2 + courseString.substring(0, courseString.indexOf(" "));
                            courseString.delete(0, courseString.indexOf(" ") + 1);
                        }
                        if (courseString.length() < 12) {
                            courseLocation = courseString.substring(0, courseString.length());
                        }
                        else {
                            courseLocation = courseString.substring(0, courseString.indexOf(","));
                            courseString.delete(0, courseString.indexOf(",")+1);
                        }
                    }
                }

                String courseLocationRoom = "";
                String courseLocationName = "";
                String tp_x = "";
                String tp_y = "";

                courseLocationRoom = courseLocation.substring(courseString.indexOf("-")+1);
                courseLocation = courseLocation.substring(0, courseString.indexOf("-"));

                Resources res = view.getResources();

                String[] pnu = res.getStringArray(R.array.pnu_buildings);

                if ((courseLocation.indexOf("양산") > -1) || (courseLocation.indexOf("Y") > -1)) {
                    courseLocationName = courseLocation;
                    tp_x = "0";
                    tp_y = "0";
                }
                else {
                    for (int i = 0; i < pnu.length; i++) {
                        if (pnu[i].substring(0, 3).equals(courseLocation)) {
                            pnu[i] = pnu[i].substring(pnu[i].indexOf(":")+1);
                            courseLocationName = pnu[i].substring(0, pnu[i].indexOf(":"));

                            pnu[i] = pnu[i].substring(pnu[i].indexOf(":")+1);
                            tp_x = pnu[i].substring(0, pnu[i].indexOf(":"));

                            pnu[i] = pnu[i].substring(pnu[i].indexOf(":")+1);
                            tp_y = pnu[i].substring(0, pnu[i].indexOf(":"));
                        }
                    }
                }

                courseLocation = tp_x + "," + tp_y;

                boolean[] periodDayofWeek = {false, false, false, false, false, false, false};

                if ((courseStartTime2.equals("")) || (courseTime2.equals(""))) {
                    if (dayweek == 0) {
                        periodDayofWeek[0] = true;
                    }
                    else if (dayweek == 1) {
                        periodDayofWeek[1] = true;
                    }
                    else if (dayweek == 2) {
                        periodDayofWeek[2] = true;
                    }
                    else if (dayweek == 3) {
                        periodDayofWeek[3] = true;
                    }
                    else if (dayweek == 4) {
                        periodDayofWeek[4] = true;
                    }

                    System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
                    System.out.println(courseStartTime + " " + courseTime);
                    System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");

                    ControlSchedule add = new ControlSchedule(MainActivity.helper, activity,
                            new PeriodScheduleData(courseList.get(i).getCourseTitle(),
                                    "등록된 강의 : " + courseList.get(i).getCourseTitle(),
                                    periodDayofWeek,
                                    Integer.parseInt(courseStartTime.substring(0, 2)),
                                    Integer.parseInt(courseStartTime.substring(3)),
                                    Integer.parseInt(courseTime.substring(0, 2)),
                                    Integer.parseInt(courseTime.substring(3)),
                                    courseLocationName,
                                    Double.parseDouble(tp_x),
                                    Double.parseDouble(tp_y),
                                    courseLocationRoom));

                    if (add.checkSchedule()) {
                        add.addSchedule();
                    } else {
                        return;
                    }
                }
                else {
                    if ((dayweek % 10) == 0) {
                        periodDayofWeek[0] = true;
                    }
                    else if ((dayweek % 10) == 1) {
                        periodDayofWeek[1] = true;
                    }
                    else if ((dayweek % 10) == 2) {
                        periodDayofWeek[2] = true;
                    }
                    else if ((dayweek % 10) == 3) {
                        periodDayofWeek[3] = true;
                    }
                    else if ((dayweek % 10) == 4) {
                        periodDayofWeek[4] = true;
                    }

                    System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
                    System.out.println(courseStartTime + " " + courseTime);
                    System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");

                    ControlSchedule add = new ControlSchedule(MainActivity.helper, activity,
                            new PeriodScheduleData(courseList.get(i).getCourseTitle(),
                                    "등록된 강의 : " + courseList.get(i).getCourseTitle(),
                                    periodDayofWeek,
                                    Integer.parseInt(courseStartTime.substring(0, 2)),
                                    Integer.parseInt(courseStartTime.substring(3)),
                                    Integer.parseInt(courseTime.substring(0, 2)),
                                    Integer.parseInt(courseTime.substring(3)),
                                    courseLocationName,
                                    Double.parseDouble(tp_x),
                                    Double.parseDouble(tp_y),
                                    courseLocationRoom));

                    periodDayofWeek[0] = false;
                    periodDayofWeek[1] = false;
                    periodDayofWeek[2] = false;
                    periodDayofWeek[3] = false;
                    periodDayofWeek[4] = false;
                    periodDayofWeek[5] = false;
                    periodDayofWeek[6] = false;

                    if (((dayweek / 10) - 1) == 0) {
                        periodDayofWeek[0] = true;
                    }
                    else if (((dayweek / 10) - 1) == 1) {
                        periodDayofWeek[1] = true;
                    }
                    else if (((dayweek / 10) - 1) == 2) {
                        periodDayofWeek[2] = true;
                    }
                    else if (((dayweek / 10) - 1) == 3) {
                        periodDayofWeek[3] = true;
                    }
                    else if (((dayweek / 10) - 1) == 4) {
                        periodDayofWeek[4] = true;
                    }

                    ControlSchedule add2 = new ControlSchedule(MainActivity.helper, activity,
                            new PeriodScheduleData(courseList.get(i).getCourseTitle(),
                                    "등록된 강의 : " + courseList.get(i).getCourseTitle(),
                                    periodDayofWeek,
                                    Integer.parseInt(courseStartTime2.substring(0, 2)),
                                    Integer.parseInt(courseStartTime2.substring(3)),
                                    Integer.parseInt(courseTime2.substring(0, 2)),
                                    Integer.parseInt(courseTime2.substring(3)),
                                    courseLocationName,
                                    Double.parseDouble(tp_x),
                                    Double.parseDouble(tp_y),
                                    courseLocationRoom));

                    if (add.checkSchedule() && add2.checkSchedule()) {
                        add.addSchedule();
                        add2.addSchedule();
                    } else {
                        return;
                    }
                }

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());

                if ((courseLocation.indexOf("양산") > -1) || (courseLocation.indexOf("Y") > -1)) {
                    dialog = builder.setMessage("양산캠퍼스의 경우 세부 일정 및 길찾기 기능이 지원되지 않음을 유의해주세요.\n\n강의가 추가되었습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                }
                else {
                    dialog = builder.setMessage("강의가 추가되었습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                }

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");

                cursor = MainActivity.db.query("schedule1", null, null, null, null, null, null, null);

                for (int a = 0; a < cursor.getCount(); a++) {
                    if (a == 0) {
                        cursor.moveToFirst();
                    }
                    else {
                        cursor.moveToNext();
                    }
                    System.out.println(" 0. ID "
                            + cursor.getInt(0)
                            + " 1.date "
                            + cursor.getString(1)
                            + " 2.day_week "
                            + cursor.getInt(2)
                            + " 3.start_time "
                            + cursor.getString(3)
                            + " 4.end_time "
                            + cursor.getString(4)
                            + " 5.start_location "
                            + cursor.getString(5)
                            + " 6.end_location "
                            + cursor.getString(6)
                            + " 7.end_location_name "
                            + cursor.getString(7)
                            + " 8.name "
                            + cursor.getString(8)
                            + " 9.script "
                            + cursor.getString(9)
                            + " 10.cyclic "
                            + cursor.getInt(10)
                            + " 11.tpolyline_x "
                            + cursor.getString(11)
                            + " 12.tpolyline_y "
                            + cursor.getString(12)
                            + " 13.room "
                            + cursor.getString(13));
                }
            }
        });
        return v;

    }

}
