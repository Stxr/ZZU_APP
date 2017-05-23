package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   BindActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/25 16:18
 *  描述：    
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.AbsGridAdapter;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BindActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_bind_login;
    private EditText edt_bind_id;
    private EditText edt_bind_password;
    private Spinner sp_year;
    private String year;
    private boolean isSuccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        initView();
        initData();
    }

    private void initData() {
        boolean isBind = ShareUtils.getBoolean(this, "idBind", false);
        //如果之前已经绑定了
        if (isBind) {
            edt_bind_password.setText(ShareUtils.getString(this, "mima", ""));
            edt_bind_id.setText(ShareUtils.getString(this, "xuehao", ""));
            sp_year.setSelection(ShareUtils.getInt(this,"nianji",0));
        }else{
        }
    }

    private void initView() {
        sp_year = (Spinner) findViewById(R.id.sp_year);
        edt_bind_id = (EditText) findViewById(R.id.edt_bind_id);
        edt_bind_password = (EditText) findViewById(R.id.edt_bind_password);
        btn_bind_login = (Button) findViewById(R.id.btn_bind_login);
        btn_bind_login.setOnClickListener(this);
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                T.shortShow(BindActivity.this,parent.getItemAtPosition(position).toString());
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Document doc = Jsoup.parse((String) msg.obj);
            Elements isLogin = doc.select("title");
            if (!isLogin.text().equals("信息")) {//title不是信息说明登录成功
                isSuccess=true;
            }else{
                isSuccess=false;
                ShareUtils.putBoolean(BindActivity.this, "idBind", false);
                T.shortShow(BindActivity.this,"账号密码错误！！！");

            }
        }
    };

    private void login(String id,String password,String nianji) {
        HttpUtils.getInstance();
        HashMap<String, String> key = new HashMap<>();
        key.put("xuehao", id);
        key.put("nianji", nianji);
        key.put("mima", password);
        HttpUtils.doPost("http://jw.zzu.edu.cn/pks/pkisapi2.dll/kbofstu", key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 0;
                byte[] b = response.body().bytes();
                final String info = new String(b, "GB2312");
                msg.obj = info;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录按钮
            case R.id.btn_bind_login:
                L.e(year);
                String id = edt_bind_id.getText().toString().trim();
                String password = edt_bind_password.getText().toString().trim();
                login(id,password,year);
                if (isSuccess) {
                    ShareUtils.putString(BindActivity.this, "mima", password);
                    ShareUtils.putString(BindActivity.this,"year",year);
                    ShareUtils.putInt(BindActivity.this, "nianji", sp_year.getSelectedItemPosition());
                    ShareUtils.putString(BindActivity.this, "xuehao", id);
                    ShareUtils.putBoolean(BindActivity.this, "idBind", true);
                    finish();
                }
                break;

        }
    }
}
