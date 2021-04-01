package com.example.pnuwalker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class ScheduleSearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        // Spinner
        Spinner yearSpinner = (Spinner)view.findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner termSpinner = (Spinner)view.findViewById(R.id.spinner_term);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_term, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(monthAdapter);

        Spinner gradeSpinner = (Spinner)view.findViewById(R.id.spinner_grade);
        ArrayAdapter gradeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.grade, android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);

        Spinner collegeSpinner = (Spinner)view.findViewById(R.id.spinner_college);
        ArrayAdapter collegeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.college, android.R.layout.simple_spinner_item);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner.setAdapter(collegeAdapter);

        return view;
    }
}