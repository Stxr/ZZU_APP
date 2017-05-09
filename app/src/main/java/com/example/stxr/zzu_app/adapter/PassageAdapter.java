package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.DataUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.stxr.zzu_app.statics.StaticConstant.BMOB_IAMGE_CACHE;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   PassageAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/4/20 23:10
 *  描述：    
 */
public class PassageAdapter extends RecyclerView.Adapter<PassageAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{
    private List<MyBBS> myBBSList;
    private MyBBS myBBS;
    private Context context;
    private OnRecyclerViewItemClickListener onItemClickListener;
    private OnRecyclerViewItemLongClickListener onItemLongClickListener;

    /**
     * 构造函数为外部提供接口
     * @param context
     * @param myBBSList
     */
    public PassageAdapter(Context context, List<MyBBS> myBBSList) {
        this.context = context;
        this.myBBSList = myBBSList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passage_item,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassageAdapter.ViewHolder holder, int position) {
        myBBS = myBBSList.get(position);
        //数据保存？
        holder.itemView.setTag(myBBS);

        holder.tv_author.setText(myBBS.getAuthor().getUsername());
//        holder.tv_createdTime.setText(myBBS.getCreatedAt());
        holder.tv_title.setText(myBBS.getTitle());
        holder.tv_visit.setText("浏览 "+myBBS.getVisits()+" 次");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(myBBS.getCreatedAt());
            holder.tv_createdTime.setText(DataUtils.getFriendlyTime(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyUser user = myBBS.getAuthor();
        String path = BMOB_IAMGE_CACHE + user.getObjectId()+".jpg";
        File f= new File(path);
        if(!f.exists()){
            BmobFile img = new BmobFile(user.getObjectId() + ".jpg",null, user.getImg().getUrl());
            final File savefile = new File(BMOB_IAMGE_CACHE, img.getFilename());
            img.download(savefile, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        //T.shortShow(getActivity(), "下载成功，保存路径：" + s);
//                        UtilTools.getImageFromSD(context,user.getObjectId()+".jpg", imageView);
                    } else {
//                        T.shortShow(context, "下载失败：" + e.getMessage());
                    }
                }
                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.civ_face.setImageBitmap(bitmap);
        holder.tv_ObjectID.setTag(myBBS.getObjectId());
    }
    @Override
    public int getItemCount() {
        return myBBSList.size();
    }

    //提供外部接口
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //取出数据
            onItemClickListener.onItemClick(v, (MyBBS) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(v, (MyBBS) v.getTag());
        }
        return true;
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , MyBBS myBBS);
    }
    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view , MyBBS myBBS);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ObjectID;
        private TextView tv_title;
        private TextView tv_visit;
        private TextView tv_author;
        private CircleImageView civ_face;
        private TextView tv_createdTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_visit = (TextView) itemView.findViewById(R.id.tv_visit);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_ObjectID = (TextView) itemView.findViewById(R.id.tv_ObjectID);
            civ_face = (CircleImageView) itemView.findViewById(R.id.civ_face);
            tv_createdTime = (TextView) itemView.findViewById(R.id.tv_createdTime);
            tv_ObjectID = (TextView) itemView.findViewById(R.id.tv_ObjectID);
        }
    }
}

//public class PassageAdapter extends BaseAdapter {
//    private List<MyBBS> passage;
//    private MyBBS myBBS;
//    private Context context;
//    private LayoutInflater inflater;
//    public PassageAdapter(Context context, List<MyBBS> passage) {
//        this.context = context;
//        this.passage = passage;
//        //获取系统服务
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//    @Override
//    public int getCount() {
//        return passage.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return passage.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.passage_item, null);
//            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
//            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
//            viewHolder.tv_ObjectID = (TextView) convertView.findViewById(R.id.tv_ObjectID);
//            viewHolder.civ_face = (CircleImageView) convertView.findViewById(R.id.civ_face);
//            viewHolder.tv_createdTime = (TextView) convertView.findViewById(R.id.tv_createdTime);
//            //设置缓存
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        //设置数据
//        myBBS = passage.get(position);
//        viewHolder.tv_author.setText(myBBS.getAuthor().getUsername());
//        viewHolder.tv_title.setText(myBBS.getTitle());
//        viewHolder.tv_content.setText(myBBS.getContent());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = sdf.parse(myBBS.getCreatedAt());
//            viewHolder.tv_createdTime.setText(DataUtils.getFriendlyTime(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        MyUser user = passage.get(position).getAuthor();
//        String path = BMOB_IAMGE_CACHE + user.getObjectId()+".jpg";
//        File f= new File(path);
//        if(!f.exists()){
//            BmobFile img = new BmobFile(user.getObjectId() + ".jpg",null, user.getImg().getUrl());
//            final File savefile = new File(BMOB_IAMGE_CACHE, img.getFilename());
//            img.download(savefile, new DownloadFileListener() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null) {
//                        //T.shortShow(getActivity(), "下载成功，保存路径：" + s);
////                        UtilTools.getImageFromSD(context,user.getObjectId()+".jpg", imageView);
//                    } else {
////                        T.shortShow(context, "下载失败：" + e.getMessage());
//                    }
//                }
//                @Override
//                public void onProgress(Integer integer, long l) {
//
//                }
//            });
//        }
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        viewHolder.civ_face.setImageBitmap(bitmap);
//
//        viewHolder.tv_ObjectID.setTag(passage.get(position).getObjectId());
//        return convertView;
//    }
//    class ViewHolder{
//        private TextView tv_ObjectID;
//        private TextView tv_title;
//        private TextView tv_content;
//        private TextView tv_author;
//        private CircleImageView civ_face;
//        private TextView tv_createdTime;
//    }
//}
