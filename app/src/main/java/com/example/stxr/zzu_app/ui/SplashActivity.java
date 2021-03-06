package com.example.stxr.zzu_app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.statics.StaticConstant;

import static com.example.stxr.zzu_app.statics.StaticConstant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.example.stxr.zzu_app.statics.StaticConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   SplashActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 21:47
 *  描述：     引导界面
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        permission();
        initView();
    }

    private void initView() {
        handler.sendEmptyMessageDelayed(StaticConstant.HANDLE_SPLASH, 500);
    }

    @Override
    public void onBackPressed() {
        //禁止返回键
//        super.onBackPressed();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticConstant.HANDLE_SPLASH:
                    //跳到登录界面
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    //防止按返回键又回到这个界面
                    finish();
                    break;
            }
        }
    };
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case StaticConstant.MY_PERMISSIONS_REQUEST_READ_CONTACTS:
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                break;
//        }
//    }

}
