package com.example.stxr.zzu_app.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.ZZUClass;
import com.example.stxr.zzu_app.ui.SplashActivity;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.widget.Awidget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.service
 *  文件名:   WidgetService
 *  创建者:   Stxr
 *  创建时间:  2017/5/31 19:12
 *  描述：    
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(getApplicationContext(), intent);
    }

    //
    class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private List<String> courseList;
        private ZZUClass zzuClass;
        private List<ZZUClass> zzuClassList;

        WidgetViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            //设置数据

            ZZUClass zzu;
            zzu = zzuClassList.get(position);
            String inteval = "";
            if (zzu.getInterval().contains("单")) {
                inteval = "单周";
            } else if(zzu.getInterval().contains("双")){
                inteval = "双周";
            }
            rv.setTextViewText(R.id.tv_widgetItem_name, zzu.getName());
            rv.setTextViewText(R.id.tv_widgetItem_teacher, "("+zzu.getTeacher()+")");
            rv.setTextViewText(R.id.tv_widgetItem_location, zzu.getLocation());
            rv.setTextViewText(R.id.tv_widgetItem_interval, inteval);
            rv.setTextViewText(R.id.tv_widgetItem_time, DataUtils.transTimetoCourse(zzu.getTimeDay(),zzu.getWeekday()));

            //传递数据
            Intent fillInIntent = new Intent(getApplicationContext(), SplashActivity.class);
            fillInIntent.putExtra(Awidget.EXTRA_ITEM, position);
            rv.setOnClickFillInIntent(R.id.ll_widgetItem, fillInIntent);
            return rv;
        }

        @Override
        public void onCreate() {
            updateData();
        }


        private void addMather(Matcher m) {
            zzuClass = new ZZUClass();
            zzuClass.setId(m.group(1));
            zzuClass.setName(m.group(2));
            zzuClass.setInterval(m.group(5));
            zzuClass.setLocation(m.group(6));
            zzuClass.setTeacher(m.group(7));
            try {
                zzuClass.setStartWeek(Integer.parseInt(m.group(3)));
                zzuClass.setEndWeek(Integer.parseInt(m.group(4)));
                zzuClass.setWeekday(Integer.parseInt(m.group(8)));
                zzuClass.setTimeDay(Integer.parseInt(m.group(9)));
            } catch (NumberFormatException e) {
                T.shortShow(getApplicationContext(), "解析错误" + e.getMessage());
            }
            zzuClassList.add(zzuClass);
        }

        private void updateData() {
            Gson gson = new Gson();
            String test = ShareUtils.getString(getApplicationContext(), "course", null);
            if (test != null) {
                L.e("fromJson" + gson.fromJson(test, new TypeToken<List<String>>() {
                }.getType()).toString());
                courseList = new ArrayList<>((List<String>) gson.fromJson(test, new TypeToken<List<String>>() {
                }.getType()));
                List<String> testList = new ArrayList<>((List<String>) gson.fromJson(test, new TypeToken<List<String>>() {
                }.getType()));

                String pattern = "(\\d+):(.*?)\\[.*?\\]\\s.*?(\\d+)-(\\d+)(\\S*)\\s(\\S*).*?:(\\S*).*?xxx(\\d)(\\d)"; //单周
                Pattern r = Pattern.compile(pattern);
                zzuClassList = new ArrayList<>();
                for (String course : testList) {
                    Matcher m = r.matcher(course);
//                    L.e(course);
                    if (course.contains("--")) { //包含当双周
                        String pattern1 = ".*-- (\\d+):(.*?)\\[.*?\\]\\s.*?(\\d+)-(\\d+)(\\S*)\\s(\\S*).*?:(\\S*).*?xxx(\\d)(\\d)"; //双周
                        Matcher m1 = Pattern.compile(pattern1).matcher(course);
                        if (m1.find()) {
                            addMather(m1);
//                            L.e("****************");
//                            L.e("id:"+m1.group(1));
//                            L.e("name:"+m1.group(2));
//                            L.e("startTime:"+m1.group(3));
//                            L.e("endTime:"+m1.group(4));
//                            L.e("interval:"+m1.group(5));
//                            L.e("location:"+m1.group(6));
//                            L.e("teacher:"+m1.group(7));
//                            L.e("timeWeek:"+m1.group(8));
//                            L.e("timeDay:"+m1.group(9));
//                            L.e("****************");

                        }
                    }
                    if (m.find()) {
                        addMather(m);
//                        L.e("--------------");
//                        L.e("id:"+m.group(1));
//                        L.e("name:"+m.group(2));
//                        L.e("startTime:"+m.group(3));
//                        L.e("endTime:"+m.group(4));
//                        L.e("interval:"+m.group(5));
//                        L.e("location:"+m.group(6));
//                        L.e("teacher:"+m.group(7));
//                        L.e("timeWeek:"+m.group(8));
//                        L.e("timeDay:"+m.group(9));
//                        L.e("--------------");
                    }
                    for (ZZUClass c : zzuClassList) {
                        L.e(c.toString());
                    }
//                    zzuClass.setId(m.group());
                }
            }
        }

        @Override
        public void onDataSetChanged() {
            updateData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (courseList == null) {
                return 0;
            }
            return courseList.size();
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
