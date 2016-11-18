package com.example.feicui.news2.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/19.
 */

public class LogUtil {

    public static final String TAG = "新闻随意看";
    public static boolean isDebug = true;

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }
    public static void d(String msg){
        if(isDebug)
            Log.d(LogUtil.TAG,msg);
    }
}
