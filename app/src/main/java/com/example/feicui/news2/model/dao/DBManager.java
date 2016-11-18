package com.example.feicui.news2.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.feicui.news2.entity.News;
import com.example.feicui.news2.entity.SubType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 2016/6/6.
 */
public class DBManager {

    private Context context;
    private static DBManager dbManager;
    private DBOpenHelper dbHelper;

    private DBManager(Context context){
        this.context = context;
        dbHelper = new DBOpenHelper(context);
    }

    public static DBManager getInstance(Context context){

        if(dbManager==null){
            synchronized (DBManager.class){
                if(dbManager == null){
                    dbManager = new DBManager(context);
                }
            }
        }
        return dbManager;
    }

    /**
     * 添加数据
     */
    public void insertNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", news.getType());
        values.put("nid", news.getNid());
        values.put("stamp", news.getStamp());
        values.put("icon", news.getIcon());
        values.put("title", news.getTitle());
        values.put("summary", news.getSummary());
        values.put("link", news.getLink());
        db.insert("news", null, values);
        db.close();
    }

    //存储新闻数据到 newslove 表中
    public boolean saveLoveNews(News news){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("  select  *  from lovenews where nid=  " + news.getNid(), null);
        if(cursor.moveToFirst()){
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("type", news.getType());
        values.put("nid", news.getNid());
        values.put("stamp", news.getStamp());
        values.put("icon", news.getIcon());
        values.put("title", news.getTitle());
        values.put("summary", news.getSummary());
        values.put("link", news.getLink());
        db.insert("lovenews",null,values);
        db.close();
        return true;
    }

    //查询newslove表中的数据，返回集合
    public ArrayList<News> queryLoveNews(){
        ArrayList<News> data = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from lovenews order by _id desc", null);
        while(cursor.moveToNext()){
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int nid = cursor.getInt(cursor.getColumnIndex("nid"));
            String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            String link = cursor.getString(cursor.getColumnIndex("link"));

            News news = new News(type,nid, title, summary, icon, link, stamp );
            data.add(news);
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean saveNewsType(List<SubType> list){

        for(SubType type : list){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from type where subid = " + type.getSubid(), null);

            if(cursor.moveToFirst()){
                cursor.close();
                return false;
            }

            cursor.close();
            ContentValues values = new ContentValues();
            values.put("subid",type.getSubid());
            values.put("subgroup",type.getSubgroup());
            db.insert("type",null,values);
            db.close();
        }
        return true;

    }

    public List<SubType> queryNewsType(){
        List<SubType> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from type order by _id desc", null);
        while(cursor.moveToNext()){
            int subid = cursor.getInt(cursor.getColumnIndex("subid"));
            String subgroup = cursor.getString(cursor.getColumnIndex("subgroup"));
            SubType type = new SubType(subid, subgroup);
            list.add(type);
        }

        return list;
    }




    public List<News> queryNews() {
        List<News> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from news", null);



        return list;
    }
}
