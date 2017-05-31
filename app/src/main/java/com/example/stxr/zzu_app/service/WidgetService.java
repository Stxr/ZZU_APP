package com.example.stxr.zzu_app.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.widget.Awidget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.service
 *  文件名:   WidgetService
 *  创建者:   Stxr
 *  创建时间:  2017/5/31 19:12
 *  描述：    
 */
public class WidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(getApplicationContext(), intent);
    }
    //
    class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context context;
        private List<String> courseList;
        WidgetViewsFactory(Context context, Intent intent) {
            this.context = context;
        }
        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            //设置数据
//            if (courseList.get(position) != "") {
                rv.setTextViewText(R.id.tv_widgetItem, courseList.get(position));
//            }

            //传递数据
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(Awidget.EXTRA_ITEM, position);
            rv.setOnClickFillInIntent(R.id.tv_widgetItem, fillInIntent);
            return rv;
        }

        @Override
        public void onCreate() {
            Gson gson = new Gson();
            String test = ShareUtils.getString(getApplicationContext(), "course", null);
            if (test != null) {
                L.e("fromJson"+gson.fromJson(test,new TypeToken<List<String>>(){}.getType()).toString());
                courseList = new ArrayList<>();
                courseList.addAll((List<String>)gson.fromJson(test,new TypeToken<List<String>>(){}.getType()));
            }
        }

        @Override
        public void onDataSetChanged() {
            Gson gson = new Gson();
            String test = ShareUtils.getString(getApplicationContext(), "course", null);
            if (test != null) {
                L.e("fromJson"+gson.fromJson(test,new TypeToken<List<String>>(){}.getType()).toString());
                courseList = new ArrayList<>();
                courseList.addAll((List<String>)gson.fromJson(test,new TypeToken<List<String>>(){}.getType()));
            }
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
