package com.example.pnuwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;
    private SchduleFragment schduleFragment;
    private TravelFragment travelFragment;
    private ScheduleSearchFragment scheduleSearchFragment;
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}