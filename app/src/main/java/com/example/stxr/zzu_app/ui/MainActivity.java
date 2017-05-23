package com.example.stxr.zzu_app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.fragment.ClassFragment;
import com.example.stxr.zzu_app.fragment.ExchangeFragment;
import com.example.stxr.zzu_app.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;


/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   TestActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:58
 *  描述：    
 */
public class MainActivity extends AppCompatActivity {
    private List<String> title;
    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉阴影
//        getSupportActionBar().setElevation(0);
        initData();
        initView();
    }
    private void initData() {
        title = new ArrayList<>();
        fragments = new ArrayList<>();
        title.add(getString(R.string.title1));
        title.add(getString(R.string.title2));
        title.add(getString(R.string.title3));
        fragments.add(new ClassFragment());
        fragments.add(new ExchangeFragment());
        fragments.add(new UserFragment());
    }
    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        viewPager = (ViewPager) findViewById(R.id.mViewPager);
        //预加载
        viewPager.setOffscreenPageLimit(fragments.size());
        //添加适配
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
            //返回标题
            @Override
            public CharSequence getPageTitle(int position) {
                return title.get(position);
            }
        });
        //绑定到一起
        tabLayout.setupWithViewPager(viewPager);
    }

}
