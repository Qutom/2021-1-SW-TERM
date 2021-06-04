package com.example.pnuwalker.schedule;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.pnuwalker.MainActivity;
import com.example.pnuwalker.R;
import com.example.pnuwalker.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDeleteFragment extends Fragment {
    Cursor cursor;
    ScheduleListAdapter adapter;
    ListView ScheduleListView;
    List<Data> dataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_fragment, container, false);
        cursor = MainActivity.db.query("schedule1", null, null, null, null, null, null, null);

        ScheduleListView = (ListView)view.findViewById(R.id.scheduleListView);
        dataList = new ArrayList<Data>();
        adapter = new ScheduleListAdapter(getContext().getApplicationContext(), dataList, this, getActivity());
        ScheduleListView.setAdapter(adapter);

        for (int a = 0; a < cursor.getCount(); a++) {
            if (a == 0) {
                cursor.moveToFirst();
            }
            else {
                cursor.moveToNext();
            }
            Data data = new Data(cursor.getInt(0),
                    cursor.getInt(10),
                    cursor.getString(8),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(1));
            dataList.add(data);
        }

        adapter.notifyDataSetChanged();

        return view;
    }
}
