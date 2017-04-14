package com.example.stxr.zzu_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.statics.StaticConstant;
import com.example.stxr.zzu_app.utils.DBManager;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.MySQliteHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MySQliteHelper helper;
    private DBManager dbManager;
    private SQLiteDatabase courseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager =new DBManager(this);
        //导入数据库
        dbManager.openDatabase();
        dbManager.closeDatabase();
    }

    public void displayDB(View view) {
       //创建数据库
        helper = DBManager.getIntance(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME + " where 课程名 is "+"医学免疫学";
        Cursor cursor = DBManager.selectDataBySql(db, selectSql, null);
        List<Course> list = DBManager.cursorToList(cursor);
        for (Course c : list) {
            L.i(c.toString());
        }
        dbManager.closeDatabase();
    }
}
