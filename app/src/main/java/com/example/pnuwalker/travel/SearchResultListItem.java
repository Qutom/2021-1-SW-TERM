package com.example.pnuwalker.travel;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.io.Serializable;

public class SearchResultListItem {
    private TMapPOIItem poi;
    private double distance;

    public SearchResultListItem(TMapPOIItem poi, double distance) {
        this.poi = poi;
        this.distance = distance;
    }

    public TMapPOIItem getPOI() { return poi; }
    public double getDistance() { return distance; }
}