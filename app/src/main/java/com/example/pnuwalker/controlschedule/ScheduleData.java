package com.example.pnuwalker.controlschedule;

public class ScheduleData {
    public String name;
    public String desc;

    public int startHour; //일정 시작 시
    public int startMin; //일정 시작 분
    public int endHour; //일정 종료 시
    public int endMin; //일정 종료 분

    public boolean isTemporalSchedule; //임시 일정인가?

    public String destName;
    public double destLat;
    public double destLon;
    public String room;

    public ScheduleData(String name, String desc, int startHour , int startMin, int endHour, int endMin, String destName,
                        double destLat, double destLon, String room) {
        this.name = name;
        this.desc = desc;

        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;

        this.destName = destName;
        this.destLat = destLat;
        this.destLon = destLon;
        this.room = room;
    }

    public int[] getTimeInfo() { return new int[]{startHour, startMin, endHour , endMin}; }
    public String getScheduleName() { return name; }
    public String getScheduleDesc() { return desc; }
    public String getDestName() { return destName; }
    public double[] getDestCoordinate() { return new double[]{destLat, destLon}; }
    public boolean isTemporalSchedule() { return isTemporalSchedule; }

}
