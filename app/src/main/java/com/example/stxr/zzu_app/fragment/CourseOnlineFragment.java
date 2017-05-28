package com.example.stxr.zzu_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CourseOnlineAdapter;
import com.example.stxr.zzu_app.bean.ClassroomFree;
import com.example.stxr.zzu_app.test.TestActivity;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragment
 *  文件名:   CourseOnlineFragment
 *  创建者:   Stxr
 *  创建时间:  2017/5/28 23:08
 *  描述：    
 */
public class CourseOnlineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private RecyclerView rv_courseOnline;
    private Button btn_search;
    private Spinner sp_weekday;
    private Spinner sp_courseTime;
    private static final int DELAY =12;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courseonline, null);
        context = view.getContext();
        findView();
        initData();
        return view;
    }

    private void initData() {
        btn_search.setOnClickListener(this);
    }
    private void findView() {
        rv_courseOnline = (RecyclerView) view.findViewById(R.id.rv_courseOnline);
        btn_search = (Button) view.findViewById(R.id.btn_search);
        sp_weekday = (Spinner) view.findViewById(R.id.sp_weekday);
        sp_courseTime = (Spinner) view.findViewById(R.id.sp_classTime);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                login();
//                handler.sendEmptyMessageDelayed(DELAY, 500);
                int weekDay = sp_weekday.getSelectedItemPosition() + 1;
                int coursetime = sp_courseTime.getSelectedItemPosition() + 1;
                T.shortShow(getActivity(),"星期" + weekDay + "第" + coursetime + "节");
                query(weekDay,coursetime);
                break;
        }
    }
    void login() {
        HashMap<String, String> key = new HashMap<>();
        key.put("zhanghao",ShareUtils.getString(getActivity(),"xuehao",""));
//        key.put("nianji","2014");
        key.put("mima", ShareUtils.getString(getActivity(),"mima",""));
        HttpUtils.doPost("http://jw.zzu.edu.cn/scripts/freeroom/freeroom.dll/mylogin", key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] b = response.body().bytes();
                final String info = new String(b, "GB2312");
//                L.e(info);
            }
        });
    }

    void query(int weekday,int courseTime) {
        String url = "http://jw.zzu.edu.cn/Scripts/freeroom/freeroom.dll/submit?userid=2D2034F53DAB36663AD8D4E6A58F561A703DB799";
        HashMap<String, String> key = new HashMap<>();
        key.put("xqsort", weekday+"");
        key.put("jcsort", courseTime+"");
        HttpUtils.doPost(url, key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] b = response.body().bytes();
                final String info = new String(b, "GB2312");
                Document doc = Jsoup.parse(info);
                Elements isLogin = doc.select("table tr td[class=STYLE3]"); //寻找table标签下的tr标签下的td标签下的class=STYLE3的
                Message msg = new Message();
                msg.obj = isLogin;
                msg.what = 110;
                handler.sendMessage(msg);
//                rv_test.setText(info);
//                L.e(info);
            }
        });
    }
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ClassroomFree classroom = new ClassroomFree();
            List<ClassroomFree> classroomFreesList = new ArrayList<>();
            switch (msg.what) {
                case 110:
                    Elements isLogin = (Elements) msg.obj;
//                    L.e(isLogin.text());
                    for (int i = 0; i < isLogin.size(); i++) {
                        switch (i % 4) {
                            case 0:
                                classroom.setId(isLogin.get(i).text());
                                break;
                            case 1:
                                classroom.setLoc(isLogin.get(i).text());
                                break;
                            case 2:
                                classroom.setRoom(isLogin.get(i).text());
//                                L.e(classroom.getId()+" "+classroom.getLoc()+" "+classroom.getRoom());
                                classroomFreesList.add(classroom);
                                classroom = new ClassroomFree(); //必须要加，否则都是重复的
                                break;
                        }
                    }
                    rv_courseOnline.setLayoutManager(new LinearLayoutManager(context));
                    CourseOnlineAdapter adapter = new CourseOnlineAdapter(context,classroomFreesList);
                    rv_courseOnline.setAdapter(adapter);
                    break;
//                case DELAY:
//
//                    break;
            }
        }
    };
}
