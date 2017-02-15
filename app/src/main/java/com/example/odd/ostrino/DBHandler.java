package com.example.odd.ostrino;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Odd on 15.02.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ostdb", OST_TABLE = "ostTable";


    private static final String KEY_ID = "ostid", KEY_TITLE = "title", KEY_SHOW = "show", KEY_TAGS = "tags";

    public DBHandler(Context context){ super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_OST_TABLE = "CREATE TABLE " + OST_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_SHOW + " TEXT,"
                + KEY_TAGS + " TEXT " + ")";
        db.execSQL(CREATE_OST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + OST_TABLE);

        onCreate(db);
    }


}
