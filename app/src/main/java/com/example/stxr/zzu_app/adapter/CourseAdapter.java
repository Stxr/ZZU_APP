package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.utils.L;

import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   CourseAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 21:49
 *  描述：    
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private Context context;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context =context;
        this.courseList = courseList;
    }
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.course_list, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        holder.textView.setText(courseList.get(position).getCourse_loacation());
//        L.i(courseList.get(position).getFreeClassroomByLocation(true).toString());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public CourseViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_course_list);

        }
    }
}
