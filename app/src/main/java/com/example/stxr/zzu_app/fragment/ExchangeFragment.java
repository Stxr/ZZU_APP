package com.example.stxr.zzu_app.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.PassageAdapter;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.ui.ShowPassageActivity;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
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
    public static final String ARGUMENT = "isSent";
    private XRecyclerView xclv_passage;
    private Context context;
    private List<MyBBS> passageList;
    private MyBBS passage;
    private View view;
    private PassageAdapter pad;
    private int numsLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exchange, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
//        pad = new PassageAdapter(getActivity(), new ArrayList<MyBBS>());
        showData(10);
        xclv_passage.setLayoutManager(new LinearLayoutManager(context));
        //上拉刷新
        xclv_passage.setLoadingMoreEnabled(true);
        //下拉刷新
        xclv_passage.setPullRefreshEnabled(true);
        //添加分界线
        xclv_passage.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        xclv_passage.setRefreshProgressStyle(ProgressStyle.SquareSpin);
        xclv_passage.setLoadingMoreProgressStyle(ProgressStyle.BallScale);
        xclv_passage.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xclv_passage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showData(10);
                        xclv_passage.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                xclv_passage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData(2);
                        xclv_passage.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }

    public void showData(int num) {
        BmobQuery<MyBBS> q = new BmobQuery<>();
        //按时间降序排序
        q.order("-createdAt");
        q.include("author");
        //查询20条信息
        numsLoad = num;
        q.setLimit(num);
//        boolean isCache = q.hasCachedResult(MyBBS.class);
//        if(isCache){  //--此为举个例子，并不一定按这种方式来设置缓存策略
//            q.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        }else{
//            q.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        q.findObjects(new FindListener<MyBBS>() {
            @Override
            public void done(List<MyBBS> list, BmobException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = list;
                    handler.sendMessage(message);
                } else {
                    T.shortShow(getActivity(), "加载失败" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void loadMoreData(int num) {
        BmobQuery<MyBBS> q = new BmobQuery<>();
        //按时间降序排序
        q.order("-createdAt");
        q.include("author");
        //查询20条信息
        q.setSkip(numsLoad);
        q.setLimit(numsLoad + num);
        numsLoad = numsLoad + num;
//        boolean isCache = q.hasCachedResult(MyBBS.class);
//        if(isCache){  //--此为举个例子，并不一定按这种方式来设置缓存策略
//            q.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        }else{
//            q.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        q.findObjects(new FindListener<MyBBS>() {
            @Override
            public void done(List<MyBBS> list, BmobException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = list;
                    handler.sendMessage(message);
                } else {
                    T.shortShow(getActivity(), "加载失败" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pad != null) {
            pad.notifyDataSetChanged();
        }
//        loadMoreData(10);
    }

    private void initView() {
        xclv_passage = (XRecyclerView) view.findViewById(R.id.xclv_passage);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    passageList = (List<MyBBS>) msg.obj;
                    pad = new PassageAdapter(getActivity(), passageList);
                    xclv_passage.setAdapter(pad);
                    pad.setOnItemClickListener(new PassageAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, MyBBS myBBS) {
                            Intent intent = new Intent(getContext(), ShowPassageActivity.class);
                            //通过intent传键值对上去
                            intent.putExtra("title", myBBS.getTitle());
                            intent.putExtra("content", myBBS.getContent());
                            intent.putExtra("ObjectID", myBBS.getObjectId());
                            intent.putExtra("createdTime", myBBS.getCreatedAt());
                            intent.putExtra("username", myBBS.getAuthor().getUsername());
                            myBBS.increment("visits");
                            myBBS.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                }
                            });
                             startActivityForResult(intent,52);
                        }
                    });
                    pad.setOnItemLongClickListener(new PassageAdapter.OnRecyclerViewItemLongClickListener() {
                        @Override
                        public void onItemLongClick(final View view, final MyBBS myBBS) {
                            if(myBBS.getAuthor().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("提示");
                                builder.setMessage("确定删除此帖？");
                                builder.setCancelable(false);
                                builder.setNegativeButton("取消", null);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myBBS.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    T.shortShow(getContext(), "删除成功");
                                                    showData(numsLoad);
                                                } else {
                                                    T.shortShow(getContext(), "删除失败"+e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                });
                                builder.create().show();
                            }

                        }
                    });
                    break;
                case 2:
                    if ((List<MyBBS>) msg.obj != null && passageList != null) {
                        passageList.addAll((List<MyBBS>) msg.obj);
                        pad.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 52:
//                showData(10);
                break;

        }
    }
}
