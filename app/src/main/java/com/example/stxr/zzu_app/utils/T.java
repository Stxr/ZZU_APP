package com.example.stxr.zzu_app.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   T
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 0:26
 *  描述：    Toast的封装
 */
public class T {
    private  Context context;

    /**
     * 短时间显示
     * @param string
     */
    public static void shortShow(Context context,String string) {
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示
     * @param string
     */
    public static void longShow(Context context,String string) {
        Toast.makeText(context, string,Toast.LENGTH_LONG).show();
    }
}
