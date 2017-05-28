package com.example.stxr.zzu_app.bean;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   MyBBS
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 0:02
 *  描述：    建立一个自己的bbs的数据库
 */
public class MyBBS extends BmobObject {
    private String title;//帖子的标题
    private MyUser author;//发帖的作者
    private String content; //发帖的内容
    private List<BmobFile> picList;
    private Integer visits;

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public List<BmobFile> getPicList() {
        return picList;
    }

    public void setPicList(List<BmobFile> picList) {
        this.picList = picList;
    }
}
