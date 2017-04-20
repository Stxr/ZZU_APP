package com.example.stxr.zzu_app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stxr.zzu_app.R;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   UserFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:53
 *  描述：    个人设置
 */
public class UserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.franment_user, null);
        return view;
    }
}
