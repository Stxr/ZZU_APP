package com.example.stxr.zzu_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.statics.StaticConstant;
import com.example.stxr.zzu_app.utils.BaiduMapUtils;
import com.example.stxr.zzu_app.utils.DBManager;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.MySQliteHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static TextView text;
    private MySQliteHelper helper;
    private DBManager dbManager;
    private BaiduMapUtils baiduMapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        dbManager =new DBManager(this);
        //导入数据库
        dbManager.openDatabase();
        dbManager.closeDatabase();
        baiduMapUtils = new BaiduMapUtils();
        baiduMapUtils.onCreate(this);

//        //初始化gps获取
//        LocationUtils.initLocation(this);
//        L.i(DataUtils.getSysTime());
//        L.i(DataUtils.getYear());
//        L.i(DataUtils.getDay());
//        L.i(DataUtils.getHour());
//        L.i(DataUtils.getWeekday());
//        LocationUtils.initLocation(this);
//        //获取经纬度
//        L.i("经度："+LocationUtils.longitude);
//        L.i("纬度："+LocationUtils.latitude);
    }
    public void displayDB(View view) {
        switch (view.getId()) {
            case R.id.btn_course:
                //创建数据库
                helper = DBManager.getIntance(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                String selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME + " where (时间 <> '11') and (教室号 like '%北4%') and (时间 like '1%')";
                Cursor cursor = DBManager.selectDataBySql(db, selectSql, null);
                List<Course> list = DBManager.cursorToList(cursor);
                for (Course c : list) {
                    L.i(c.toString());
                }
                dbManager.closeDatabase();
                break;
            case R.id.btn_gps:
                L.i(DataUtils.getClassLabel() + "");
                //text.setText(baiduMapUtils.getLocation());
                       //获取经纬度
//                text.setText(LocationUtils.locationInfo);
//                L.i(LocationUtils.locationInfo);
                break;

        }

    }
}
