package com.example.stxr.zzu_app.bean;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   ZZUClass
 *  创建者:   Stxr
 *  创建时间:  2017/6/1 0:46
 *  描述：    解析课程表数据
 */
public class ZZUClass {
    private String id;
    private String name;
    private String teacher;
    private int timeDay;
    private int weekday;
    private String interval;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ZZUClass{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", timeDay=" + timeDay +
                ", weekday=" + weekday +
                ", interval='" + interval + '\'' +
                ", startWeek=" + startWeek +
                ", location='" + location + '\'' +
                ", endWeek=" + endWeek +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(int timeDay) {
        this.timeDay = timeDay;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    private int startWeek;
    private String location;
    private int endWeek;
}
