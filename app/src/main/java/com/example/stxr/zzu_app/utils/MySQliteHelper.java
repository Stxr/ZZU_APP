package com.example.stxr.zzu_app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.stxr.zzu_app.statics.StaticConstant;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   MySQliteHelper
 *  创建者:   Stxr
 *  创建时间:  2017/4/14 9:31
 *  描述：    
 */
public class MySQliteHelper extends SQLiteOpenHelper {
    public MySQliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQliteHelper(Context context) {
        super(context, StaticConstant.DB_NAME, null, StaticConstant.DB_COURSE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        L.i("---------------------数据库创建了-----------------");
//        String sql = "create table if not exists "+StaticConstant.DB_COURSE_TABLE_NAME+"("+StaticConstant.DB_COURSE_ID+
//                " Integer primary key,"+StaticConstant.DB_COURSE_COURSENAME +" varchar(15),"+
//                StaticConstant.DB_COURSE_TEACHER +" varchar(10)"+")";
//        db.execSQL(sql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        L.i("---------------------数据库打开了-----------------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
