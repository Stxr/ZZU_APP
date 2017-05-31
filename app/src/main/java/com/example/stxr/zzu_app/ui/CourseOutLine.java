package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   CourseOutLine
 *  创建者:   Stxr
 *  创建时间:  2017/5/30 14:07
 *  描述：    离线查询
 */

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CourseAdapter;
import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.utils.L;

import java.util.ArrayList;
import java.util.List;

public class CourseOutLine extends BaseActivity implements View.OnClickListener {
    private Button btn_show_classroom;
    private Button btn_show_clmByLcn;
    private RecyclerView rcv_courseList;
    private Course course;
    private Course course1;
    private List<Course> courseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_outline);
        initView();
        initData();
    }

    private void initData() {
        btn_show_classroom.setOnClickListener(this);
        btn_show_clmByLcn.setOnClickListener(this);
    }

    private void initView() {
        btn_show_clmByLcn = (Button) findViewById(R.id.btn_show_clmByLcn1);
        btn_show_classroom = (Button) findViewById(R.id.btn_show_clm1);
        rcv_courseList = (RecyclerView) findViewById(R.id.rcv_courseList1);
        course = new Course(this);
        course.onCreate();
    }
    private void getCourseData(List<String> l) {
        courseList = new ArrayList<>();
        List<String> list = new ArrayList<>(l);
        L.i(list.toString());
        for (String s : list) {
            course1 = new Course(this);
            course1.setCourse_loacation(s);
            L.i(course1.getCourse_loacation());
            courseList.add(course1);
        }
    }

    private void displayData() {
        rcv_courseList.setLayoutManager(new LinearLayoutManager(this));
        CourseAdapter courseAdapter = new CourseAdapter(this,courseList);
        rcv_courseList.setAdapter(courseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_clm1:
                getCourseData(course.getFreeClassroom(false));//根据位置显示当天有教室的
                displayData();
                break;
            case R.id.btn_show_clmByLcn1:
                getCourseData(course.getFreeClassroomByLocation(false));//根据位置显示当天有教室的
                displayData();
                break;
        }
    }
}
