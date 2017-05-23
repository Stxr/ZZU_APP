package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   ForgetActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/23 19:01
 *  描述：    
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_forget_password;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    //初始化View
    private void initView() {
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);
        et_email = (EditText) findViewById(R.id.et_email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_password:
                //1.获取输入框的邮箱
                final String email = et_email.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(email)) {
                    //3.发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ForgetActivity.this,
                                        getString(R.string.text_email_send_ok) + email, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetActivity.this,
                                        R.string.text_email_send_no, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
