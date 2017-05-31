package com.example.stxr.zzu_app.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.AbsGridAdapter;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.StringUtils;
import com.example.stxr.zzu_app.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragment
 *  文件名:   CourseTableFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/25 11:27
 *  描述：    
 */
public class CourseTableFragment extends Fragment implements View.OnClickListener {
    private Spinner spinner;

    private GridView detailCource;

    private String[][] contents;

    private String[][] courseTable;

    private AbsGridAdapter secondAdapter;

    private List<String> dataList;

    private ArrayAdapter<String> spinnerAdapter;

    private TextView tv_year;

    private View v;

    private Button btn_setting;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Document doc = Jsoup.parse((String) msg.obj);
            Elements isLogin = doc.select("title");
            if (!isLogin.text().equals("信息")) {//title不是信息说明登录成功
                Elements course = doc.select("table#table3 tr");//找到id=table3 的table 下面所有的tr
                courseTable = new String[7][10];
                L.e(course.get(1).childNodeSize() - 1 + "");
                for (int i = 1; i < course.size(); i++) {
                    for (int j = 1; j < course.get(1).childNodeSize(); j++) {
                        courseTable[i - 1][j - 1] = course.get(i).child(j).text() == null ? "" : course.get(i).child(j).text();
                        //courseList.add(courseTable[i - 1][j - 1]);
                    }
//                    L.e(courseTable[i - 1][1] + courseTable[i - 1][1] + courseTable[i - 1][2]
//                            + courseTable[i - 1][3] + courseTable[i - 1][4] + courseTable[i - 1][5]
//                            + courseTable[i - 1][6] + courseTable[i - 1][7]);
                }
                List<String> courseList = new ArrayList<>();
                Gson gson = new Gson();
                for (int j = 1; j < course.get(1).childNodeSize(); j++) {
                    for (int i = 1; i < course.size(); i++) {
                        String value = courseTable[i - 1][j - 1];
                        if (value.equals("")) {
//                            courseList.add(null);
                        } else {
                            courseList.add(value);
                        }

                    }
                }

                L.e("json:"+gson.toJson(courseList));
                ShareUtils.putString(getContext(), "course", gson.toJson(courseList));
//                String test = ShareUtils.getString(getContext(), "course", "");
//                L.e("fromJson"+gson.fromJson(test,new TypeToken<List<String>>(){}.getType()).toString());
                secondAdapter = new AbsGridAdapter(getContext());
                secondAdapter.setContent(courseTable, 5, 7);
                detailCource.setAdapter(secondAdapter);
            } else {
                T.shortShow(getContext(), "还没有绑定账号  ！！！");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_course_table, null);
        initView();
        initData();
        return v;
    }

    private void initData() {
        HttpUtils.getInstance();
        HashMap<String, String> key = new HashMap<>();
        key.put("xuehao", ShareUtils.getString(getActivity(), "xuehao", ""));
        key.put("nianji", ShareUtils.getString(getActivity(), "year", ""));
        key.put("mima", ShareUtils.getString(getActivity(), "mima", ""));
        HttpUtils.doPost("http://jw.zzu.edu.cn/pks/pkisapi2.dll/kbofstu", key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("获取账号失败");
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
        //////////////创建Spinner数据
        fillDataList();
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        btn_setting.setOnClickListener(this);
        tv_year.setText(DataUtils.getYear());//得到年份
        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int thisWeek = calendar.get(Calendar.WEEK_OF_YEAR) - ShareUtils.getInt(getContext(), "weekOfSchool", 0);
        if (thisWeek > dataList.size()) {
            thisWeek = dataList.size() - 1;
        } else if (thisWeek < 0) {
            thisWeek = 0;
        }
        spinner.setSelection(thisWeek);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isBind = ShareUtils.getBoolean(getActivity(), "idBind", false);
        if (isBind) {
            initData();
        }
    }

    private void initView() {
        spinner = (Spinner) v.findViewById(R.id.switchWeek);
        detailCource = (GridView) v.findViewById(R.id.courceDetail);
        tv_year = (TextView) v.findViewById(R.id.year);
        btn_setting = (Button) v.findViewById(R.id.btn_setting);
    }

    public void fillDataList() {
        dataList = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            dataList.add("第" + i + "周");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                String string;
                Date date = new Date();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                new AlertDialog.Builder(getActivity())
                        .setTitle("设置当前周")
                        .setItems(dataList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int week = calendar.get(Calendar.WEEK_OF_YEAR) - which;
                                ShareUtils.putInt(getContext(), "weekOfSchool", week);
//                                T.shortShow(getActivity(),"当前是"+(Calendar.WEEK_OF_YEAR-week));
                                spinner.setSelection(calendar.get(Calendar.WEEK_OF_YEAR) - ShareUtils.getInt(getContext(), "weekOfSchool", 0));
                                T.shortShow(getActivity(), "设置成功，当前周为" + (calendar.get(Calendar.WEEK_OF_YEAR) - week + 1) + "周");
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
                break;
        }
    }
}
