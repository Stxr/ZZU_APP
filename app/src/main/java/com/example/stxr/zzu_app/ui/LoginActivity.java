package com.example.stxr.zzu_app.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.view.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.BmobUser.getCurrentUser;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   LoginActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 22:17
 *  描述：    登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_forget;
    private EditText edt_user_name;
    private EditText edt_password;
    private CheckBox cb_isRemember;//记住密码复选框
    private boolean isChecked;//是否记住密码标志位
    private Button btn_sign_up;//注册按钮
    private Button btn_sign_in;//登录按钮
    private CustomDialog dialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        edt_password = (EditText) findViewById(R.id.edt_user_password);
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        cb_isRemember = (CheckBox) findViewById(R.id.checkBox_remember);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        dialog.setCancelable(false);
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        //如果登录了，则直接登录
        if (BmobUser.getCurrentUser() != null) {
            startActivity(new Intent(this,BottomNavigationActivity.class));
            finish();
        }

        isChecked = ShareUtils.getBoolean("config",this, "keeppass", false);
        cb_isRemember.setChecked(isChecked);
        if (cb_isRemember.isChecked()) {
            String user_name = ShareUtils.getString("config",this, "user_name", "");//不能返回null，会引发异常
            String user_password = ShareUtils.getString("config",this, "user_password", "");
            edt_password.setText(user_password);
            edt_user_name.setText(user_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                dialog.show();
                String user_name = edt_user_name.getText().toString().trim();
                String user_password = edt_password.getText().toString().trim();
                final MyUser user = new MyUser();
                user.setUsername(user_name);
                user.setPassword(user_password);
                user.login(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            dialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
                            finish();
                        } else {
                            T.shortShow(LoginActivity.this,"登录失败"+e.toString());
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(this,RegisteredActivity.class));
                break;
            //忘记密码
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean("config",this,"keeppass",cb_isRemember.isChecked());
        if (cb_isRemember.isChecked()) {
            ShareUtils.putString("config",this,"user_name",edt_user_name.getText().toString().trim());
            ShareUtils.putString("config",this,"user_password",edt_password.getText().toString().trim());
        }else{//不勾选保存则忘记密码
            ShareUtils.deleShare("config",this,"user_name");
            ShareUtils.deleShare("config",this,"user_password");
        }
    }
}
