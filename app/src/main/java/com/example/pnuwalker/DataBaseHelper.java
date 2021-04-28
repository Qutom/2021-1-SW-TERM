package com.example.pnuwalker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists schedule1("
                + "_id integer primary key autoincrement, "
                + "date text, "
                + "day_week int, "
                + "start_time text, "
                + "end_time text, "
                + "start_location text, "
                + "end_location text, "
                + "end_location_name text, "
                + "name text, "
                + "script text, "
                + "cyclic int, "
                + "tpolyline_x text, "
                + "tpolyline_y text); ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists student1";
        db.execSQL(sql);

        onCreate(db);
    }
}
