package com.example.stxr.zzu_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.service.WidgetService;
import com.example.stxr.zzu_app.utils.DataUtils;

import java.util.Calendar;

import cn.bmob.v3.BmobUser;


/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.widget
 *  文件名:   Awidget
 *  创建者:   Stxr
 *  创建时间:  2017/5/30 19:00
 *  描述：    
 */
public class Awidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "com.stxr.zzu_app.EXTRA_ITEM";
    public static final String TOAST_ACTION = "com.stxr.zzu_app.TOAST_ACTION"; // 点击事件的广播ACTION
    private String[] months={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    private String[] days={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // TODO Auto-generated method stub

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        Time time=new Time();
//        time.setToNow();
//        String month=time.year+" "+months[time.month];
//        remoteViews.setTextViewText(R.id.txtDay, new Integer(time.monthDay).toString());
//        remoteViews.setTextViewText(R.id.txtMonth, month);
//        remoteViews.setTextViewText(R.id.txtWeekDay, days[time.weekDay]);
        String name;
        if (BmobUser.getCurrentUser() != null) {
            name = BmobUser.getCurrentUser().getUsername()+"的课表";
        } else {
            name = "请登录";
        }
        remoteViews.setTextViewText(R.id.tv_widgetName,name);
        remoteViews.setTextViewText(R.id.tv_widgetWeek,days[DataUtils.getWeekday()-1]);
        //设置适配器
        Intent intent = new Intent(context, WidgetService.class);
        remoteViews.setRemoteAdapter(R.id.ll_widgetCourse, intent);
        //当适配器列表为空时，直接显示空
//        remoteViews.setEmptyView(R.id.ll_widgetCourse,);
        //zuowei broadcasdReceiver组件
        Intent toasIntent = new Intent(context, Awidget.class).setAction(TOAST_ACTION);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, toasIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //将pendingIntent 和 listview进行关联
        remoteViews.setPendingIntentTemplate(R.id.ll_widgetCourse, pendingIntent);
//        remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        appWidgetManager.updateAppWidget(new ComponentName(context, Awidget.class), remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        if(intent.getAction().equals(TOAST_ACTION)){
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "点击了第"+viewIndex+"个"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
