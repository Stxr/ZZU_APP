package com.example.stxr.zzu_app.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.bean
 *  文件名:   MyUser
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 21:44
 *  描述：    
 */
public class MyUser extends BmobUser {
    private int age;
    private boolean sex;
    private String desc;
    private BmobFile img;
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }
}