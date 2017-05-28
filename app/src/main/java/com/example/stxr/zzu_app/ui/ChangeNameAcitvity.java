package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   ChangeNameAcitvity
 *  创建者:   Stxr
 *  创建时间:  2017/5/28 15:16
 *  描述：    
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.stxr.zzu_app.R;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import static com.example.stxr.zzu_app.statics.StaticConstant.SET_NAME_BACK_CODE;
import static com.example.stxr.zzu_app.statics.StaticConstant.SET_SPECIALITY_BACK_CODE;

public class ChangeNameAcitvity extends BaseActivity {
    private EditText et_setName;
    private FloatLabeledEditText fe_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_change_name);
        initView();
        initData();
    }

    private void initData() {
        //显示已有的名字
        if (getIntent() != null) {
            //修改院系
            if (getIntent().getStringExtra("id").equals("changeSpeciality")) {
                getSupportActionBar().setTitle("修改院系信息");
                et_setName.setHint("请输入所属院系");
                fe_set.setHint("请输入所属院系");
                et_setName.setText(getIntent().getStringExtra("speciality"));
            } else {//修改名字
                et_setName.setText(getIntent().getStringExtra("name"));
            }
        }

    }

    private void initView() {
        et_setName = (EditText) findViewById(R.id.et_change_name);
        fe_set = (FloatLabeledEditText) findViewById(R.id.fe_set);
    }

    //显示菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (et_setName.getText() != null) {
                    //修改院系
                    if (getIntent().getStringExtra("id").equals("changeSpeciality")) {
                        Intent intent = new Intent();
                        String speciality = et_setName.getText().toString().trim();
                        intent.putExtra("speciality", speciality);
                        setResult(SET_NAME_BACK_CODE,intent);
                        finish();
                    } else {//修改姓名
                        Intent intent = new Intent();
                        String name = et_setName.getText().toString().trim();
                        intent.putExtra("name", name);
                        setResult(SET_SPECIALITY_BACK_CODE,intent);
                        finish();
                    }

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
