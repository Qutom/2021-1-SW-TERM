package com.example.pnuwalker.controlschedule;

import android.content.ContentValues;

import com.example.pnuwalker.Pair;
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

    private long cyclic; //cyclic 정보
    private ArrayList<Long> additionalOverrideId;
    private long periodHead;

    private String destName;
    private double destLat;
    private double destLon;
    private String room;

    private TMapPolyLine polyLine;


    public DaySchedule(long id, String dateStr, int dayOfWeek, String startTimeStr, String endTimeStr
                        , String startCoord, String destPosStr, String destName, String name, String desc,
                       int cyclic, String addOverId, String polylineLon, String polylineLat, String room, Long periodHead) {
        int[] startTime = strTimeToIntArray(startTimeStr);
        int[] endTime = strTimeToIntArray(endTimeStr);
        int[] date = strDateToIntArray(dateStr);
        double[] destPos = strPosToDoubleArray(destPosStr);

        additionalOverrideId = new ArrayList<>();
        setAdditionOverrideId(addOverId);

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
        this.room = room;
        this.periodHead = periodHead;
    }

    public DaySchedule(DaySchedule s) {
        additionalOverrideId = new ArrayList<>();
        for (long v : s.getAdditionalOverrideId())
            additionalOverrideId.add(v);
        startHour = s.getStartHour();
        startMin = s.getStartMin();
        endHour = s.getEndHour();
        endMin = s.getEndMin();

        destLat = s.getDestLat();
        destLon = s.getDestLon();

        year = s.getYear();
        month = s.getMonth();
        day = s.getDay();

        this.dayOfWeek = s.getDayOfWeek();
        this.destName = s.getDestName();
        this.id = s.getId();
        this.name = s.getName();
        this.desc = s.getDesc();
        polyLine = s.getPolyLine();
        this.cyclic = s.getCyclic();
        this.room = s.getRoom();
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

    private TMapPolyLine strToPolyline(String lonStr, String latStr) {
        if (lonStr.equals("start") || lonStr.equals(""))
            return null;

        TMapPolyLine line = new TMapPolyLine();
        String[] lon = lonStr.split(",");
        String[] lat = latStr.split(",");
        for (int i = 0; i < lon.length; i++)
            line.addLinePoint(new TMapPoint(Double.parseDouble(lat[i]), Double.parseDouble(lon[i])));

        return line;
    }

    private void setAdditionOverrideId(String overrideStr) {
        System.out.println("OverrideStr :" + overrideStr);
        if (overrideStr == null || overrideStr.equals("")) { return; }
        String[] temp = overrideStr.split(",");
        if ( temp.length >= 1 )
            for (int i = 0; i < temp.length; i++)
                additionalOverrideId.add(Long.parseLong(temp[i]));
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("date", String.format("%d_%02d_%02d", year, month, day));
        values.put("day_week", dayOfWeek);
        values.put("start_time", String.format("%02d_%02d", startHour, startMin));
        values.put("end_time", String.format("%02d_%02d", endHour, endMin));
        values.put("start_location", "0,0");
        values.put("end_location_name", destName);
        values.put("end_location", String.format("%f,%f", destLat, destLon));
        values.put("name", name);
        values.put("script", desc);
        values.put("cyclic", cyclic);
        String str = "";
        for (int i = 1; i < additionalOverrideId.size(); i++) {
            str += Long.toString(additionalOverrideId.get(i));
            if ( i != additionalOverrideId.size() - 1 )
                str += ",";
        }

        values.put("additional_override_id" , str);

        String result[] = {"",""}; //lon , lat
        if (polyLine != null) {
            ArrayList<TMapPoint> points = polyLine.getLinePoint();
            for ( int i = 0; i < points.size(); i++ ) {
                TMapPoint p = points.get(i);
                result[0] += String.format("%f",p.getLongitude());
                result[1] += String.format("%f",p.getLatitude());

                if (i != points.size() - 1) {
                    result[0] += ",";
                    result[1] += ",";
                }

            }
        }
        values.put("tpolyline_x" , result[0]); //lat
        values.put("tpolyline_y" , result[1]); //lon

        values.put("room", room);
        values.put("period_head", periodHead);
        return values;
    }

    public double getDestLat() { return destLat; }
    public double getDestLon() { return destLon; }
    public long getCyclic() { return cyclic; }
    public ArrayList<Long> getAdditionalOverrideId() { return additionalOverrideId; }
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
    public String getRoom() { return room; }
    public TMapPolyLine getPolyLine() { return polyLine; }
    public TMapPoint getDestPoint() { return new TMapPoint(destLat, destLon); }
    public long getPeriodHead() { return periodHead; }
    public boolean isPeriod() { return cyclic == 1 || cyclic < 0; } //정규 일정이면 true리턴
    public boolean isOverride(long id) { return Math.abs(cyclic) == id || additionalOverrideId.contains(id); } //이 일정이 해당 id를 override하는가?

    public void setPolyLine(TMapPolyLine polyLine) { this.polyLine = polyLine; }
    public Pair<String> getPolyLineInStr() {
        String result[] = {"",""}; //lon , lat
        if (polyLine != null) {
            ArrayList<TMapPoint> points = polyLine.getLinePoint();
            for ( int i = 0; i < points.size(); i++ ) {
                TMapPoint p = points.get(i);
                result[0] += String.format("%f",p.getLongitude());
                result[1] += String.format("%f",p.getLatitude());

                if (i != points.size() - 1) {
                    result[0] += ",";
                    result[1] += ",";
                }

            }
        }

        return new Pair<String>(result[0], result[1]);
    }

    public void setCyclic(long cyclic) { this.cyclic = cyclic; }
    public void setDate(int year, int month, int day, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
    }
    public void setAdditionalOverrideId(ArrayList<Long> l) { this.additionalOverrideId = l; }

    public String toString() {
        return String.format("%d | [%s] %d/%d/%d(%d) %d:%d ~ %d:%d  Cyclic : %d / Additional ID : %s", id, name, year, month, day, dayOfWeek,
                startHour, startMin, endHour, endMin , cyclic, additionalOverrideId.toString());
    }

}
