package com.example.stxr.zzu_app.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   MyLinearLayoutManager
 *  创建者:   Stxr
 *  创建时间:  2017/4/27 20:46
 *  描述：    
 */
public class MyLinearLayoutManager extends LinearLayoutManager {

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View view = recycler.getViewForPosition(0);
        measureChild(view, widthSpec, heightSpec);
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int measuredHeight = view.getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
