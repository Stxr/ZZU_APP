package com.example.stxr.zzu_app.utils;

import android.util.Log;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   L
 *  创建者:   Stxr
 *  创建时间:  2017/4/14 9:38
 *  描述：    日志封装类
 */
public class L {
    private static final boolean DEBUG = true;
    private static final String TAG = "zzu_app";

    public static void i(String string) {
        Log.i(TAG, string);
    }
    public static void w(String string) {
        Log.w(TAG, string);
    }
    public static void d(String string) {
        Log.d(TAG, string);
    }
    public static void e(String string) {
        Log.e(TAG, string);
    }
    public static void wtf(String string) {
        Log.wtf(TAG, string);
    }
}
