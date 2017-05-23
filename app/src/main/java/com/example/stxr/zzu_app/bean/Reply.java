package com.example.stxr.zzu_app.bean;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   Reply
 *  创建者:   Stxr
 *  创建时间:  2017/5/24 0:22
 *  描述：    嵌套回复
 */
public class Reply {
    private String content; //回复内容
    private String  name; //回复的名字
    private String toName; //回复对象的名字

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
