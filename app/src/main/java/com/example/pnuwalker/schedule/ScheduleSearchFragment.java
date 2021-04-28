package com.example.pnuwalker.schedule;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.pnuwalker.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ScheduleSearchFragment extends Fragment {
    private String[] classinfo = new String[8];

    int count = 0;
    String courseID = "";
    String courseUniversity = "";
    String courseYear = "";
    String courseTerm = "";
    String courseArea = "";
    String courseMajor = "";
    String courseGrade = "";
    String courseTitle = "";
    String courseCredit = "";
    String courseDivide = "";
    String coursePersonnel = "";
    String courseProfessor = "";
    String courseTime = "";
    String courseRoom = "";

    ListView courseListView;
    CourseListAdapter adapter;
    List<Course> courseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        String[] searchString = new String [6];

        courseListView = (ListView)view.findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new CourseListAdapter(getContext().getApplicationContext(), courseList, this);
        courseListView.setAdapter(adapter);

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

        Spinner universitySpinner = (Spinner)view.findViewById(R.id.spinner_university);
        ArrayAdapter[] universityAdapter = {ArrayAdapter.createFromResource(getContext(), R.array.간호대학, android.R.layout.simple_spinner_item)};
        universityAdapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(universityAdapter[0]);

        Spinner majorSpinner = (Spinner)view.findViewById(R.id.spinner_major);
        ArrayAdapter[] majorAdapter = {ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item)};
        majorAdapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter[0]);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0) {
                    searchString[0] = "    \"2021\",\n";
                }
                else if (parent.getSelectedItemPosition() == 1) {
                    searchString[0] = "    \"2020\",\n";
                }
                else if (parent.getSelectedItemPosition() == 2) {
                    searchString[0] = "    \"2019\",\n";
                }
                else if (parent.getSelectedItemPosition() == 3) {
                    searchString[0] = "    \"2018\",\n";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0) {
                    searchString[1] = "    \"10\",\n";
                }
                else if (parent.getSelectedItemPosition() == 1) {
                    searchString[1] = "    \"20\",\n";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0) {
                    searchString[2] = "    \"0\",\n";
                }
                else if (parent.getSelectedItemPosition() == 1) {
                    searchString[2] = "    \"1\",\n";
                }
                else if (parent.getSelectedItemPosition() == 2) {
                    searchString[2] = "    \"2\",\n";
                }
                else if (parent.getSelectedItemPosition() == 3) {
                    searchString[2] = "    \"3\",\n";
                }
                else if (parent.getSelectedItemPosition() == 4) {
                    searchString[2] = "    \"4\",\n";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        int[] point = new int[1];
        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.간호대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"480000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 1) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.경영대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"490000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 2) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.경제통상대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"500000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 3) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.공과대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"340000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 4) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.관광컨벤션학부, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"445000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 5) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나노과학기술대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"460000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 6) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.대외교류본부, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"606900\",\n";
                }
                else if (parent.getSelectedItemPosition() == 7) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.법과대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"350000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 8) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.사범대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"360000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 9) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.사회과학대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"320000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 10) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.상과대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"370000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 11) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.생명자원과학대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"470000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 12) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.생활환경대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"430000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 13) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.스포츠과학부, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"443000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 14) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.약학대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"380000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 15) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.예술대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"420000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 16) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.의과대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"390000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 17) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.이공대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"560000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 18) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.인문대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"310000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 19) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.자연과학대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"330000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 20) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.자유전공학부, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"440000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 21) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.정보의생명공학대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"590000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 22) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.치과대학, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"400000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 23) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.치의학전문대학원, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"400000\",\n";
                }
                else if (parent.getSelectedItemPosition() == 24) {
                    point[0] = parent.getSelectedItemPosition();
                    universityAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.한의학전문대학원, android.R.layout.simple_spinner_item);
                    universitySpinner.setAdapter(universityAdapter[0]);
                    searchString[3] = "    \"294000\",\n";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((searchString[3] == "    \"480000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"481100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"490000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"491100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"491200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"500000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"502000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"505000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"503100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"504000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.국제학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"501000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"340000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"346730\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.건설융합학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"344700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"341000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"346900\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"346760\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"345300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"346770\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 7) {
                        searchString[4] = "    \"346740\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 8) {
                        searchString[4] = "    \"345600\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 9) {
                        searchString[4] = "    \"346750\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 10) {
                        searchString[4] = "    \"344000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 11) {
                        searchString[4] = "    \"343500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 12) {
                        searchString[4] = "    \"346710\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.전기컴퓨터공학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 13) {
                        searchString[4] = "    \"343800\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 14) {
                        searchString[4] = "    \"346500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 15) {
                        searchString[4] = "    \"342100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 16) {
                        searchString[4] = "    \"342400\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 17) {
                        searchString[4] = "    \"342500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 18) {
                        searchString[4] = "    \"346720\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.화공생명환경공학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 19) {
                        searchString[4] = "    \"345700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"445000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"445100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"460000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"461820\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"461600\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"461810\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"606900\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"606900\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"350000\",\n") == true) {
                    searchString[4] = "    \"\",\n";
                    majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                    majorSpinner.setAdapter(majorAdapter[0]);
                }
                else if ((searchString[3] == "    \"360000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"362100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"361100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"361500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"364300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"361700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"364700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"364100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 7) {
                        searchString[4] = "    \"363300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 8) {
                        searchString[4] = "    \"361300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 9) {
                        searchString[4] = "    \"362300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 10) {
                        searchString[4] = "    \"363800\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 11) {
                        searchString[4] = "    \"363100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 12) {
                        searchString[4] = "    \"364900\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 13) {
                        searchString[4] = "    \"363500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 14) {
                        searchString[4] = "    \"365100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 15) {
                        searchString[4] = "    \"364060\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 16) {
                        searchString[4] = "    \"363060\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 17) {
                        searchString[4] = "    \"362500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 18) {
                        searchString[4] = "    \"364500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"320000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"323100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"323500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"322100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"322300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"322500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"321500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"321100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"370000\",\n") == true) {
                    searchString[4] = "    \"\",\n";
                    majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                    majorSpinner.setAdapter(majorAdapter[0]);
                }
                else if ((searchString[3] == "    \"470000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"471300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"473100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"472300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"473400\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"472200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"471100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"472100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 7) {
                        searchString[4] = "    \"474800\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 8) {
                        searchString[4] = "    \"471200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 9) {
                        searchString[4] = "    \"471400\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 10) {
                        searchString[4] = "    \"474100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 11) {
                        searchString[4] = "    \"475800\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 12) {
                        searchString[4] = "    \"473500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"430000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"432100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"436200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"435100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"433100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"436100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"443000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"443100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"380000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"384000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.약학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"420000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"425200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.디자인학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"424100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.무용학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"422100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.미술학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"426100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"421100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.음악학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"422300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.조형학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"423200\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.한국음악학과, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"390000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"393100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"391100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"560000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"561300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"310000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"313700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"311100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"312700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"312500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"312300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"313100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"314000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 7) {
                        searchString[4] = "    \"312100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 8) {
                        searchString[4] = "    \"311500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 9) {
                        searchString[4] = "    \"311300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 10) {
                        searchString[4] = "    \"313300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 11) {
                        searchString[4] = "    \"313500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"330000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"335500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"331300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[4] = "    \"334300\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[4] = "    \"334500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 4) {
                        searchString[4] = "    \"334700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 5) {
                        searchString[4] = "    \"331100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 6) {
                        searchString[4] = "    \"336900\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 7) {
                        searchString[4] = "    \"335400\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 8) {
                        searchString[4] = "    \"336500\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 9) {
                        searchString[4] = "    \"331700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 10) {
                        searchString[4] = "    \"335700\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 11) {
                        searchString[4] = "    \"332100\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"440000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"441000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"590000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"591000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.의생명융합공학부, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[4] = "    \"592000\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"400000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"40511A\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
                else if ((searchString[3] == "    \"294000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[4] = "    \"29401A\",\n";
                        majorAdapter[0] = ArrayAdapter.createFromResource(getContext(), R.array.나머지, android.R.layout.simple_spinner_item);
                        majorSpinner.setAdapter(majorAdapter[0]);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((searchString[4] == "    \"504000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"504001\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"504002\",\n";
                    }
                }
                else if ((searchString[4] == "    \"346730\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"346731\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"346732\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"346733\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 3) {
                        searchString[5] = "    \"346734\",\n";
                    }
                }
                else if ((searchString[4] == "    \"346710\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"346712\",\n";
                    }
                }
                else if ((searchString[4] == "    \"346720\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"346721\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"346722\",\n";
                    }
                }
                else if ((searchString[4] == "    \"384000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"384001\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"384002\",\n";
                    }
                }
                else if ((searchString[4] == "    \"425200\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"425205\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"425201\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"425204\",\n";
                    }
                }
                else if ((searchString[4] == "    \"424100\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"424104\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"424101\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"424102\",\n";
                    }
                }
                else if ((searchString[4] == "    \"422100\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"422104\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"422101\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"422102\",\n";
                    }
                }
                else if ((searchString[4] == "    \"422300\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"422302\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"422301\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"422303\",\n";
                    }
                }
                else if ((searchString[4] == "    \"423200\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"423202\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"423203\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 2) {
                        searchString[5] = "    \"423201\",\n";
                    }
                }
                else if ((searchString[4] == "    \"591000\",\n") == true) {
                    if (parent.getSelectedItemPosition() == 0) {
                        searchString[5] = "    \"591003\",\n";
                    }
                    else if (parent.getSelectedItemPosition() == 1) {
                        searchString[5] = "    \"591001\",\n";
                    }
                }
                else {
                    searchString[5] = "    \"\",\n";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button searchButton = (Button)view.findViewById(R.id.button2);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = null;
                            url = new URL("https://e-onestop.pusan.ac.kr/middleware/curriculum/college/getCurriculumCollegeList");
                            HttpsURLConnection con = null;
                            con = (HttpsURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json");
                            con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
                            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
                            con.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
                            con.setRequestProperty("Connection", "keep-alive");
                            con.setRequestProperty("Host", "e-onestop.pusan.ac.kr");
                            con.setRequestProperty("Origin", "https://e-onestop.pusan.ac.kr");
                            con.setRequestProperty("Referer", "https://e-onestop.pusan.ac.kr/menu/class/C03/C03006?menuId=2000030306&rMenu=03");
                            con.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
                            con.setRequestProperty("sec-ch-ua-mobile", "?0");
                            con.setRequestProperty("Sec-Fetch-Dest", "empty");
                            con.setRequestProperty("Sec-Fetch-Mode", "cors");
                            con.setRequestProperty("Sec-Fetch-Site", "same-origin");
                            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
                            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                            /* Payload support */
                            con.setDoOutput(true);
                            DataOutputStream out = null;
                            out = new DataOutputStream(con.getOutputStream());
                            out.writeBytes("{\n");
                            out.writeBytes("  \"pName\": [\n");
                            out.writeBytes("    \"params1\",\n");
                            out.writeBytes("    \"params2\",\n");
                            out.writeBytes("    \"params3\",\n");
                            out.writeBytes("    \"params4\",\n");
                            out.writeBytes("    \"params5\",\n");
                            out.writeBytes("    \"params6\",\n");
                            out.writeBytes("    \"params7\",\n");
                            out.writeBytes("    \"params8\",\n");
                            out.writeBytes("    \"params9\",\n");
                            out.writeBytes("    \"params10\",\n");
                            out.writeBytes("    \"params11\",\n");
                            out.writeBytes("    \"params12\",\n");
                            out.writeBytes("    \"params13\",\n");
                            out.writeBytes("    \"params14\",\n");
                            out.writeBytes("    \"params15\",\n");
                            out.writeBytes("    \"params16\",\n");
                            out.writeBytes("    \"params17\",\n");
                            out.writeBytes("    \"params18\",\n");
                            out.writeBytes("    \"params19\",\n");
                            out.writeBytes("    \"params20\",\n");
                            out.writeBytes("    \"params21\",\n");
                            out.writeBytes("    \"params22\",\n");
                            out.writeBytes("    \"params23\",\n");
                            out.writeBytes("    \"params24\",\n");
                            out.writeBytes("    \"params25\"\n");
                            out.writeBytes("  ],\n");
                            out.writeBytes("  \"pValue\": [\n");
                            out.writeBytes(searchString[0]);
                            out.writeBytes(searchString[1]);
                            out.writeBytes(searchString[2]);
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"01\",\n");
                            out.writeBytes("    \"00000\",\n");
                            out.writeBytes(searchString[3]);
                            out.writeBytes(searchString[4]);
                            out.writeBytes(searchString[5]);
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"\",\n");
                            out.writeBytes("    \"0\",\n");
                            out.writeBytes("    \"0\",\n");
                            out.writeBytes("    \"00\",\n");
                            out.writeBytes("    \"000\",\n");
                            out.writeBytes("    \"00\"\n");
                            out.writeBytes("  ]\n");
                            out.writeBytes("}");
                            out.flush();
                            out.close();

                            int status = 0;
                            status = con.getResponseCode();
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer content = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                content.append(inputLine);
                            }
                            in.close();
                            con.disconnect();
                            content.delete(content.indexOf("{"), content.indexOf("{")+1);
                            int c, d;
                            c = content.indexOf("\"");
                            content.delete(c, c+1);
                            d = content.indexOf("\"");
                            content.delete(c, d+1);
                            content.delete(content.indexOf(":"), content.indexOf("[")+1);
                            courseList.clear();
                            while(content.indexOf("{") != -1) {
                                if ((content.indexOf("개설학과코드") != -1) && (content.indexOf("개설학과코드") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[7] = content.substring(a, b);
                                    courseUniversity = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("개설학과명") != -1) && (content.indexOf("개설학과명") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    courseMajor = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("올사이버") != -1) && (content.indexOf("올사이버") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("학년") != -1) && (content.indexOf("학년") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[0] = content.substring(a, b);
                                    courseGrade = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교과구분") != -1) && (content.indexOf("교과구분") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    courseArea = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("원어강의") != -1) && (content.indexOf("원어강의") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교과목번호") != -1) && (content.indexOf("교과목번호") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    courseID = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교과목명") != -1) && (content.indexOf("교과목명") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[1] = content.substring(a, b);
                                    courseTitle = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("분반") != -1) && (content.indexOf("분반") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[2] = content.substring(a, b);
                                    courseDivide = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("학점") != -1) && (content.indexOf("학점") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf(":");
                                    content.delete(a, a + 1);
                                    b = content.indexOf(",");
                                    classinfo[5] = content.substring(a, b);
                                    courseCredit = content.substring(a, b);
                                    content.delete(a, b + 1);
                                }
                                if ((content.indexOf("이론시간") != -1) && (content.indexOf("이론시간") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf(":");
                                    content.delete(a, a + 1);
                                    b = content.indexOf(",");
                                    content.delete(a, b + 1);
                                }
                                if ((content.indexOf("실습시간") != -1) && (content.indexOf("실습시간") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf(":");
                                    content.delete(a, a + 1);
                                    b = content.indexOf(",");
                                    content.delete(a, b + 1);
                                }
                                if ((content.indexOf("시간") != -1) && (content.indexOf("시간") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf(":");
                                    content.delete(a, a + 1);
                                    b = content.indexOf(",");
                                    content.delete(a, b + 1);
                                }
                                if ((content.indexOf("시간강의실") != -1) && (content.indexOf("시간강의실") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[3] = content.substring(a, b);
                                    courseTime = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("제한인원") != -1) && (content.indexOf("제한인원") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[6] = content.substring(a, b);
                                    coursePersonnel = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교수번호") != -1) && (content.indexOf("교수번호") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교수명") != -1) && (content.indexOf("교수명") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    classinfo[4] = content.substring(a, b);
                                    courseProfessor = content.substring(a, b);
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("비고") != -1) && (content.indexOf("비고") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("교과목번호_분반") != -1) && (content.indexOf("교과목번호_분반") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(",") + 1);
                                }
                                if ((content.indexOf("제한사항") != -1) && (content.indexOf("제한사항") < 3)) {
                                    int a, b;
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    a = content.indexOf("\"");
                                    content.delete(a, a + 1);
                                    b = content.indexOf("\"");
                                    content.delete(a, b + 1);
                                    content.delete(content.indexOf(":"), content.indexOf(":") + 1);
                                }
                                content.delete(content.indexOf("{"), content.indexOf("}") + 2);
                                Course course = new Course(courseID, courseUniversity, courseYear, courseTerm, courseArea, courseMajor, courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime, courseRoom);
                                courseList.add(course);
                                count++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            AlertDialog dialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleSearchFragment.this.getActivity());
                            dialog = builder.setMessage("조회된 강의가 없습니다.").setPositiveButton("확인", null).create();
                            dialog.show();
                        }

                        adapter.notifyDataSetChanged();
                        count = 0;
                    }
                },2000);
            }
        });

        return view;
    }
}