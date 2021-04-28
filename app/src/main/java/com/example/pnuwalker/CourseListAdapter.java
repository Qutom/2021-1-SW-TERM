package com.example.pnuwalker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class CourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    private Fragment parent;

    public CourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
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
                StringBuffer courseString = new StringBuffer(courseList.get(i).getCourseTime());
                String courseTime = "";
                String courseLocation = "";
                Cursor cursor;

                int dayweek = 0;

                values.put("date", "");
                if (courseList.get(i).getCourseTime().indexOf("월") > -1) {
                    if (dayweek == 0) {
                        dayweek = 1;
                    }
                    else {
                        dayweek = dayweek + 10;
                    }
                    courseString.delete(0, 2);
                    if (courseTime.length() == 0) {
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    else {
                        courseTime = courseTime + ",";
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    if (courseString.length() < 10) {
                        courseLocation = courseString.substring(0, courseString.length());
                    }
                    else {
                        courseLocation = courseString.substring(0, courseString.indexOf(","));
                        courseString.delete(0, courseString.indexOf(",")+1);
                    }
                }

                if (courseList.get(i).getCourseTime().indexOf("화") > -1) {
                    if (dayweek == 0) {
                        dayweek = 2;
                    }
                    else {
                        dayweek = dayweek + 20;
                    }
                    courseString.delete(0, 2);
                    if (courseTime.length() == 0) {
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    else {
                        courseTime = courseTime + ",";
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    if (courseString.length() < 10) {
                        courseLocation = courseString.substring(0, courseString.length());
                    }
                    else {
                        courseLocation = courseString.substring(0, courseString.indexOf(","));
                        courseString.delete(0, courseString.indexOf(",")+1);
                    }
                }


                if (courseList.get(i).getCourseTime().indexOf("수") > -1) {
                    if (dayweek == 0) {
                        dayweek = 3;
                    }
                    else {
                        dayweek = dayweek + 30;
                    }
                    courseString.delete(0, 2);
                    if (courseTime.length() == 0) {
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    else {
                        courseTime = courseTime + ",";
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    if (courseString.length() < 10) {
                        courseLocation = courseString.substring(0, courseString.length());
                    }
                    else {
                        courseLocation = courseString.substring(0, courseString.indexOf(","));
                        courseString.delete(0, courseString.indexOf(",")+1);
                    }
                }


                if (courseList.get(i).getCourseTime().indexOf("목") > -1) {
                    if (dayweek == 0) {
                        dayweek = 4;
                    }
                    else {
                        dayweek = dayweek + 40;
                    }
                    courseString.delete(0, 2);
                    if (courseTime.length() == 0) {
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    else {
                        courseTime = courseTime + ",";
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    if (courseString.length() < 10) {
                        courseLocation = courseString.substring(0, courseString.length());
                    }
                    else {
                        courseLocation = courseString.substring(0, courseString.indexOf(","));
                        courseString.delete(0, courseString.indexOf(",")+1);
                    }
                }


                if (courseList.get(i).getCourseTime().indexOf("금") > -1) {
                    if (dayweek == 0) {
                        dayweek = 5;
                    }
                    else {
                        dayweek = dayweek + 50;
                    }
                    courseString.delete(0, 2);
                    if (courseTime.length() == 0) {
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    else {
                        courseTime = courseTime + ",";
                        courseTime = courseTime + courseString.substring(0, 2);
                        courseString.delete(0, 2);
                        courseTime = courseTime + "_";
                        courseString.delete(0, 1);
                        courseTime = courseTime + courseString.substring(0, courseString.indexOf(" "));
                        courseString.delete(0, courseString.indexOf(" ") + 1);
                    }
                    if (courseString.length() < 10) {
                        courseLocation = courseString.substring(0, courseString.length());
                    }
                    else {
                        courseLocation = courseString.substring(0, courseString.indexOf(","));
                        courseString.delete(0, courseString.indexOf(",")+1);
                    }
                }

                values.put("day_week", dayweek);
                values.put("start_time", courseTime);
                values.put("end_time", courseTime);
                values.put("start_location", "");
                values.put("end_location", courseLocation);
                values.put("end_location_name", courseLocation);
                values.put("name", courseList.get(i).getCourseTitle());
                values.put("script", "등록된 강의 : " + courseList.get(i).getCourseTitle());
                values.put("cyclic", 1);
                values.put("tpolyline_x", "NULL");
                values.put("tpolyline_y", "NULL");

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                dialog = builder.setMessage("강의가 추가되었습니다.").setPositiveButton("확인", null).create();
                dialog.show();

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");

                MainActivity.db.insert("schedule1", null, values);

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
                            + " 2.start_time "
                            + cursor.getString(3)
                            + " 3.end_time "
                            + cursor.getString(4)
                            + " 4.start_location "
                            + cursor.getString(5)
                            + " 5.end_location "
                            + cursor.getString(6)
                            + " 6.end_location_name "
                            + cursor.getString(7)
                            + " 7.name "
                            + cursor.getString(8)
                            + " 8.script "
                            + cursor.getString(9)
                            + " 9.cyclic "
                            + cursor.getInt(10)
                            + " 10.tpolyline_x "
                            + cursor.getString(11)
                            + " 11.tpolyline_y "
                            + cursor.getString(12));
                }
            }
        });
        return v;

    }

}
