package com.example.pnuwalker.travel;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.io.Serializable;

public class SearchResult implements Serializable {
    Double lon;
    Double lat;

    String name;
    String telNo;
    String upperAddrName;
    String middleAddrName;
    String lowerAddrName;
    String firstNo;
    String secondNo;
    String upperBizName;
    String middleBizName;
    String lowerBizName;
    String roadName;
    String buildingNo1;

    public SearchResult(TMapPOIItem poi) {
        TMapPoint p = poi.getPOIPoint();
        lon = p.getLongitude();
        lat = p.getLatitude();
        name = poi.name;
        telNo = poi.telNo;
        upperAddrName = poi.upperAddrName;
        middleAddrName = poi.middleAddrName;
        lowerAddrName = poi.lowerAddrName;
        firstNo = poi.firstNo;
        secondNo = poi.secondNo;
        upperBizName = poi.upperBizName;
        middleBizName = poi.middleBizName;
        lowerBizName = poi.lowerBizName;
        roadName = poi.roadName;
        buildingNo1 = poi.buildingNo1;
    }
}
