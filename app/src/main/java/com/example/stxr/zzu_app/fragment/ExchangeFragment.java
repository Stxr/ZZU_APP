package com.example.stxr.zzu_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.PassageAdapter;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.ui.ShowPassageActivity;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   ExchangeFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:50
 *  描述：    交流
 */
public class ExchangeFragment extends Fragment {
    private SwipeRefreshLayout srl_fresh;
    public static final String ARGUMENT = "isSent";
    private ListView lv_passage;
    private Context context;
    private List<MyBBS> passageList;
    private MyBBS passage;
    private View view;
    private PassageAdapter pad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exchange, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        showData(20);
        lv_passage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),ShowPassageActivity.class);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
                TextView tv_objectID = (TextView) view.findViewById(R.id.tv_ObjectID);
               //通过intent传键值对上去
                intent.putExtra("title",tv_title.getText().toString());
                intent.putExtra("content", tv_content.getText().toString());
                intent.putExtra("ObjectID", tv_objectID.getTag().toString());
                startActivity(intent);
            }
        });
        lv_passage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                L.e("长按了" + position);
                TextView tv_objectID = (TextView) view.findViewById(R.id.tv_ObjectID);
                L.e(tv_objectID.getTag().toString());
                MyBBS bbs = new MyBBS();
                bbs.setObjectId(tv_objectID.getTag().toString());
                bbs.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null) {
                            T.shortShow(getContext(),"删除成功");
                        }else {
                            T.shortShow(getContext(),"删除失败");
                        }
                    }
                });
                showData(20);
                return true;
            }
        });
        //下拉刷新
        srl_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData(20);
                srl_fresh.setRefreshing(false);
            }
        });
    }

    private void showData(int num) {
        BmobQuery<MyBBS> q = new BmobQuery<>();
        //按时间降序排序
        q.order("-createdAt");
        q.include("author");
        //查询20条信息
        q.setLimit(num);
        q.findObjects(new FindListener<MyBBS>() {
            @Override
            public void done(List<MyBBS> list, BmobException e) {
                if (e == null) {
                    pad = new PassageAdapter(getContext(), list);
                    lv_passage.setAdapter(pad);
                } else {
                    T.shortShow(getContext(), "加载失败" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //pad.notifyDataSetChanged();
        showData(20);
    }

    private void initView() {
        lv_passage = (ListView) view.findViewById(R.id.lv_passage);
        srl_fresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
    }


}
