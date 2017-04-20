package com.example.stxr.zzu_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.PassageAdapter;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   ExchangeFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:50
 *  描述：    交流
 */
public class ExchangeFragment extends Fragment {
    private ListView lv_passage;
    private Context context;
    private List<MyBBS> passageList;
    private MyBBS passage;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exchange,null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        BmobQuery<MyBBS> q = new BmobQuery<>();
        //按时间降序排序
        q.order("-createdAt");
        //查询20条信息
        q.setLimit(20);
        q.findObjects(new FindListener<MyBBS>() {
            @Override
            public void done(List<MyBBS> list, BmobException e) {
                if (e == null) {
                    PassageAdapter pad = new PassageAdapter(getContext(), list);
                    lv_passage.setAdapter(pad);
//                    for (MyBBS b : list) {
//                        L.i(b.getTitle());
//                        L.i(b.getContent());
//                        L.i(b.getUserName());
//                    }
                }else{
                    T.shortShow(getContext(),"加载失败"+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    private void initView() {
        lv_passage = (ListView) view.findViewById(R.id.lv_passage);
    }

}
