package com.example.pnuwalker.schedule;

import android.content.Context;

import com.example.pnuwalker.R;
import com.example.pnuwalker.schedule.AutoResizeTextView;

public class Schedule {
    private String monday[] = new String[25];
    private String tuesday[] = new String[25];
    private String wednesday[] = new String[25];
    private String thursday[] = new String[25];
    private String friday[] = new String[25];
    private int monday2[] = new int[25];
    private int tuesday2[] = new int[25];
    private int wednesday2[] = new int[25];
    private int thursday2[] = new int[25];
    private int friday2[] = new int[25];

    public Schedule() {
        for (int i = 0; i < 25; i++) {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    }

    public void allClear() {
        for(int i = 0; i < 25; i++) {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
            monday2[i] = 0;
            tuesday2[i] = 0;
            wednesday2[i] = 0;
            thursday2[i] = 0;
            friday2[i] = 0;
        }
    }

    public void addSchedule(int day_week, String startScheduleText, String scheduleText, String end_location, String end_location_name, String name, String room, int cyclic) {
        int day = day_week % 10;
        int array;
        StringBuffer sa = new StringBuffer(startScheduleText);
        StringBuffer sb = new StringBuffer(scheduleText);
        String a = "";

        if (sb.indexOf("(") > -1) {
            if (day == 0) {
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                monday[array] = name + "(" + end_location + ")";
                monday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;

                int count = 0;
                while (b > 0) {
                    monday[array] = name;
                    monday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        monday[array] = "(" + end_location + ")";
                        monday2[array] = cyclic;
                        if (room != null) {
                            monday[array] = monday[array];
                            monday2[array] = cyclic;
                        }
                    }
                    else {
                        monday[array] = "1";
                        monday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 1) {
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                tuesday[array] = name + "(" + end_location + ")";
                tuesday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    tuesday[array] = name;
                    tuesday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        tuesday[array] = "(" + end_location;
                        tuesday2[array] = cyclic;
                        if (room != null) {
                            tuesday[array] = tuesday[array];
                            tuesday2[array] = cyclic;
                        }
                    }
                    else {
                        tuesday[array] = "1";
                        tuesday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 2) {
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                wednesday[array] = name + "(" + end_location + ")";
                wednesday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    wednesday[array] = name;
                    wednesday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        wednesday[array] = "(" + end_location;
                        wednesday2[array] = cyclic;
                        if (room != null) {
                            wednesday[array] = wednesday[array];
                            wednesday2[array] = cyclic;
                        }
                    }
                    else {
                        wednesday[array] = "1";
                        wednesday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 3) {
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                thursday[array] = name + "(" + end_location + ")";
                thursday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    thursday[array] = name;
                    thursday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        thursday[array] = "(" + end_location + ")";
                        thursday2[array] = cyclic;
                        if (room != null) {
                            thursday[array] = thursday[array];
                            thursday2[array] = cyclic;
                        }
                    }
                    else {
                        thursday[array] = "1";
                        thursday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 4) {
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                friday[array] = name + "(" + end_location + ")";
                friday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    friday[array] = name;
                    friday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        friday[array] = "(" + end_location + ")";
                        friday2[array] = cyclic;
                        if (room != null) {
                            friday[array] = friday[array];
                            friday2[array] = cyclic;
                        }
                    }
                    else {
                        friday[array] = "1";
                        friday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            day = day_week / 10;

            if (day == 1) {
                sb.delete(0, sb.indexOf(",")+1);
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                monday[array] = name + "(" + end_location + ")";
                monday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    monday[array] = name;
                    monday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        monday[array] = "(" + end_location + ")";
                        monday2[array] = cyclic;
                        if (room != null) {
                            monday[array] = monday[array];
                            monday2[array] = cyclic;
                        }
                    }
                    else {
                        monday[array] = "1";
                        monday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 2) {
                sb.delete(0, sb.indexOf(",")+1);
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                tuesday[array] = name + "(" + end_location + ")";
                tuesday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    tuesday[array] = name;
                    tuesday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        tuesday[array] = "(" + end_location + ")";
                        tuesday2[array] = cyclic;
                        if (room != null) {
                            tuesday[array] = tuesday[array];
                            tuesday2[array] = cyclic;
                        }
                    }
                    else {
                        tuesday[array] = "1";
                        tuesday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 3) {
                sb.delete(0, sb.indexOf(",")+1);
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                wednesday[array] = name + "(" + end_location + ")";
                wednesday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    wednesday[array] = name;
                    wednesday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        wednesday[array] = "(" + end_location + ")";
                        wednesday2[array] = cyclic;
                        if (room != null) {
                            wednesday[array] = wednesday[array];
                            wednesday2[array] = cyclic;
                        }
                    }
                    else {
                        wednesday[array] = "1";
                        wednesday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 4) {
                sb.delete(0, sb.indexOf(",")+1);
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                thursday[array] = name + "(" + end_location + ")";
                thursday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    thursday[array] = name;
                    thursday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        thursday[array] = "(" + end_location + ")";
                        thursday2[array] = cyclic;
                        if (room != null) {
                            thursday[array] = thursday[array];
                            thursday2[array] = cyclic;
                        }
                    }
                    else {
                        thursday[array] = "1";
                        thursday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }

            if (day == 5) {
                sb.delete(0, sb.indexOf(",")+1);
                a = sb.substring(0, 2);
                sb.delete(0, 2);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sb.substring(0, sb.indexOf("("));

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sb.delete(0, sb.indexOf("(")+1);

                friday[array] = name + "(" + end_location + ")";
                friday2[array] = cyclic;

                a = sb.substring(0, sb.indexOf(")"));
                int b = Integer.parseInt(a);

                b = b - 30;
                int count = 0;
                while (b > 0) {
                    friday[array] = name;
                    friday2[array] = cyclic;
                    array = array + 1;
                    if (count == 0) {
                        friday[array] = "(" + end_location + ")";
                        friday2[array] = cyclic;
                        if (room != null) {
                            friday[array] = friday[array];
                            friday2[array] = cyclic;
                        }
                    }
                    else {
                        friday[array] = "1";
                        friday2[array] = cyclic;
                    }
                    count = count + 1;
                    b = b - 30;
                }

                sb.delete(0, sb.indexOf(")")+1);
            }
        }
        else {
            if (day == 0) {
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                if (array < 0) {
                    array = 0;
                }
                else if (array > 24) {
                    array = 24;
                }
                monday[array] = name + "(" + end_location_name + ")";
                monday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                if (array2 < 0) {
                    array2 = 0;
                }
                else if (array2 > 24) {
                    array2 = 24;
                }
                monday[array2] = name + "(" + end_location_name + ")";
                monday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    monday[array] = name;
                    monday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        monday[array2] = "(" + end_location_name + ")";
                        monday2[array2] = cyclic;
                        if (room != null) {
                            monday[array2] = monday[array2];
                            monday2[array2] = cyclic;
                        }
                    }
                    else {
                        monday[array2] = "1";
                        monday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 1) {
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                if (array < 0) {
                    array = 0;
                }
                else if (array > 24) {
                    array = 24;
                }
                tuesday[array] = name + "(" + end_location_name + ")";
                tuesday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                if (array2 < 0) {
                    array2 = 0;
                }
                else if (array2 > 24) {
                    array2 = 24;
                }
                tuesday[array2] = name + "(" + end_location_name + ")";
                tuesday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    tuesday[array] = name;
                    tuesday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        tuesday[array2] = "(" + end_location_name + ")";
                        tuesday2[array2] = cyclic;
                        if (room != null) {
                            tuesday[array2] = tuesday[array2];
                            tuesday2[array2] = cyclic;
                        }
                    }
                    else {
                        tuesday[array2] = "1";
                        tuesday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 2) {
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                if (array < 0) {
                    array = 0;
                }
                else if (array > 24) {
                    array = 24;
                }
                wednesday[array] = name + "(" + end_location_name + ")";
                wednesday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                if (array2 < 0) {
                    array2 = 0;
                }
                else if (array2 > 24) {
                    array2 = 24;
                }
                wednesday[array2] = name + "(" + end_location_name + ")";
                wednesday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    wednesday[array] = name;
                    wednesday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        wednesday[array2] = "(" + end_location_name + ")";
                        wednesday2[array2] = cyclic;
                        if (room != null) {
                            wednesday[array2] = wednesday[array2];
                            wednesday2[array2] = cyclic;
                        }
                    }
                    else {
                        wednesday[array2] = "1";
                        wednesday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 3) {
                System.out.println("sa = " + sa.substring(0, 4));
                System.out.println("sb = " + sb.substring(0, 4));

                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                if (array < 0) {
                    array = 0;
                }
                else if (array > 24) {
                    array = 24;
                }
                thursday[array] = name + "(" + end_location_name + ")";
                thursday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                if (array2 < 0) {
                    array2 = 0;
                }
                else if (array2 > 24) {
                    array2 = 24;
                }

                thursday[array2] = name + "(" + end_location_name + ")";
                thursday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    thursday[array] = name;
                    thursday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        thursday[array2] = "(" + end_location_name + ")";
                        thursday2[array2] = cyclic;
                        if (room != null) {
                            thursday[array2] = thursday[array2];
                            thursday2[array2] = cyclic;
                        }
                    }
                    else {
                        thursday[array2] = "1";
                        thursday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 4) {
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                if (array < 0) {
                    array = 0;
                }
                else if (array > 24) {
                    array = 24;
                }
                friday[array] = name + "(" + end_location_name + ")";
                friday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                if (array2 < 0) {
                    array2 = 0;
                }
                else if (array2 > 24) {
                    array2 = 24;
                }
                friday[array2] = name + "(" + end_location_name + ")";
                friday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    friday[array] = name;
                    friday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        friday[array2] = "(" + end_location_name + ")";
                        friday2[array2] = cyclic;
                        if (room != null) {
                            friday[array2] = friday[array2];
                            friday2[array2] = cyclic;
                        }
                    }
                    else {
                        friday[array2] = "1";
                        friday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            day = day_week / 10;

            if (day == 1) {
                sa.delete(0, sb.indexOf(",")+1);
                sb.delete(0, sb.indexOf(",")+1);
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                monday[array] = name + "(" + end_location_name + ")";
                monday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                monday[array2] = name + "(" + end_location_name + ")";
                monday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    monday[array] = name;
                    monday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        monday[array2] = "(" + end_location_name + ")";
                        monday2[array2] = cyclic;
                        if (room != null) {
                            monday[array2] = monday[array2];
                            monday2[array2] = cyclic;
                        }
                    }
                    else {
                        monday[array2] = "1";
                        monday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 2) {
                sa.delete(0, sb.indexOf(",")+1);
                sb.delete(0, sb.indexOf(",")+1);
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                tuesday[array] = name + "(" + end_location_name + ")";
                tuesday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                tuesday[array2] = name + "(" + end_location_name + ")";
                tuesday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    tuesday[array] = name;
                    tuesday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        tuesday[array2] = "(" + end_location_name + ")";
                        tuesday2[array2] = cyclic;
                        if (room != null) {
                            tuesday[array2] = tuesday[array2];
                            tuesday2[array2] = cyclic;
                        }
                    }
                    else {
                        tuesday[array2] = "1";
                        tuesday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 3) {
                sa.delete(0, sb.indexOf(",")+1);
                sb.delete(0, sb.indexOf(",")+1);
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                wednesday[array] = name + "(" + end_location_name + ")";
                wednesday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                wednesday[array2] = name + "(" + end_location_name + ")";
                wednesday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    wednesday[array] = name;
                    wednesday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        wednesday[array2] = "(" + end_location_name + ")";
                        wednesday2[array2] = cyclic;
                        if (room != null) {
                            wednesday[array2] = wednesday[array2];
                            wednesday2[array2] = cyclic;
                        }
                    }
                    else {
                        wednesday[array2] = "1";
                        wednesday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 4) {
                sa.delete(0, sb.indexOf(",")+1);
                sb.delete(0, sb.indexOf(",")+1);
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                thursday[array] = name + "(" + end_location_name + ")";
                thursday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                thursday[array2] = name + "(" + end_location_name + ")";
                thursday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    thursday[array] = name;
                    thursday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        thursday[array2] = "(" + end_location_name + ")";
                        thursday2[array2] = cyclic;
                        if (room != null) {
                            thursday[array2] = thursday[array2];
                            thursday2[array2] = cyclic;
                        }
                    }
                    else {
                        thursday[array2] = "1";
                        thursday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }

            if (day == 5) {
                sa.delete(0, sb.indexOf(",")+1);
                sb.delete(0, sb.indexOf(",")+1);
                a = sa.substring(0, sa.indexOf("_"));
                sa.delete(0, sa.indexOf("_")+1);
                array = Integer.parseInt(a) - 8;
                array = array * 2;
                a = sa.substring(0, 2);

                if (Integer.parseInt(a) >= 30) {
                    array = array + 1;
                }

                sa.delete(0, 2);

                friday[array] = name + "(" + end_location_name + ")";
                friday2[array] = cyclic;

                int array2 = 0;

                a = sb.substring(0, sb.indexOf("_"));
                sb.delete(0, sb.indexOf("_")+1);
                array2 = Integer.parseInt(a) - 8;
                array2 = array2 * 2;
                a = sb.substring(0, 2);

                if (Integer.parseInt(a) == 0) {
                    array2 = array2 - 1;
                }

                if (Integer.parseInt(a) >= 31) {
                    array2 = array2 + 1;
                }

                friday[array2] = name + "(" + end_location_name + ")";
                friday2[array2] = cyclic;

                int count = 0;
                while (array != array2) {
                    friday[array] = name;
                    friday2[array] = cyclic;
                    if ((count == 0) && ((array) == (array2 - 1))) {
                        friday[array2] = "(" + end_location_name + ")";
                        friday2[array2] = cyclic;
                        if (room != null) {
                            friday[array2] = friday[array2];
                            friday2[array2] = cyclic;
                        }
                    }
                    else {
                        friday[array2] = "1";
                        friday2[array2] = cyclic;
                    }
                    array2 = array2 - 1;
                }

                sb.delete(0, 2);
            }
        }
    }

    public void setting(AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday, AutoResizeTextView[] friday, Context context) {
        int maxLength = 0;
        String maxString = "";

        for (int i = 0; i < 25; i++) {
            if (this.monday[i].length() > maxLength) {
                maxLength = this.monday[i].length();
                maxString = this.monday[i];
            }
            if (this.tuesday[i].length() > maxLength) {
                maxLength = this.tuesday[i].length();
                maxString = this.tuesday[i];
            }
            if (this.wednesday[i].length() > maxLength) {
                maxLength = this.wednesday[i].length();
                maxString = this.wednesday[i];
            }
            if (this.thursday[i].length() > maxLength) {
                maxLength = this.thursday[i].length();
                maxString = this.thursday[i];
            }
            if (this.friday[i].length() > maxLength) {
                maxLength = this.friday[i].length();
                maxString = this.friday[i];
            }
        }

        for (int i = 0; i < 25; i++) {
            if (this.monday2[i] >= 0) {
                if (this.monday[i] != "") {
                    if (this.monday[i] == "1") {
                        monday[i].setText("");
                        monday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.monday2[i] != 1) {
                            monday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                    else {
                        monday[i].setText(this.monday[i]);
                        monday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.monday2[i] != 1) {
                            monday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                }
                else {
                    monday[i].setText(maxString);
                }
                monday[i].resizeText();
            }
        }
        for (int i = 0; i < 25; i++) {
            System.out.println(this.tuesday2[i]);
            if (this.tuesday2[i] >= 0) {
                if (this.tuesday[i] != "") {
                    if (this.tuesday[i] == "1") {
                        tuesday[i].setText("");
                        tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.tuesday2[i] != 1) {
                            tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                    else {
                        tuesday[i].setText(this.tuesday[i]);
                        tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.tuesday2[i] != 1) {
                            tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                }
                else {
                    tuesday[i].setText(maxString);
                }
                tuesday[i].resizeText();
            }
        }
        for (int i = 0; i < 25; i++) {
            if (this.wednesday2[i] >= 0) {
                if (this.wednesday[i] != "") {
                    if (this.wednesday[i] == "1") {
                        wednesday[i].setText("");
                        wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.wednesday2[i] != 1) {
                            wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                    else {
                        wednesday[i].setText(this.wednesday[i]);
                        wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.wednesday2[i] != 1) {
                            wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                }
                else {
                    wednesday[i].setText(maxString);
                }
                wednesday[i].resizeText();
            }
        }
        for (int i = 0; i < 25; i++) {
            if (this.thursday2[i] >= 0) {
                if (this.thursday[i] != "") {
                    if (this.thursday[i] == "1") {
                        thursday[i].setText("");
                        thursday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.thursday2[i] != 1) {
                            thursday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                    else {
                        thursday[i].setText(this.thursday[i]);
                        thursday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.thursday2[i] != 1) {
                            thursday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                }
                else {
                    thursday[i].setText(maxString);
                }
                thursday[i].resizeText();
            }
        }
        for (int i = 0; i < 25; i++) {
            if (this.friday2[i] >= 0) {
                if (this.friday[i] != "") {
                    if (this.friday[i] == "1") {
                        friday[i].setText("");
                        friday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.friday2[i] != 1) {
                            friday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                    else {
                        friday[i].setText(this.friday[i]);
                        friday[i].setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                        if (this.friday2[i] != 1) {
                            friday[i].setBackgroundColor(context.getResources().getColor(R.color.teal_700));
                        }
                    }
                }
                else {
                    friday[i].setText(maxString);
                }
                friday[i].resizeText();
            }
        }
    }
}
