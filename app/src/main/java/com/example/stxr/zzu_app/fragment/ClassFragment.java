package com.example.stxr.zzu_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CourseAdapter;
import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.utils.L;

import java.util.ArrayList;
import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   ClassFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:44
 *  描述：    找教室
 */
public class ClassFragment extends Fragment implements View.OnClickListener {
    private Button btn_show_classroom;
    private Button btn_show_clmByLcn;
    private RecyclerView rcv_courseList;
    private Context context;
    private List<Course> courseList;
    private Course course;
    private Course course1;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, null);
        context = view.getContext();
//        this.context = getActivity().getApplicationContext();
        findView();
        return view;
    }

    private void findView() {
        btn_show_classroom = (Button) view.findViewById(R.id.btn_show_clm);
        rcv_courseList = (RecyclerView) view.findViewById(R.id.rcv_courseList);
        btn_show_clmByLcn = (Button) view.findViewById(R.id.btn_show_clmByLcn);
        btn_show_classroom.setOnClickListener(this);
        btn_show_clmByLcn.setOnClickListener(this);
        course = new Course(context);
        course.onCreate();
    }

    private void getCourseData(List<String> l) {
        courseList = new ArrayList<>();
        List<String> list = new ArrayList<>(l);
        L.i(list.toString());
        for (String s : list) {
            course1 = new Course(context);
            course1.setCourse_loacation(s);
            L.i(course1.getCourse_loacation());
            courseList.add(course1);
        }
    }

    private void displayData() {
        rcv_courseList.setLayoutManager(new LinearLayoutManager(context));
        CourseAdapter courseAdapter = new CourseAdapter(context,courseList);
        rcv_courseList.setAdapter(courseAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_clm:
                getCourseData(course.getFreeClassroom(false));//根据位置显示当天有教室的
                displayData();
                break;
            case R.id.btn_show_clmByLcn:
                getCourseData(course.getFreeClassroomByLocation(false));//根据位置显示当天有教室的
                displayData();
                break;
        }
    }
}
