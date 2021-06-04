package com.example.pnuwalker.schedule;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;

import java.util.Calendar;

public class SchduleFragment extends Fragment {
    private Schedule schedule = new Schedule();
    private AutoResizeTextView monday[] = new AutoResizeTextView[25];
    private AutoResizeTextView tuesday[] = new AutoResizeTextView[25];
    private AutoResizeTextView wednesday[] = new AutoResizeTextView[25];
    private AutoResizeTextView thursday[] = new AutoResizeTextView[25];
    private AutoResizeTextView friday[] = new AutoResizeTextView[25];
    private int year[] = new int[7];
    private int month[] = new int[7];
    private int day[] = new int[7];
    private String daystring[] = new String[7];
    private int count = 0;

    Calendar cal = Calendar.getInstance();

    Cursor cursor;

    public void initializeDayArray(int count) {
        for (int i = 0; i < count; i++) {
            year[i] = cal.get(cal.YEAR);
            month[i] = cal.get(cal.MONTH);
            day[i] = cal.get(cal.DAY_OF_MONTH);
            cal.add(cal.DAY_OF_MONTH, 1);
        }
    }

    public void initializeDayStringArray(int count) {
        for (int i = 0; i < count; i++) {
            daystring[i] = String.format("%d_%02d_%02d",year[i],month[i]+1,day[i]);
        }
    }

    public boolean Stringcmp(String target, int count) {
        for (int i = 0; i < count; i++) {
            if (target.equals(daystring[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        cursor = MainActivity.db.query("schedule1", null, null, null, null, null, null, null);

        super.onActivityCreated(b);
        monday[0] = (AutoResizeTextView) getView().findViewById(R.id.monday0);
        monday[1] = (AutoResizeTextView) getView().findViewById(R.id.monday1);
        monday[2] = (AutoResizeTextView) getView().findViewById(R.id.monday2);
        monday[3] = (AutoResizeTextView) getView().findViewById(R.id.monday3);
        monday[4] = (AutoResizeTextView) getView().findViewById(R.id.monday4);
        monday[5] = (AutoResizeTextView) getView().findViewById(R.id.monday5);
        monday[6] = (AutoResizeTextView) getView().findViewById(R.id.monday6);
        monday[7] = (AutoResizeTextView) getView().findViewById(R.id.monday7);
        monday[8] = (AutoResizeTextView) getView().findViewById(R.id.monday8);
        monday[9] = (AutoResizeTextView) getView().findViewById(R.id.monday9);
        monday[10] = (AutoResizeTextView) getView().findViewById(R.id.monday10);
        monday[11] = (AutoResizeTextView) getView().findViewById(R.id.monday11);
        monday[12] = (AutoResizeTextView) getView().findViewById(R.id.monday12);
        monday[13] = (AutoResizeTextView) getView().findViewById(R.id.monday13);
        monday[14] = (AutoResizeTextView) getView().findViewById(R.id.monday14);
        monday[15] = (AutoResizeTextView) getView().findViewById(R.id.monday15);
        monday[16] = (AutoResizeTextView) getView().findViewById(R.id.monday16);
        monday[17] = (AutoResizeTextView) getView().findViewById(R.id.monday17);
        monday[18] = (AutoResizeTextView) getView().findViewById(R.id.monday18);
        monday[19] = (AutoResizeTextView) getView().findViewById(R.id.monday19);
        monday[20] = (AutoResizeTextView) getView().findViewById(R.id.monday20);
        monday[21] = (AutoResizeTextView) getView().findViewById(R.id.monday21);
        monday[22] = (AutoResizeTextView) getView().findViewById(R.id.monday22);
        monday[23] = (AutoResizeTextView) getView().findViewById(R.id.monday23);
        monday[24] = (AutoResizeTextView) getView().findViewById(R.id.monday24);


        tuesday[0] = (AutoResizeTextView) getView().findViewById(R.id.tuesday0);
        tuesday[1] = (AutoResizeTextView) getView().findViewById(R.id.tuesday1);
        tuesday[2] = (AutoResizeTextView) getView().findViewById(R.id.tuesday2);
        tuesday[3] = (AutoResizeTextView) getView().findViewById(R.id.tuesday3);
        tuesday[4] = (AutoResizeTextView) getView().findViewById(R.id.tuesday4);
        tuesday[5] = (AutoResizeTextView) getView().findViewById(R.id.tuesday5);
        tuesday[6] = (AutoResizeTextView) getView().findViewById(R.id.tuesday6);
        tuesday[7] = (AutoResizeTextView) getView().findViewById(R.id.tuesday7);
        tuesday[8] = (AutoResizeTextView) getView().findViewById(R.id.tuesday8);
        tuesday[9] = (AutoResizeTextView) getView().findViewById(R.id.tuesday9);
        tuesday[10] = (AutoResizeTextView) getView().findViewById(R.id.tuesday10);
        tuesday[11] = (AutoResizeTextView) getView().findViewById(R.id.tuesday11);
        tuesday[12] = (AutoResizeTextView) getView().findViewById(R.id.tuesday12);
        tuesday[13] = (AutoResizeTextView) getView().findViewById(R.id.tuesday13);
        tuesday[14] = (AutoResizeTextView) getView().findViewById(R.id.tuesday14);
        tuesday[15] = (AutoResizeTextView) getView().findViewById(R.id.tuesday15);
        tuesday[16] = (AutoResizeTextView) getView().findViewById(R.id.tuesday16);
        tuesday[17] = (AutoResizeTextView) getView().findViewById(R.id.tuesday17);
        tuesday[18] = (AutoResizeTextView) getView().findViewById(R.id.tuesday18);
        tuesday[19] = (AutoResizeTextView) getView().findViewById(R.id.tuesday19);
        tuesday[20] = (AutoResizeTextView) getView().findViewById(R.id.tuesday20);
        tuesday[21] = (AutoResizeTextView) getView().findViewById(R.id.tuesday21);
        tuesday[22] = (AutoResizeTextView) getView().findViewById(R.id.tuesday22);
        tuesday[23] = (AutoResizeTextView) getView().findViewById(R.id.tuesday23);
        tuesday[24] = (AutoResizeTextView) getView().findViewById(R.id.tuesday24);


        wednesday[0] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday0);
        wednesday[1] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday1);
        wednesday[2] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday2);
        wednesday[3] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday3);
        wednesday[4] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday4);
        wednesday[5] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday5);
        wednesday[6] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday6);
        wednesday[7] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday7);
        wednesday[8] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday8);
        wednesday[9] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday9);
        wednesday[10] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday10);
        wednesday[11] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday11);
        wednesday[12] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday12);
        wednesday[13] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday13);
        wednesday[14] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday14);
        wednesday[15] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday15);
        wednesday[16] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday16);
        wednesday[17] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday17);
        wednesday[18] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday18);
        wednesday[19] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday19);
        wednesday[20] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday20);
        wednesday[21] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday21);
        wednesday[22] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday22);
        wednesday[23] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday23);
        wednesday[24] = (AutoResizeTextView) getView().findViewById(R.id.Wednesday24);


        thursday[0] = (AutoResizeTextView) getView().findViewById(R.id.Thursday0);
        thursday[1] = (AutoResizeTextView) getView().findViewById(R.id.Thursday1);
        thursday[2] = (AutoResizeTextView) getView().findViewById(R.id.Thursday2);
        thursday[3] = (AutoResizeTextView) getView().findViewById(R.id.Thursday3);
        thursday[4] = (AutoResizeTextView) getView().findViewById(R.id.Thursday4);
        thursday[5] = (AutoResizeTextView) getView().findViewById(R.id.Thursday5);
        thursday[6] = (AutoResizeTextView) getView().findViewById(R.id.Thursday6);
        thursday[7] = (AutoResizeTextView) getView().findViewById(R.id.Thursday7);
        thursday[8] = (AutoResizeTextView) getView().findViewById(R.id.Thursday8);
        thursday[9] = (AutoResizeTextView) getView().findViewById(R.id.Thursday9);
        thursday[10] = (AutoResizeTextView) getView().findViewById(R.id.Thursday10);
        thursday[11] = (AutoResizeTextView) getView().findViewById(R.id.Thursday11);
        thursday[12] = (AutoResizeTextView) getView().findViewById(R.id.Thursday12);
        thursday[13] = (AutoResizeTextView) getView().findViewById(R.id.Thursday13);
        thursday[14] = (AutoResizeTextView) getView().findViewById(R.id.Thursday14);
        thursday[15] = (AutoResizeTextView) getView().findViewById(R.id.Thursday15);
        thursday[16] = (AutoResizeTextView) getView().findViewById(R.id.Thursday16);
        thursday[17] = (AutoResizeTextView) getView().findViewById(R.id.Thursday17);
        thursday[18] = (AutoResizeTextView) getView().findViewById(R.id.Thursday18);
        thursday[19] = (AutoResizeTextView) getView().findViewById(R.id.Thursday19);
        thursday[20] = (AutoResizeTextView) getView().findViewById(R.id.Thursday20);
        thursday[21] = (AutoResizeTextView) getView().findViewById(R.id.Thursday21);
        thursday[22] = (AutoResizeTextView) getView().findViewById(R.id.Thursday22);
        thursday[23] = (AutoResizeTextView) getView().findViewById(R.id.Thursday23);
        thursday[24] = (AutoResizeTextView) getView().findViewById(R.id.Thursday24);


        friday[0] = (AutoResizeTextView) getView().findViewById(R.id.Friday0);
        friday[1] = (AutoResizeTextView) getView().findViewById(R.id.Friday1);
        friday[2] = (AutoResizeTextView) getView().findViewById(R.id.Friday2);
        friday[3] = (AutoResizeTextView) getView().findViewById(R.id.Friday3);
        friday[4] = (AutoResizeTextView) getView().findViewById(R.id.Friday4);
        friday[5] = (AutoResizeTextView) getView().findViewById(R.id.Friday5);
        friday[6] = (AutoResizeTextView) getView().findViewById(R.id.Friday6);
        friday[7] = (AutoResizeTextView) getView().findViewById(R.id.Friday7);
        friday[8] = (AutoResizeTextView) getView().findViewById(R.id.Friday8);
        friday[9] = (AutoResizeTextView) getView().findViewById(R.id.Friday9);
        friday[10] = (AutoResizeTextView) getView().findViewById(R.id.Friday10);
        friday[11] = (AutoResizeTextView) getView().findViewById(R.id.Friday11);
        friday[12] = (AutoResizeTextView) getView().findViewById(R.id.Friday12);
        friday[13] = (AutoResizeTextView) getView().findViewById(R.id.Friday13);
        friday[14] = (AutoResizeTextView) getView().findViewById(R.id.Friday14);
        friday[15] = (AutoResizeTextView) getView().findViewById(R.id.Friday15);
        friday[16] = (AutoResizeTextView) getView().findViewById(R.id.Friday16);
        friday[17] = (AutoResizeTextView) getView().findViewById(R.id.Friday17);
        friday[18] = (AutoResizeTextView) getView().findViewById(R.id.Friday18);
        friday[19] = (AutoResizeTextView) getView().findViewById(R.id.Friday19);
        friday[20] = (AutoResizeTextView) getView().findViewById(R.id.Friday20);
        friday[21] = (AutoResizeTextView) getView().findViewById(R.id.Friday21);
        friday[22] = (AutoResizeTextView) getView().findViewById(R.id.Friday22);
        friday[23] = (AutoResizeTextView) getView().findViewById(R.id.Friday23);
        friday[24] = (AutoResizeTextView) getView().findViewById(R.id.Friday24);

        for (int a = 0; a < cursor.getCount(); a++) {
            if (a == 0) {
                cursor.moveToFirst();
            }
            else {
                cursor.moveToNext();
            }
            System.out.println(" 0. ID "
                    + cursor.getInt(0)
                    + " 1.date "
                    + cursor.getString(1)
                    + " 2.day_week "
                    + cursor.getInt(2)
                    + " 3.start_time "
                    + cursor.getString(3)
                    + " 4.end_time "
                    + cursor.getString(4)
                    + " 5.start_location "
                    + cursor.getString(5)
                    + " 6.end_location "
                    + cursor.getString(6)
                    + " 7.end_location_name "
                    + cursor.getString(7)
                    + " 8.name "
                    + cursor.getString(8)
                    + " 9.script "
                    + cursor.getString(9)
                    + " 10.cyclic "
                    + cursor.getInt(10)
                    + " 11.tpolyline_x "
                    + cursor.getString(11)
                    + " 12.tpolyline_y "
                    + cursor.getString(12)
                    + " 13.room "
                    + cursor.getString(13));
        }

        if (count == 0) {
            initializeDayArray(7);
            initializeDayStringArray(7);

            for (int i = 0; i < 7; i++) {
                System.out.println("=====================================");
                System.out.println(daystring[i]);
                System.out.println("=====================================");
            }

            count++;
        }

        for (int a = 0; a < cursor.getCount(); a++) {
            if (a == 0) {
                cursor.moveToFirst();
            } else {
                cursor.moveToNext();
            }

            if (cursor.getInt(10) == 1) {
                schedule.addSchedule(cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(13), cursor.getInt(10));
            }
        }

        for (int a = 0; a < cursor.getCount(); a++) {
            if (a == 0) {
                cursor.moveToFirst();
            } else {
                cursor.moveToNext();
            }

            if ((cursor.getInt(10) != 1) && (Stringcmp(cursor.getString(1), 7))) {
                schedule.addSchedule(cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(13), cursor.getInt(10));
            }
        }
        schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schdule_fragment, container, false);

        Button search_btn = (Button) view.findViewById(R.id.button3);
        Button delete_btn = (Button) view.findViewById(R.id.button4);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onFragmentChanged(0);
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onFragmentChanged(2);
            }
        });

        return view;
    }
}
