package com.example.pnuwalker.controlschedule;

import com.example.pnuwalker.pathfind.Coordinate;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

import java.util.ArrayList;

public class DaySchedule {
    private long id;

    private int year;
    private int month;
    private int day;
    private int dayOfWeek;

    private String name;
    private String desc;

    private int startHour; //일정 시작 시
    private int startMin; //일정 시작 분
    private int endHour; //일정 종료 시
    private int endMin; //일정 종료 분

    private int cyclic; //cyclic 정보

    private String destName;
    private double destLat;
    private double destLon;

    private TMapPolyLine polyLine;

    public DaySchedule(long id, String dateStr, int dayOfWeek, String startTimeStr, String endTimeStr
                        , String startCoord, String destPosStr, String destName, String name, String desc,
                       int cyclic, String polylineLon, String polylineLat) {
        int[] startTime = strTimeToIntArray(startTimeStr);
        int[] endTime = strTimeToIntArray(endTimeStr);
        int[] date = strDateToIntArray(dateStr);
        double[] destPos = strPosToDoubleArray(destPosStr);

        startHour = startTime[0];
        startMin = startTime[1];
        endHour = endTime[0];
        endMin = endTime[1];

        destLat = destPos[0];
        destLon = destPos[1];

        year = date[0];
        month = date[1];
        day = date[2];

        this.dayOfWeek = dayOfWeek;
        this.destName = destName;
        this.id = id;
        this.name = name;
        this.desc = desc;
        polyLine = strToPolyline(polylineLon, polylineLat);
        this.cyclic = cyclic;
    }

    private int[] strDateToIntArray(String dateStr) {
        int[] result = new int[3];

        if (dateStr.equals("cyclic")) {
            result[0] = 9999;
            result[1] = 99;
            result[2] = 99;
        } else {
            String[] temp = dateStr.split("_");
            result[0] = Integer.parseInt(temp[0]);
            result[1] = Integer.parseInt(temp[1]);
            result[2] = Integer.parseInt(temp[2]);
        }
        return result;
    }

    private int[] strTimeToIntArray(String timeStr) { //"hh_mm" 을 {Hour, Minute} 로 변환
        String[] temp = timeStr.split("_");
        int[] result = new int[2];

        result[0] = Integer.parseInt(temp[0]);
        result[1] = Integer.parseInt(temp[1]);

        return result;
    }

    private double[] strPosToDoubleArray(String posStr) { //"lat,lon" 을 {lat, lon} 로 변환
        String[] temp = posStr.split(",");
        double[] result = new double[2];

        result[0] = Double.parseDouble(temp[0]);
        result[1] = Double.parseDouble(temp[1]);

        return result;
    }

    public TMapPolyLine strToPolyline(String lonStr, String latStr) {
        if (lonStr.equals("start"))
            return null;

        TMapPolyLine line = new TMapPolyLine();
        String[] lon = lonStr.split(",");
        String[] lat = latStr.split(",");
        for (int i = 0; i < lon.length; i++)
            line.addLinePoint(new TMapPoint(Double.parseDouble(lat[i]), Double.parseDouble(lon[i])));

        return line;
    }

    public double getDestLat() { return destLat; }
    public double getDestLon() { return destLon; }
    public int getCyclic() { return cyclic; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getDayOfWeek() { return dayOfWeek; }
    public int getEndHour() { return endHour; }
    public int getEndMin() { return endMin; }
    public int getStartHour() { return startHour; }
    public int getStartMin() { return startMin; }
    public long getId() { return id; }
    public String getDesc() { return desc; }
    public String getDestName() { return destName; }
    public String getName() { return name; }
    public TMapPolyLine getPolyLine() { return polyLine; }

    public String toString() {
        return String.format("%d | [%s] %d/%d/%d %d:%d ~ %d:%d  Cyclic : %d", id, name, year, month, day,
                startHour, startMin, endHour, endMin , cyclic);
    }

}
