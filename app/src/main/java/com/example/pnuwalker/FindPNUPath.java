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

    public FindPNUPath() {
        tMapData = new TMapData();
    }

    public TMapPolyLine getPathPointList(TMapPoint start, TMapPoint end) {

        TMapPoint p[] = {start ,end};
        try {
            TMapPolyLine line = new findPath().execute(p).get();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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
