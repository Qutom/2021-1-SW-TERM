package com.example.pnuwalker.controlschedule;

public class TemporalScheduleData extends ScheduleData {
    public int year;
    public int month;
    public int day;
    public int dayofWeek;

    public TemporalScheduleData(String name, String desc, int year, int month, int day, int dayofWeek,
                              int startHour, int startMin, int endHour, int endMin, String destName, double destLat, double destLon) {
        super(name , desc, startHour, startMin, endHour, endMin, destName, destLat, destLon);
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayofWeek = dayofWeek;

        isTemporalSchedule = true;
    }

    public int[] getDateInfo() { return new int[]{year, month, day, dayofWeek}; }
}

