package com.example.stxr.zzu_app.application;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.example.stxr.zzu_app.service.LocationService;
import com.example.stxr.zzu_app.statics.StaticConstant;
import com.example.stxr.zzu_app.utils.BaiduMapUtils;

import cn.bmob.v3.Bmob;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.application
 *  文件名:   BaseApplication
 *  创建者:   Stxr
 *  创建时间:  2017/4/17 23:51
 *  描述：    
 */
public class BaseApplication extends Application {
    public BaiduMapUtils baiduMapUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, StaticConstant.BMOB_ID);
        baiduMapUtils = new BaiduMapUtils(getApplicationContext());
        baiduMapUtils.onCreate();
    }
}
