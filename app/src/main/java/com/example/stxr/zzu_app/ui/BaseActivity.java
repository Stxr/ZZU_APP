package com.example.stxr.zzu_app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   BaseActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 23:09
 *  描述：    提供界面的统一样式：显示返回键
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示返回键
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //按了返回键就关掉界面
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
