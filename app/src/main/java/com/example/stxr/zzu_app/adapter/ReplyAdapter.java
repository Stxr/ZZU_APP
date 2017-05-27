package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.Reply;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;

import java.util.List;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   ReplyAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/5/24 0:28
 *  描述：    
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> implements View.OnClickListener {
    private List<Reply> replies;
    private Context context;
    private OnRecyclerViewItemClickListener clickListener;
    private int parentPosition;


    public ReplyAdapter(Context context, List<Reply> replies,int parentPosition) {
        this.context = context;
        this.replies = replies;
        this.parentPosition = parentPosition;
    }

    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_relpy, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.ViewHolder holder, int position) {
        Reply reply = replies.get(position);
        //设置Tag，在click中取出来
        holder.itemView.setTag(R.id.reply,reply);
        //记录父节点的位置
        holder.itemView.setTag(R.id.replyParentPosition,parentPosition);

        final String name = reply.getName();
        String toName = reply.getToName();
        String content = reply.getContent();
        if (TextUtils.isEmpty(toName)) { //如果回复的人是空
            holder.tv_item_reply.setText(content);
        } else {
            final int start = name.length();
            String text = name + " 回复 " + toName +": "+content;
            SpannableStringBuilder ss = new SpannableStringBuilder(text);
            //富文本 把回复的颜色变灰
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                    0, name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //设置点击事件
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#5A5A5A")),
                    start, start + 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                    start+4, start+4+toName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            ss.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    T.shortShow(context, "点击了" + widget.getId());
//                }
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText(false);
//                    ds.setColor(ds.linkColor);
//                }
//            }, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_item_reply.setText(ss);
            //设置点击事件
            //会与itemClick产生冲突
//            holder.tv_item_reply.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public int getItemCount() {
        if (replies != null) {
            return replies.size();
        }else{
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onItemClick(v, (Reply) v.getTag(R.id.reply));
        }
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Reply reply);
    }
    public void setOnClickListener(OnRecyclerViewItemClickListener l){
        this.clickListener =  l;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_reply;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_reply = (TextView) itemView.findViewById(R.id.tv_item_reply);
        }
    }
}
