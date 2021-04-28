package com.example.pnuwalker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

public class FindPNUPath {
    TMapData tMapData;
    TMapPolyLine polyline;

    public FindPNUPath() {
        tMapData = new TMapData();
    }

    public void findPath(TMapPoint start, TMapPoint end, boolean needDelay) {
        TMapPoint p[] = {start ,end};
        try {
            TMapPolyLine line = new findPath().execute(p).get();
            polyline = line;
            if ( needDelay )
                Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getPolyLineinStr() {
        String result[] = {"",""}; //lon , lat
        if (polyline != null) {
            ArrayList<TMapPoint> points = polyline.getLinePoint();
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
        return result;
    }

    public TMapPolyLine getPolyLine() { return polyline; }

    class findPath extends AsyncTask<TMapPoint, Void,TMapPolyLine> {
        @Override
        protected TMapPolyLine doInBackground(TMapPoint... tMapPoints) {
            try {
                TMapPolyLine line = tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPoints[0], tMapPoints[1]);
                return line;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
