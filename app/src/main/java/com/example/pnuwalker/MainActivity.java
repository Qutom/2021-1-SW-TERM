package com.example.pnuwalker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pnuwalker.travel.TravelFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;
    private SchduleFragment scheduleFragment;
    private TravelFragment travelFragment;
    private ScheduleSearchFragment scheduleSearchFragment;
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        scheduleSearchFragment = new ScheduleSearchFragment();
        bottomNavigation = findViewById(R.id.navigationView);
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, mainFragment).commitAllowingStateLoss();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch(item.getItemId()) {
                    case R.id.page_main :
                        if ( mainFragment != null ) {
                            //MainFragment는 매번 정보를 새로 받아와야하므로, 새로고침됨
                            fragmentManager.beginTransaction().detach(mainFragment).attach(mainFragment).commit();
                            fragmentManager.beginTransaction().show(mainFragment).commit();
                        }

                        if ( travelFragment != null )
                            fragmentManager.beginTransaction().hide(travelFragment).commit();
                        if ( scheduleFragment != null)
                            fragmentManager.beginTransaction().hide(scheduleFragment).commit();
                        break;

                    case R.id.page_schedule :
                        if ( scheduleFragment == null ) {
                            scheduleFragment = new SchduleFragment();
                            fragmentManager.beginTransaction().add(R.id.fragmentLayout, scheduleFragment).commit();
                        }
                        if ( mainFragment != null )
                            fragmentManager.beginTransaction().hide(mainFragment).commit();
                        if ( travelFragment != null )
                            fragmentManager.beginTransaction().hide(travelFragment).commit();
                        if ( scheduleFragment != null)
                            fragmentManager.beginTransaction().show(scheduleFragment).commit();
                        break;
                    case R.id.page_travel :
                        if ( travelFragment == null ) {
                            travelFragment = new TravelFragment();
                            fragmentManager.beginTransaction().add(R.id.fragmentLayout, travelFragment).commit();
                        }
                        if ( mainFragment != null )
                            fragmentManager.beginTransaction().hide(mainFragment).commit();
                        if ( travelFragment != null )
                            fragmentManager.beginTransaction().show(travelFragment).commit();
                        if ( scheduleFragment != null)
                            fragmentManager.beginTransaction().hide(scheduleFragment).commit();
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
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, scheduleFragment).commit();
        }
        else if (index == 0) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, scheduleSearchFragment).commit();
        }
    }
}