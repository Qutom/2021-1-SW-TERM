package com.example.pnuwalker.controlschedule;

public class PeriodScheduleData extends ScheduleData {

    public boolean[] periodDayofWeek = new boolean[7];

    public PeriodScheduleData(String name, String desc, boolean[] periodDayofWeek, int startHour, int startMin,
                              int endHour, int endMin, String destName, double destLat, double destLon, String room) {
        super(name, desc, startHour, startMin, endHour, endMin, destName, destLat, destLon, room);
        for ( int i = 0; i < 7; i++ )
            this.periodDayofWeek[i] = periodDayofWeek[i];

        isTemporalSchedule = false;
    }

    public boolean[] getPeriodDayofWeek() { return periodDayofWeek; }
}
