package com.example.pnuwalker.schedule;

public class Data {
    private int courseID;
    private int courseKind;
    private String courseName;
    private int courseDayofTheWeek;
    private String courseTime;
    private String courseTime3;
    private String courseDate;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getcourseKind() {
        return courseKind;
    }

    public void setcourseKind(int courseKind) {
        this.courseKind = courseKind;
    }
    public String getcourseName() {
        return courseName;
    }

    public void setcourseName(String courseName) {
        this.courseName = courseName;
    }
    public int getcourseDayofTheWeek() {
        return courseDayofTheWeek;
    }

    public void setcourseDayofTheWeek(int courseDayofTheWeek) {
        this.courseDayofTheWeek = courseDayofTheWeek;
    }
    public String getcourseTime() {
        return courseTime;
    }

    public void setcourseTime(String courseTime) {
        this.courseTime = courseTime;
    }
    public String getcourseTime3() {
        return courseTime3;
    }

    public void setcourseTime3(String courseTime3) {
        this.courseTime3 = courseTime3;
    }

    public String getcourseDate() {
        return courseDate;
    }

    public void setcourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public Data(int courseID, int courseKind, String courseName, int courseDayofTheWeek, String courseTime, String courseTime3, String courseDate) {
        this.courseID = courseID;
        this.courseKind = courseKind;
        this.courseName = courseName;
        this.courseDayofTheWeek = courseDayofTheWeek;
        this.courseTime = courseTime;
        this.courseTime3 = courseTime3;
        this.courseDate = courseDate;
    }
}