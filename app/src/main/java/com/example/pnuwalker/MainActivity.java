package com.example.pnuwalker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pnuwalker.controlschedule.ControlSchedule;
import com.example.pnuwalker.controlschedule.DaySchedule;
import com.example.pnuwalker.controlschedule.ScheduleReader;
import com.example.pnuwalker.main.MainFragment;
import com.example.pnuwalker.main.MainFragwhole;
import com.example.pnuwalker.schedule.SchduleFragment;
import com.example.pnuwalker.schedule.ScheduleDeleteFragment;
import com.example.pnuwalker.schedule.ScheduleSearchFragment;
import com.example.pnuwalker.travel.TravelFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skt.Tmap.TMapGpsManager;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {
    public static DataBaseHelper helper;
    public static SQLiteDatabase db;

    private MainFragment mainFragment;
    private SchduleFragment schduleFragment;
    private TravelFragment travelFragment;
    private ScheduleSearchFragment scheduleSearchFragment;
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainFragwhole mainFragwhole;
    private ScheduleDeleteFragment scheduleDeleteFragment;


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = new DataBaseHelper(MainActivity.this, "schedule1.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        db.delete("schedule1" , "_id > 0", null);
        MainActivity.db.delete("schedule1", null, null);

        System.out.println("Create Success============================");
        System.out.println("Create Success============================");
        System.out.println("Create Success============================");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragwhole = new MainFragwhole();
        mainFragment = new MainFragment();
        schduleFragment = new SchduleFragment();
        travelFragment = new TravelFragment();
        scheduleSearchFragment = new ScheduleSearchFragment();
        scheduleDeleteFragment = new ScheduleDeleteFragment();
        bottomNavigation = findViewById(R.id.navigationView);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, mainFragment).commitAllowingStateLoss();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.page_main :
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,  mainFragment).commit();
                        break;
                    case R.id.page_schedule :
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,  schduleFragment).commit();
                        break;
                    case R.id.page_travel :
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,  travelFragment).commit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public void onFragmentChanged(int index) {
        if (index == 1) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, schduleFragment).commit();
        }
        else if (index == 0) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, scheduleSearchFragment).commit();
        }
        else if (index == 2) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, scheduleDeleteFragment).commit();
        }
    }

    public void onFragmentChange(int fragmentNum) {
        //프래그먼트의 번호에 따라 다르게 작동하는 조건문
        if(fragmentNum == 1) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, mainFragwhole).commit();
        } else if(fragmentNum == 2) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, mainFragment).commit();
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활설화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
        public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    
    public void checkRunTimePErmission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){

        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    REQUIRED_PERMISSIONS[0])){
                Toast.makeText(MainActivity.this,"이 입을 실행하려면 위치 접근 권한이 필요합니다",
                        Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }
}