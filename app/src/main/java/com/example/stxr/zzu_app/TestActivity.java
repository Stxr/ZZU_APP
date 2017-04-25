package com.example.stxr.zzu_app;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stxr.zzu_app.bean.Course;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.utils.BaiduMapUtils;
import com.example.stxr.zzu_app.utils.HttpUtils;
import com.example.stxr.zzu_app.utils.L;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {
    public static  TextView text;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            //text.setText((String) msg.obj);
          //  L.e((String) msg.obj);
            Document doc = Jsoup.parse((String) msg.obj);
            //标题
            Elements title = doc.select("span");
            //上课的时间
            Elements courseTime = doc.select("td[align]");
            Elements course = doc.select("table#table3 tr");//找到id=table3 的table 下面所有的tr
//            L.e(title.text());
//            L.e(courseTime.get(1).text());//得到第二个值
            //L.e(course.get(0).text()); //星期2 第一节课
            //L.e(course.get(1).childNodeSize()+"");
            String[][] courseTable = new String[7][10];
            for(int i=0;i<course.size();i++) {
                for(int j=0;j<course.get(1).childNodeSize();j++) {
                    courseTable[i][j]=course.get(i).child(j).text();
                }
                L.e(courseTable[i][0]+courseTable[i][1]+courseTable[i][2]
                        +courseTable[i][3]+courseTable[i][4]+courseTable[i][5]
                        +courseTable[i][6]+courseTable[i][7]);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        text = (TextView) findViewById(R.id.text);
        //        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("https://www.baidu.com").build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Message msg = new Message();
//                msg.what=0;
//                msg.obj = response.body().string();
//                mHandler.sendMessage(msg);
//            }
//        });
        HttpUtils.getInstance();
//        HttpUtils.doGet("https://www.baidu.com", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Message msg = new Message();
//                msg.what=0;
//                msg.obj = response.body().string();
//                mHandler.sendMessage(msg);
//            }
//        });
        HashMap<String, String> key = new HashMap<>();
        key.put("xuehao","20142410122");
        key.put("nianji", "2014");
        key.put("mima","sunshee");
        HttpUtils.doPost("http://jw.zzu.edu.cn/pks/pkisapi2.dll/kbofstu", key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what=0;
//                msg.obj = response.body().string();
//                String string = new String(response.body().string().getBytes("GBK"), "UTF-8");
//                msg.obj = new String(response.body().string().getBytes(), "UTF-8");
                byte[] b =response.body().bytes();
                final String info = new String(b, "GB2312");
                msg.obj = info;
                mHandler.sendMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(info);
                    }
                });
            }
        });

    }
}
