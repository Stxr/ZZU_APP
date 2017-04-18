package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   RegisteredActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 22:54
 *  描述：    
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.T;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_user_name;
    private EditText edt_user_password;
    private EditText edt_user_pwd;
    private EditText edt_user_email;
    private RadioGroup rg_sex;
    private Button btn_sign_in;
    boolean isGrender;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        initView();
    }

    private void initView() {
        edt_user_name = (EditText) findViewById(R.id.edt_username);
        edt_user_password = (EditText) findViewById(R.id.edt_password);
        edt_user_pwd = (EditText) findViewById(R.id.edt_password_confirm);
        edt_user_email = (EditText) findViewById(R.id.edt_email);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        btn_sign_in = (Button) findViewById(R.id.button_register);
        btn_sign_in.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                String name = edt_user_name.getText().toString().trim();
                String pass = edt_user_password.getText().toString().trim();
                String password = edt_user_pwd.getText().toString().trim();
                String email = edt_user_email.getText().toString().trim();
                //关键信息不能为空
                if(!TextUtils.isEmpty(name)  &&!TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(pass)&& !TextUtils.isEmpty(password)){
                    if (!pass.equals(password)) {
                        T.shortShow(this, "密码不一致");
                    }else{
                        //判断男女
                        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.rb_man:
                                        isGrender = true;
                                        break;
                                    case R.id.rb_male:
                                        isGrender = false;
                                }
                            }
                        });
                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.signUp(new SaveListener<Object>() {
                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    T.shortShow(RegisteredActivity.this, "注册成功");
                                    finish();
                                } else {
                                    T.shortShow(RegisteredActivity.this,"注册失败"+ e.toString());
                                }
                            }
                        });
                    }
                }else {
                    T.shortShow(this, "信息不能为空");
                }
        }
    }
}
