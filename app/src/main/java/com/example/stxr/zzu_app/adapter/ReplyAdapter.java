package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.Reply;

import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   ReplyAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/5/24 0:28
 *  描述：    
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private List<Reply> replies;
    private Context context;


    public ReplyAdapter(Context context, List<Reply> replies) {
        this.context = context;
        this.replies = replies;
    }

    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_relpy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.ViewHolder holder, int position) {
        holder.tv_item_reply.setText(replies.get(position).toString());//设置评论
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_reply;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_reply = (TextView) itemView.findViewById(R.id.tv_item_reply);
        }
    }
}
