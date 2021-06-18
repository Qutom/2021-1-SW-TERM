package com.example.pnuwalker.travel;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import java.io.Serializable;

public class SimplePOI implements Serializable {
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
    String buildingNo2;

    public SimplePOI(TMapPOIItem poi) {
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
        buildingNo2 = poi.buildingNo2;
    }

    public String toString() {
        String s = "";
        s += name + "  : " + lon + ", " + lat + "\n";
        s += telNo + "\n";
        s += upperAddrName + " " + middleAddrName + " " + lowerAddrName + "\n";
        s += firstNo + " " + secondNo + "\n";
        s += upperBizName + " " + middleBizName + " " + lowerBizName + "\n";
        s += roadName + " " + buildingNo1 + "-" + buildingNo2 + "\n";
        return s;
    }
}
