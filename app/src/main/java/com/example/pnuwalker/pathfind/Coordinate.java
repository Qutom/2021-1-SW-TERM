package com.example.pnuwalker.pathfind;

public class Coordinate {
    private double lon;
    private double lat;

    public Coordinate(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() { return lon; }
    public double getLat() { return lat; }
    public void setLon(double v) { lon = v; }
    public void setLat(double v) { lat = v; }
}
