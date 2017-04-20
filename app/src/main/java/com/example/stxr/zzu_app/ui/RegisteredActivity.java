package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   RegisteredActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/18 22:54
 *  描述：    
 */

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.T;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_user_name;
    private EditText edt_user_password;
    private EditText edt_user_pwd;
    private EditText edt_user_email;
    private RadioGroup rg_sex;
    private Button btn_sign_in;
    private EditText edt_user_desc;
    boolean isGrender;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        edt_user_desc = (EditText) findViewById(R.id.edt_user_desc);
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
                String desc = edt_user_desc.getText().toString().trim();
                String name = edt_user_name.getText().toString().trim();
                String pass = edt_user_password.getText().toString().trim();
                String password = edt_user_pwd.getText().toString().trim();
                String email = edt_user_email.getText().toString().trim();
                //关键信息不能为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(password)) {
                    if (!pass.equals(password)) {
                        T.shortShow(this, "密码不一致");
                    } else {
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
                        user.setDesc(desc);
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
                                    T.shortShow(RegisteredActivity.this, "注册失败" + e.toString());
                                }
                            }
                        });
                    }
                } else {
                    T.shortShow(this, "信息不能为空");
                }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Registered Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
