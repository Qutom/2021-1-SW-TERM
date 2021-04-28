package com.example.pnuwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.pnuwalker.travel.TravelFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper helper;
    static SQLiteDatabase db;

    private MainFragment mainFragment;
    private SchduleFragment schduleFragment;
    private TravelFragment travelFragment;
    private ScheduleSearchFragment scheduleSearchFragment;
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainFragwhole mainFragwhole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = new DataBaseHelper(MainActivity.this, "schedule1.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

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
    }

    public void onFragmentChange(int fragmentNum) {
        //프래그먼트의 번호에 따라 다르게 작동하는 조건문
        if(fragmentNum == 1) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, mainFragwhole).commit();
        } else if(fragmentNum == 2) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, mainFragment).commit();
        }
    }
}