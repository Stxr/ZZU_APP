package com.example.stxr.zzu_app.utils;

import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
 *  项目名：  ZZU_Appk
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   DataUtils
 *  创建者:   Stxrk
 *  创建时间:  2017/4/15 13:21
 *  描述：    
 */
public class DataUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getSysTime() {
        Date date = new Date();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
        return sdf.format(date);
    }

    /**
     * @return 返回小时5
     *
     */
    public static String getHour() {
        Date date = new Date();
        sdf = new SimpleDateFormat("HH");
        return sdf.format(date);
    }

    /**
     * @return 返回分钟
     */
    public static String getMinutes() {
        Date date = new Date();
        sdf = new SimpleDateFormat("mm");
        return sdf.format(date);
    }

    /**
     * 得到星期几
     *
     * @return 1-7分别代表周一到周日 0代表错误
     */
    public static int getWeekday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        L.i(calendar.get(Calendar.DAY_OF_WEEK) + "");
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return 7;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            default:
                return 0;
        }
    }
    /**
     * 以友好的方式显示时间
     * @param time
     * @return
     */
    public static String getFriendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);
        if(ct == 0) {
            return "刚刚";
        }
        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }
        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

    /**
     * 得到日期 几月几号
     *
     * @return
     */
    public static String getDay() {
        Date date = new Date();
        sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    /**
     * 得到年份
     *
     * @return
     */
    public static String getYear() {
        Date date = new Date();
        sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 根据当前时间返回上课的时间标号
     *
     * @return 1表示第一节课 2表示第二节课 0表示没课
     */
    public static String getClassLabel() {
        String time = getHour() + getMinutes();
//        Integer h = Integer.parseInt(getHour());
//        Integer m = Integer.parseInt(getMinutes());
//        double time = h+ m.doubleValue()/60;
        //L.i("time=" + time );
        int t = Integer.parseInt(time);
        // L.i("t=" + time );
        if (t >= 600 && t <= 940) {
            return 1 + ""; //第一节课
        } else if (t > 940 && t <= 1140) {
            return 2 + "";//第二节课
        } else if (t > 1140 && t <= 1540) {
            return 3 + "";//第三节课
        } else if (t > 1540 && t <= 1750) {
            return 4 + "";//第四节课
        } else if (t > 1750 && t <= 2040) {
            return 5 + "";//第五节课
        } else {
            return "%";//没有课
        }
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static String twoDateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        L.e("time   " + timeLong);
        timeLong = timeLong / 1000 / 60 / 60 / 24;
        return timeLong + "";
//        if (timeLong<60*1000)
//            return timeLong/1000 + "秒前";
//        else if (timeLong<60*60*1000){
//            timeLong = timeLong/1000 /60;
//            return timeLong + "分钟前";
//        }
//        else if (timeLong<60*60*24*1000){
//            timeLong = timeLong/60/60/1000;
//            return timeLong+"小时前";
//        }
//        else if (timeLong<60*60*24*1000*7){
//            timeLong = timeLong/1000/ 60 / 60 / 24;
//            return timeLong + "天前";
//        }
//        else if (timeLong<60*60*24*1000*7*4){
//            timeLong = timeLong/1000/ 60 / 60 / 24/7;
//            return timeLong + "周前";
//        }
//        else {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//            return sdf.format(startDate);
//        }
    }
}
