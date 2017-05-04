package com.example.stxr.zzu_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;

import java.io.File;
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
public class PassageAdapter extends BaseAdapter {
    private List<MyBBS> passage;
    private MyBBS data;
    private Context context;
    private LayoutInflater inflater;
    public PassageAdapter(Context context, List<MyBBS> passage) {
        this.context = context;
        this.passage = passage;
        //获取系统服务
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return passage.size();
    }

    @Override
    public Object getItem(int position) {
        return passage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.passage_item, null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tv_ObjectID = (TextView) convertView.findViewById(R.id.tv_ObjectID);
            viewHolder.civ_face = (CircleImageView) convertView.findViewById(R.id.civ_face);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        data = passage.get(position);
        viewHolder.tv_author.setText(data.getAuthor().getUsername());
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_content.setText(data.getContent());


        MyUser user = passage.get(position).getAuthor();
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
        viewHolder.civ_face.setImageBitmap(bitmap);

        viewHolder.tv_ObjectID.setTag(passage.get(position).getObjectId());
        return convertView;
    }
    class ViewHolder{
        private TextView tv_ObjectID;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_author;
        private CircleImageView civ_face;
    }
}
