package com.example.stxr.zzu_app.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.ui.BindActivity;
import com.example.stxr.zzu_app.ui.LoginActivity;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   UserFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:53
 *  描述：    个人设置
 */
public class UserFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private EditText edt_sex;
    private EditText edt_desc;
    private EditText edt_name;
    private Button btn_exit;
    private Button btn_commit;
    private View view;
    private Drawable edt_defaultBK;
    private Button btn_bind;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.franment_user, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        setEnable(false);
        boolean isBind = ShareUtils.getBoolean(getActivity(), "idBind", false);
        if(isBind){
            btn_bind.setText("更改绑定");
        }else {
            btn_bind.setText("绑定账号");
        }
        //显示信息
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        edt_name.setText(userInfo.getUsername());
        edt_desc.setText(userInfo.getDesc());
        edt_sex.setText(userInfo.isSex()?"男":"女");
    }

    private void initView() {
        btn_bind = (Button) view.findViewById(R.id.btn_bind);
        btn_commit = (Button) view.findViewById(R.id.btn_commit);
        edt_desc = (EditText) view.findViewById(R.id.et_desc);
        edt_sex = (EditText) view.findViewById(R.id.et_sex);
        edt_name = (EditText) view.findViewById(R.id.edit_user);
        btn_exit = (Button) view.findViewById(R.id.btn_exit_user);
        edt_defaultBK= edt_desc.getBackground();
        btn_exit.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        edt_desc.setOnLongClickListener(this);
        edt_name.setOnLongClickListener(this);
        edt_sex.setOnLongClickListener(this);
        btn_bind.setOnClickListener(this);
    }

    private void setEnable(boolean is) {
        edt_desc.setFocusableInTouchMode(is);
        edt_sex.setFocusableInTouchMode(is);
        edt_name.setFocusableInTouchMode(is);
        edt_desc.setBackground(null);
        edt_name.setBackground(null);
        edt_sex.setBackground(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                BmobUser.logOut();   //清除缓存用户对象
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_commit:
                MyUser user = new MyUser();
                String name = edt_name.getText().toString().trim();
                String desc = edt_desc.getText().toString().trim();
                String sex = edt_sex.getText().toString().trim();
                if (sex.equals("男")) {
                    user.setSex(true);
                } else {
                    user.setSex(false);
                }
                user.setDesc(desc);
                user.setUsername(name);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //修改成功
                            setEnable(false);
                            btn_commit.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //绑定校园网账号
            case R.id.btn_bind:
                startActivity(new Intent(getActivity(),BindActivity.class));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isBind = ShareUtils.getBoolean(getActivity(), "idBind", false);
        if(isBind){
            btn_bind.setText("更改绑定");
        }else {
            ShareUtils.putString(getActivity(), "mima", "");
            ShareUtils.putString(getActivity(),"year","");
            ShareUtils.putString(getActivity(), "xuehao", "");
            btn_bind.setText("绑定账号");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        L.e(v.getId()+"");
        btn_commit.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.et_desc:
                edt_desc.setFocusableInTouchMode(true);
                //设置为默认背景
                edt_desc.setBackground(edt_defaultBK);
                break;
            case R.id.et_sex:
                edt_sex.setFocusableInTouchMode(true);
                edt_sex.setBackground(edt_defaultBK);
                break;
            case R.id.edit_user:
                edt_name.setFocusableInTouchMode(true);
                edt_name.setBackground(edt_defaultBK);
                break;
        }
        return true;
    }
}
