package com.example.stxr.zzu_app.utils;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   MySQliteHelper
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 13:22
 *  描述：
 */

import android.content.Context;
import android.content.SharedPreferences;

import cn.bmob.v3.BmobUser;

public class ShareUtils {

    //    public static final String name = "config";
//    public static String name = BmobUser.getCurrentUser() == null ? "config" : BmobUser.getCurrentUser().getObjectId();

    //键 值
    public static void putString(Context mContext, String key, String value) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void putString(String name ,Context mContext, String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    //键 默认值
    public static String getString(Context mContext, String key, String defValue) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static String getString(String name, Context mContext, String key, String defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    //键 值
    public static void putInt(Context mContext, String key, int value) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static void putInt(String name, Context mContext, String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    //键 默认值
    public static int getInt(Context mContext, String key, int defValue) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static int getInt(String name, Context mContext, String key, int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    //键 值
    public static void putBoolean(Context mContext, String key, boolean value) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void putBoolean(String name, Context mContext, String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    //键 默认值
    public static boolean getBoolean(Context mContext, String key, boolean defValue) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static boolean getBoolean(String name, Context mContext, String key, boolean defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    //刪除 单个
    public static void deleShare(Context mContext, String key) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void deleShare(String name, Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    //刪除 全部
    public static void deleAll(Context mContext) {
        String name = BmobUser.getCurrentUser().getObjectId();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public static void deleAll(String name, Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}
