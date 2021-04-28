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

    public Schedule() {
        for (int i = 0; i < 25; i++) {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    }

    public void addSchedule(int day_week, String scheduleText, String end_location, String name) {
        int day = day_week % 10;
        int array;
        StringBuffer sb = new StringBuffer(scheduleText);
        String a = "";

        if (day == 1) {
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            monday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                monday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 2) {
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            tuesday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                tuesday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 3) {
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            wednesday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                wednesday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 4) {
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            thursday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                thursday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 5) {
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            friday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                friday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        day = day_week / 10;

        if (day == 1) {
            sb.delete(0, sb.indexOf(",")+1);
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            monday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                monday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 2) {
            sb.delete(0, sb.indexOf(",")+1);
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            tuesday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                tuesday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 3) {
            sb.delete(0, sb.indexOf(",")+1);
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            wednesday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                wednesday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 4) {
            sb.delete(0, sb.indexOf(",")+1);
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            thursday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                thursday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
        }

        if (day == 5) {
            sb.delete(0, sb.indexOf(",")+1);
            a = sb.substring(0, sb.indexOf("_"));
            sb.delete(0, sb.indexOf("_")+1);
            array = Integer.parseInt(a) - 8;
            array = array * 2;
            a = sb.substring(0, sb.indexOf("("));

            if (Integer.parseInt(a) > 30) {
                array = array + 1;
            }

            sb.delete(0, sb.indexOf("(")+1);

            friday[array] = name + "(" + end_location + ")";

            a = sb.substring(0, sb.indexOf(")"));
            int b = Integer.parseInt(a);

            b = b - 30;
            while (b > 0) {
                array = array + 1;
                friday[array] = name + "(" + end_location + ")";
                b = b - 30;
            }

            sb.delete(0, sb.indexOf(")")+1);
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
            if (this.monday[i] != "") {
                monday[i].setText(this.monday[i]);
                monday[i].setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
            }
            else {
                monday[i].setText(maxString);
            }
            monday[i].resizeText();
        }
        for (int i = 0; i < 25; i++) {
            if (this.tuesday[i] != "") {
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
            }
            else {
                tuesday[i].setText(maxString);
            }
            tuesday[i].resizeText();
        }
        for (int i = 0; i < 25; i++) {
            if (this.wednesday[i] != "") {
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
            }
            else {
                wednesday[i].setText(maxString);
            }
            wednesday[i].resizeText();
        }
        for (int i = 0; i < 25; i++) {
            if (this.thursday[i] != "") {
                thursday[i].setText(this.thursday[i]);
                thursday[i].setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
            }
            else {
                thursday[i].setText(maxString);
            }
            thursday[i].resizeText();
        }
        for (int i = 0; i < 25; i++) {
            if (this.friday[i] != "") {
                friday[i].setText(this.friday[i]);
                friday[i].setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
            }
            else {
                friday[i].setText(maxString);
            }
            friday[i].resizeText();
        }
    }
}
