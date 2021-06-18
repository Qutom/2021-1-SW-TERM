package com.example.pnuwalker.travel;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

public class PNUBuildingInfo implements Serializable {
    String name;
    double lon;
    double lat;
    String timeInfo = "null";
    ArrayList<Integer> image = new ArrayList<>();
    int buildingNum;

    public PNUBuildingInfo(String name, double lon, double lat, int buildNum) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        buildingNum = buildNum;
    }

    public PNUBuildingInfo(String name, double lon, double lat, String timeInfo, ArrayList<Integer> image, int buildNum) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.timeInfo = timeInfo;
        this.image = image;
        buildingNum = buildNum;
    }
}
