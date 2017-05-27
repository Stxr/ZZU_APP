package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.ClassroomFree;

import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   CourseOnlineAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/5/27 19:03
 *  描述：    
 */
public class CourseOnlineAdapter extends RecyclerView.Adapter <CourseOnlineAdapter.CourseViewHolder> {
    private Context context;
    private List<ClassroomFree> classroomFreeList;
    private ClassroomFree classroomFree;

    public CourseOnlineAdapter(Context context, List<ClassroomFree> classroomFreeList) {
        this.classroomFreeList = classroomFreeList;
        this.context = context;
    }
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_online, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        classroomFree = classroomFreeList.get(position);
        holder.tv_course_loc.setText(classroomFree.getLoc());
        holder.tv_course_id.setText(classroomFree.getId());
        holder.tv_course_room.setText(classroomFree.getRoom());
    }

    @Override
    public int getItemCount() {
        if (classroomFreeList != null) {
            return classroomFreeList.size();
        }
        return 0;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_course_id;
        private TextView tv_course_room;
        private TextView tv_course_loc;
         public CourseViewHolder(View itemView) {
            super(itemView);
             tv_course_id = (TextView) itemView.findViewById(R.id.tv_course_id);
             tv_course_room = (TextView) itemView.findViewById(R.id.tv_course_room);
             tv_course_loc = (TextView) itemView.findViewById(R.id.tv_course_loc);
         }
    }
}
