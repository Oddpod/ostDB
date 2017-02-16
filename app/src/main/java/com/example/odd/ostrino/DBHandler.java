package com.example.odd.ostrino;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    void addNewOst(Ost newOst){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, newOst.getTitle());
        values.put(KEY_SHOW, newOst.getShow());
        values.put(KEY_TAGS, newOst.getTags());

        //inserting Row
        db.insert(OST_TABLE, null, values);
        db.close();
        System.out.println("row inserted");

    }

    public boolean deleteOst(int delID){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(OST_TABLE, KEY_ID + "=" + delID, null) > 0;
    }

    public List<Ost> getAllOsts(){

        List<Ost> ostList = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_ID + "," + KEY_TITLE + "," + KEY_SHOW + "," + KEY_TAGS + " FROM " + OST_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{

                Ost ost = new Ost();
                ost.setId(cursor.getInt(0));
                ost.setTitle(cursor.getString(1));
                ost.setShow(cursor.getString(2));
                ost.setTags(cursor.getString(3));

                ostList.add(ost);

            }while(cursor.moveToNext());
        }

        return ostList;
    }


}
