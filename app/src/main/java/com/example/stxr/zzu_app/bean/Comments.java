package com.example.stxr.zzu_app.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   Commits
 *  创建者:   Stxr
 *  创建时间:  2017/4/27 1:10
 *  描述：    
 */
public class Comments extends BmobObject{
    //评论的内容
    private String contents;
    //评论帖子的作者
    private MyUser author;
    //评论的帖子
    private MyBBS post;

    private BmobFile img;

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public MyBBS getPost() {
        return post;
    }

    public void setPost(MyBBS post) {
        this.post = post;
    }
}
