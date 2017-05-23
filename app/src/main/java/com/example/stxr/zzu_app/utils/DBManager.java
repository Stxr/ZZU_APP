package com.example.stxr.zzu_app.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.statics.StaticConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static com.example.stxr.zzu_app.statics.StaticConstant.DB_NAME;
import static com.example.stxr.zzu_app.statics.StaticConstant.PACKAGE_NAME;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   DBManager
 *  创建者:   Stxr
 *  创建时间:  2017/4/14 9:48
 *  描述：    处理数据库的业务逻辑类
 */
public class DBManager {
    private static MySQliteHelper helper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.context = context;
    }

    /**
     * 创建一个SQliteHelper对象
     *
     * @param context 上下文对象
     * @return MySQliteHelper
     */
    public static MySQliteHelper getIntance(Context context) {
        if (helper == null) {
            helper = new MySQliteHelper(context);
        }
        return helper;
    }

    public  void openDatabase() {
        this.database = this.openDatabase(StaticConstant.DB_PATH + "/" + DB_NAME);
    }

    /**
     * 打开数据库，并复制数据库里的数据
     *
     * @param dbfile 文件路径
     * @return SQLiteDatabase
     */
    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) { //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
                File f = new File(StaticConstant.DB_PATH + "/");
                // 如 database 目录不存在，新建该目录
                if (!f.exists()) {
                    f.mkdir();
                }
                InputStream is = this.context.getAssets().open(StaticConstant.DB_NAME);; //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;
        } catch (FileNotFoundException e) {
            L.e("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            L.e("IO exception");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        this.database.close();
    }

    /**
     * 根据sql语句查询获得Cursor对象
     * @param db 数据库对象
     * @param sql 查询的sql语句
     * @param selectionArgs 查询的条件
     * @return Cursor 对象
     */
    public static Cursor selectDataBySql(SQLiteDatabase db, String sql, String[] selectionArgs) {
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, selectionArgs);
        }
        return cursor;
    }

    /**
     * 将cursor游标转化为List对象
     * @param cursor 游标对象
     * @return List对象
     */
    public static List<Course> cursorToList(Cursor cursor) {
        List<Course> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String course_time = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_TIME));
            String course_teacher = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_TEACHER));
            String course_label = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_LABEL));
            String course_loacation =cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_LOACTION));
            int course_students = cursor.getInt(cursor.getColumnIndex(StaticConstant.DB_COURSE_STUDENTS));
            String course_className = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_CLASSNAME));
            String course_name = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_COURSENAME));
            Course course = new Course(course_name,course_teacher,course_students, course_loacation,course_time,course_label,course_className);
            list.add(course);
        }
        return list;
    }

}
