package com.example.frank.jinding.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * PROJECT_NAME:SZJDSpecialDeviceExam
 * PACKAGE_NAME:com.example.frank.jinding.Utils
 * USER:Frank
 * DATE:2018/6/5
 * TIME:0:41
 * DAY_NAME_FULL:星期二
 * DESCRIPTION:On the description and function of the document
 **/

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "itcast.db";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS filedownlog");
        onCreate(db);
    }

}
