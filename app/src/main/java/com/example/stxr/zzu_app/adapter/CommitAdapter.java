package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.Comments;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.utils.UtilTools;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.stxr.zzu_app.statics.StaticConstant.BMOB_IAMGE_CACHE;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.adapter
 *  文件名:   CommitAdapter
 *  创建者:   Stxr
 *  创建时间:  2017/4/27 9:22
 *  描述：    
 */
public class CommitAdapter extends RecyclerView.Adapter<CommitAdapter.CommitViewHolder>{
    private Context context;
    private List<Comments> commentsList;

    public CommitAdapter(Context context, List<Comments> commentsList) {
        this.commentsList = commentsList;
        this.context = context;
    }
    @Override
    public CommitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommitViewHolder(LayoutInflater.from(context).inflate(R.layout.item_commit, parent, false));
    }

    @Override
    public void onBindViewHolder(CommitViewHolder holder, int position) {
        holder.tv_name.setText(commentsList.get(position).getAuthor().getUsername());
        holder.tv_content.setText(commentsList.get(position).getContents());
        MyUser user = commentsList.get(position).getAuthor();
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
        holder.civ_user_image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        if(commentsList!=null){
            return commentsList.size();
        }else{
            return 0;
        }
    }

    public class CommitViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_content;
        private TextView tv_name;
        private CircleImageView civ_user_image;
        public CommitViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_commit_content);
            tv_name = (TextView) itemView.findViewById(R.id.tv_commit_name);
            civ_user_image = (CircleImageView) itemView.findViewById(R.id.civ_user_image);

        }
    }
}
