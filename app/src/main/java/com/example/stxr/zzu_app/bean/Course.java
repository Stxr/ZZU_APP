package com.example.stxr.zzu_app.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stxr.zzu_app.statics.StaticConstant;
import com.example.stxr.zzu_app.utils.BaiduMapUtils;
import com.example.stxr.zzu_app.utils.DBManager;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.MySQliteHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   Course
 *  创建者:   Stxr
 *  创建时间:  2017/4/14 14:12
 *  描述：    北1_3xx 北四_2xx  北3_5xx常年没课
 */
public class Course {
    //数据库
    private DBManager dbManager;
    private MySQliteHelper helper;
    //获取百度地图的数据
    private BaiduMapUtils baiduMapUtils;
    private Context context;
    private String course_id;//课程号
    private String course_name;//课程名
    private String course_teacher;//上课教师
    private String course_from; //开课院系名
    private int course_start;//开始时间（周）
    private int course_end;//结束时间（周）
    private int course_students;//上课学生人数
    private String course_className;//教学班名称
    private String course_loacation;//教室地点
    private String course_time;//上课时间
    private String course_label;//上课标识（单双周）

    public Course(Context context) {
        this.context = context;
    }
    public Course(String course_name, String course_teacher, int course_students,
                  String course_loacation, String course_time,String course_label,String course_className) {
        this.course_name = course_name;
        this.course_teacher = course_teacher;
        this.course_students = course_students;
        this.course_loacation = course_loacation;
        this.course_time = course_time;
        this.course_label = course_label;
        this.course_className = course_className;
    }

    public void onCreate() {
        dbManager =new DBManager(context);
        baiduMapUtils = new BaiduMapUtils(context);
        //导入数据库
        dbManager.openDatabase();
        dbManager.closeDatabase();
        //百度地图创建
        baiduMapUtils.onCreate();
        helper = DBManager.getIntance(context);
    }

    public void onDestroy() {
        dbManager.closeDatabase();
    }
    @Override
    public String toString() {
        return "课程名:"+this.course_name+" 上课教师:"+ this.course_teacher+
                " 总人数:"+this.course_students+" 教室号:"+this.course_loacation+
                " 时间:"+this.course_time+" 标示:"+this.course_label+
                " 教学班名称:"+this.course_className;
    }

    /**
     * @return 根据地理位置返回空余教室
     */
    public List<String> getFreeClassroomByLocation(boolean flag) {
        List<String> list = new ArrayList<>();
        //遍历百度地图得到的数据 北1 北2 等
        for (String s : baiduMapUtils.getClassLocation()) {
            //将得到的数据加入hashSet中
                list.addAll(getFreeClassroom(s,true));
        }
        if (!flag) {
            list.add("\n----------\n");
            for (String s : baiduMapUtils.getClassLocation()) {
                //将得到的数据加入hashSet中
                list.addAll(getFreeClassroom(s,false));
            }
        }
        return list;
    }

    /**
     *
     * @return 返回当前的没空的教室
     */
    public List<String> getFreeClassroom(){
        return getFreeClassroom("%",true);
    }
    public List<String> getFreeClassroom(boolean flag){
        List<String> list = new ArrayList<>();
        list.addAll(getFreeClassroom("%", true));
        if (!flag){
            list.add("\n----------\n");
            list.addAll(getFreeClassroom("%", false));
        }
        return list;
    }
    public List<String> getFreeClassroom(String classLoaction,boolean flag) {
        LinkedHashSet<String> freeClassroom = new LinkedHashSet<>();
        String selectSql;
        SQLiteDatabase db = helper.getWritableDatabase();
        //筛选
        String classLabel = DataUtils.getClassLabel()+""; //上课的时间，根据当前时间自动获得

        String weekday = DataUtils.getWeekday()+"";//星期几，根据当前时间自动获得

        L.i(weekday + classLabel);
        if(flag){
            selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME
                    + " where (时间 <> '"+weekday+classLabel  // 11 表示星期一第一节课
                    +"') and (教室号 not in  ("+getBusyClassroom(flag) +")"   //去除正在上课的所有教室
                    +") and (教室号   like '"+classLoaction+"%"           //附近的教室
                    +"') and (教室号 not like '0%"       //排除教室为空的情况
                    +"') and (教室号 not like '南区%"   //排除南区
                    +"') and (教室号 not like '北区%')" //排除北区
                    +"order by 教室号";
        }else{ //如果显示false 则得到所有的教室
            selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME
                    + " where (时间 <> '"+weekday+classLabel  // 11 表示星期一第一节课
                    +"') and (教室号 not in  ("+getBusyClassroom(flag) +")"   //去除正在上课的所有教室
                    +") and (教室号   like '"+classLoaction+"%"           //附近的教室
                    +"') and (教室号 not like '0%"       //排除教室为空的情况
                    +"') and (教室号 not like '南区%"   //排除南区
                    +"') and (教室号 not like '北区%')" //排除北区
                    +"order by 教室号";
        }
        Cursor cursor = DBManager.selectDataBySql(db, selectSql, null);
        while (cursor.moveToNext()) {
            String course_time = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_TIME));
            String course_loacation =cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_LOACTION));
            freeClassroom.add(course_loacation);
        }
//        L.i("-------------------");
//        L.i(freeClassroom.toString());
//        L.i("-------------------");
        List<String> list = new ArrayList<>(freeClassroom);
        return list;
    }

    /**
     * 得到有课的教室
     * @param flag true : 找出当天有课的教室 flase：找出当前时间有课的
     * @return
     */
    public String getBusyClassroom(boolean flag){
        String selectSql;
        LinkedHashSet<String> hashSet = new LinkedHashSet<String>();
        StringBuffer s =new StringBuffer();
        SQLiteDatabase db = helper.getWritableDatabase();
        //筛选
        String classLabel = DataUtils.getClassLabel()+""; //上课的时间，根据当前时间自动获得

        String weekday = DataUtils.getWeekday()+"";//星期几，根据当前时间自动获得

        L.i(weekday + classLabel);
        if(flag){ //找出当天有课的
            L.i("找出当天有课的");
            selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME
                    + " where (时间 like '"+weekday+"%"   // 11 表示星期一第一节课
//                +") and (时间   like '"+weekday+"%"           //1表示星期1
                    +"') and (教室号 not like '0%"       //排除教室为空的情况
                    +"') and (教室号 not like '南区%"   //排除南区
                    +"') and (教室号 not like '北区%')" //排除北区
                    +"order by 教室号"
                    ;
        }else{ //找出当前时间段有课的
            L.i("找出当前时间段有课的");
            selectSql = "select * from "+ StaticConstant.DB_COURSE_TABLE_NAME
                    + " where (时间 = '"+weekday+classLabel   // 11 表示星期一第一节课
//                +") and (时间   like '"+weekday+"%"           //1表示星期1
                    +"') and (教室号 not like '0%"       //排除教室为空的情况
                    +"') and (教室号 not like '南区%"   //排除南区
                    +"') and (教室号 not like '北区%')" //排除北区
                    +"order by 教室号"
                    ;
        }
        Cursor cursor = DBManager.selectDataBySql(db, selectSql, null);
        while (cursor.moveToNext()) {
            String course_time = cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_TIME));
            String course_loacation =cursor.getString(cursor.getColumnIndex(StaticConstant.DB_COURSE_LOACTION));
            hashSet.add(course_loacation);
        }
        List<String> list=new ArrayList<String>(hashSet);
        for(int i=0;i<list.size();i++) {
            if (i != list.size() - 1) { //不是最后一个
                s.append("'").append(list.get(i)).append("'").append(",");
            }else{
                s.append("'").append(list.get(i)).append("'");
            }
        }
        L.i(s.toString());
        L.i("+++++++++++++++++++++++++++++");
        return s.toString();
    }
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_teacher() {
        return course_teacher;
    }

    public void setCourse_teacher(String course_teacher) {
        this.course_teacher = course_teacher;
    }

    public String getCourse_from() {
        return course_from;
    }

    public void setCourse_from(String course_from) {
        this.course_from = course_from;
    }

    public int getCourse_start() {
        return course_start;
    }

    public void setCourse_start(int course_start) {
        this.course_start = course_start;
    }

    public int getCourse_end() {
        return course_end;
    }

    public void setCourse_end(int course_end) {
        this.course_end = course_end;
    }

    public int getCourse_students() {
        return course_students;
    }

    public void setCourse_students(int course_students) {
        this.course_students = course_students;
    }

    public String getCourse_className() {
        return course_className;
    }

    public void setCourse_className(String course_className) {
        this.course_className = course_className;
    }

    public String getCourse_loacation() {
        return course_loacation;
    }

    public void setCourse_loacation(String course_loacation) {
        this.course_loacation = course_loacation;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public String getCourse_label() {
        return course_label;
    }

    public void setCourse_label(String course_label) {
        this.course_label = course_label;
    }
}
