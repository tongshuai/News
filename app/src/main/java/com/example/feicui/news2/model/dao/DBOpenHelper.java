package com.example.feicui.news2.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 2016/6/6.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "newsdb.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table news (_id integer primary key autoincrement,nid integer,stamp text,title text,summary text,icon text,link text,type integer)");
        db.execSQL("create table lovenews (_id integer primary key autoincrement,nid integer,stamp text,title text,summary text,icon text,link text,type integer)");
        db.execSQL("create table type (_id integer primary key autoincrement,subid integer,subgroup text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
