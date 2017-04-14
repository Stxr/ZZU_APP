package com.example.stxr.zzu_app.statics;

import android.os.Environment;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   StaticConstant
 *  创建者:   Stxr
 *  创建时间:  2017/4/14 9:33
 *  描述：     一些静态的全局常量
 */
public class StaticConstant {
    //数据库的名字
    public static final String DB_COURSE_NAME = "zzu_course2017.db";
    //数据库版本号
    public static final int DB_COURSE_VERSION = 1;
    // 表名
    public static final String DB_COURSE_TABLE_NAME = "zzuCourse";
    //包名
    public static final String PACKAGE_NAME = "com.example.stxr.zzu_app";
    //保存的数据库文件名
    public static final String DB_NAME = "zzu_course2017.db";
    //在手机里存放数据库的位置
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME + "/databases";
    //课程名称
    public static final String DB_COURSE_COURSENAME = "课程名";
    //id
    public static final String DB_COURSE_ID = "课程号";
    //上课老师
    public static final String DB_COURSE_TEACHER = "上课教师";
    //开课院系名
    public static final String DB_COURSE_FROM = "开课院系名";
    //起始周次
    public static final String DB_COURSE_START = "起始周次";
    //结束周次
    public static final String DB_COURSE_END = "结束周次";
    //总人数
    public static final String DB_COURSE_STUDENTS = "总人数";
    //教学班名称
    public static final String DB_COURSE_CLASSNAME = "教学班名称";
    //教室号
    public static final String DB_COURSE_LOACTION = "教室号";
    //时间
    public static final String DB_COURSE_TIME = "时间";
    //标示
    public static final String DB_COURSE_LABEL = "标示";
}
