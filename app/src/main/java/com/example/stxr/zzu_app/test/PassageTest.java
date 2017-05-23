package com.example.stxr.zzu_app.test;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.test
 *  文件名:   PassageTest
 *  创建者:   Stxr
 *  创建时间:  2017/5/7 23:10
 *  描述：    
 */
public class PassageTest extends BmobObject {
    private String content;
    private BmobFile pic;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }
}
