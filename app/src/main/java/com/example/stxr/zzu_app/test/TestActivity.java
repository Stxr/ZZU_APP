package com.example.stxr.zzu_app.test;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CourseOnlineAdapter;
import com.example.stxr.zzu_app.bean.ClassroomFree;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;

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
import rx.Subscription;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    //    private MyEditText medt_test;
    private RecyclerView rv_test;
    private Button btn_add_image;
    private Button btn_display;
    private Button btn_translate;
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
                                L.e(classroom.getId()+" "+classroom.getLoc()+" "+classroom.getRoom());
                                classroomFreesList.add(classroom);
                                classroom = new ClassroomFree(); //必须要加，否则都是重复的
                                break;
                        }
                    }
                    rv_test.setLayoutManager(new LinearLayoutManager(TestActivity.this));
                    CourseOnlineAdapter adapter = new CourseOnlineAdapter(TestActivity.this,classroomFreesList);
                    rv_test.setAdapter(adapter);
//                    rv_test = (TextView) findViewById(R.id.rv_test);
//                    rv_test.setText("ww");

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        rv_test = (RecyclerView) findViewById(R.id.rv_test);
        btn_display = (Button) findViewById(R.id.btn_display);
        btn_translate = (Button) findViewById(R.id.btn_translate);
        btn_add_image = (Button) findViewById(R.id.btn_add_image);
        initData();
    }

    private void initData() {
        HttpUtils.getInstance();
        final TestActivity test = new TestActivity();
        btn_add_image.setOnClickListener(this);
        btn_display.setOnClickListener(this);
    }

    void login() {
        HashMap<String, String> key = new HashMap<>();
        key.put("zhanghao", "20142410122");
//        key.put("nianji","2014");
        key.put("mima", "sunshee");
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

    void query() {
        String url = "http://jw.zzu.edu.cn/Scripts/freeroom/freeroom.dll/submit?userid=2D2034F53DAB36663AD8D4E6A58F561A703DB799";
        HashMap<String, String> key = new HashMap<>();
        key.put("xqsort", "1");
        key.put("jcsort", "1");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image:
                login();
                break;
            case R.id.btn_display:
                query();
                break;
        }
    }

}
