package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragment
 *  文件名:   ShowPassageActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/22 19:49
 *  描述：    
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;

public class ShowPassageActivity extends BaseActivity {
    private TextView tv_showTitle;
    private TextView tv_showContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpassage);
        initView();
        initData();
    }

    private void initData() {
        tv_showTitle.setText(getIntent().getStringExtra("title"));
        tv_showContent.setText(getIntent().getStringExtra("content"));
    }

    private void initView() {
        tv_showContent = (TextView) findViewById(R.id.tv_showContent);
        tv_showTitle = (TextView) findViewById(R.id.tv_showTitle);
    }
}
