package com.example.stxr.zzu_app.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.fragment.ClassFragment;
import com.example.stxr.zzu_app.fragment.CourseTableFragment;
import com.example.stxr.zzu_app.fragment.ExchangeFragment;
import com.example.stxr.zzu_app.fragment.UserFragment;
import com.example.stxr.zzu_app.statics.StaticConstant;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Date;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   BottomNavigationActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 17:01
 *  描述：    
 */
public class BottomNavigationActivity extends AppCompatActivity implements View.OnClickListener {
//    private Course course;
    private LinearLayout bottom_nav_content;//内容区域
    private BottomNavigationBar bottom_navigation_bar_container;//底部导航栏
    private ClassFragment classFragment;
    private ExchangeFragment exchangeFragment;
    private CourseTableFragment courseTableFragment;
    private UserFragment userFragment;
    private BadgeItem badgeItem;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    //退出的时间
    private long firstTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
//        course = new Course(getApplicationContext());
//        course.onCreate();
        initView();
        initData();
        initBottomNavBar();
        //测试登录
//        testData();
    }

    private void initData() {
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        //让toolbar产生汉堡菜单图标
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

//    private void testData() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        try {
//            Date date1 = sdf.parse("20170428");
//            Date date2 = sdf.parse("20170528");
//            L.e(DataUtils.twoDateDistance(date1,date2));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private void initBottomNavBar() {
        //设置模式
        bottom_navigation_bar_container.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottom_navigation_bar_container.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_navigation_bar_container.setBarBackgroundColor(R.color.colorPrimary);//背景颜色
        bottom_navigation_bar_container.setInActiveColor(R.color.colorPrimaryLight);//未选中时的颜色
        bottom_navigation_bar_container.setActiveColor(R.color.colorPrimaryDark);//选中时的颜色
//        badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setHideOnSelect(true);//角标
        bottom_navigation_bar_container
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_white_24dp,R.string.title1))
                .addItem(new BottomNavigationItem(R.drawable.ic_question_answer_white_24dp,R.string.title2))
                .addItem(new BottomNavigationItem(R.drawable.ic_contact_mail_black_24dp,getString(R.string.title4)))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_white_24dp,R.string.title3))
                .initialise();
        bottom_navigation_bar_container.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                hideAllFrag();
                switch (position) {
                    case 0:
                        fab.setVisibility(View.GONE);
                        if (classFragment == null) {
                            classFragment = new ClassFragment();
                        }
                        addFrag(classFragment);
                        getSupportFragmentManager().beginTransaction().show(classFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title1));
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        if (exchangeFragment == null) {
                            exchangeFragment = new ExchangeFragment();
                        }
                        addFrag(exchangeFragment);
                        getSupportFragmentManager().beginTransaction().show(exchangeFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title2));
                        break;
                    case 2:
                    fab.setVisibility(View.GONE);
                    if (courseTableFragment == null) {
                        courseTableFragment = new CourseTableFragment();
                    }
                    addFrag(courseTableFragment);
                    getSupportFragmentManager().beginTransaction().show(courseTableFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title4));
                    break;
                    case 3:
                        fab.setVisibility(View.GONE);
                        if (userFragment == null) {
                            userFragment = new UserFragment();
                        }
                        addFrag(userFragment);
                        getSupportFragmentManager().beginTransaction().show(userFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title3));
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中

            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中

            }
        });
        setDefaultFrag();
    }

    /*设置默认Fragment*/
    private void setDefaultFrag() {
        if (classFragment == null) {
            classFragment = new ClassFragment();
        }
        addFrag(classFragment);
        /*默认显示msgFrag*/
        getSupportFragmentManager().beginTransaction().show(classFragment).commit();
    }

    /*添加Frag*/
    private void addFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && !frag.isAdded()) {
            ft.add(R.id.bottom_nav_content, frag);
        }
        ft.commit();
    }

    /*隐藏所有fragment*/
    private void hideAllFrag() {
        hideFrag(classFragment);
        hideFrag(exchangeFragment);
        hideFrag(userFragment);
        hideFrag(courseTableFragment);
    }

    /*隐藏frag*/
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
        }
        ft.commit();
    }
    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        bottom_nav_content = (LinearLayout) findViewById(R.id.bottom_nav_content);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottom_navigation_bar_container = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this,PassageActivity.class),1);
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {   //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
