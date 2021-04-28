package com.example.pnuwalker;

import android.Manifest;
import android.location.Location;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

import static com.example.pnuwalker.MainFragment.frag_tMapView;

public class MainFragGps extends AppCompatActivity  {




//
//    public static void findgps(TMapGpsManager mygps) {
//
//        mygps = new TMapGpsManager(this);
//
//        // Initial Setting
//        mygps.setMinTime(1000);
//        mygps.setMinDistance(10);
//        mygps.setProvider(mygps.NETWORK_PROVIDER);
//        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);
//        mygps.OpenGps();
//
//    }
//    public void onLocationChange(Location location) {
//        frag_tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
//        frag_tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
//    }
//
//

}
