package com.example.pnuwalker.travel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapPOIItem;

import java.util.ArrayList;

public class SearchResultListAdapter extends BaseAdapter {

    private TextView numberTextView;
    private TextView nameTextView;
    private TextView distanceTextview;

    private ArrayList<SearchResultListItem> item = new ArrayList<>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_result_item, parent , false);
        }

        numberTextView = convertView.findViewById(R.id.search_result_item_number);
        nameTextView = convertView.findViewById(R.id.search_result_item_name);
        distanceTextview = convertView.findViewById(R.id.search_result_item_distance);

        TMapPOIItem i = item.get(position).getPOI();
        nameTextView.setText(i.getPOIName());
        numberTextView.setText(Integer.toString(position));
        distanceTextview.setText(String.format("%.2f Km", item.get(position).getDistance()/1000));
        return convertView;
    }

    public void addItem(TMapPOIItem poi, double distance) {
        SearchResultListItem i = new SearchResultListItem(poi, distance);
        item.add(i);
        Log.d("Add",  item.get(item.size()-1).getPOI().toString());
    }

    public void clear() { item.clear(); }
    @Override public int getCount() { return item.size(); }
    @Override public Object getItem(int position) { return item.get(position); }
    @Override public long getItemId(int position) { return position; }

}
