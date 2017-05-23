package com.example.stxr.zzu_app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   CountDownActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/28 19:28
 *  描述：    
 */
public class CountDownActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spYear;
    private Spinner spMonth;
    private Spinner spDay;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private ArrayList<String> dataDay = new ArrayList<String>();
    private ArrayAdapter<String> adapterSpYear;
    private ArrayAdapter<String> adapterSpMonth;
    private ArrayAdapter<String> adapterSpDay;
    private Button btn_confirm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        spYear = (Spinner) findViewById(R.id.spinner_year);
        spMonth = (Spinner) findViewById(R.id.spinner_month);
        spDay = (Spinner) findViewById(R.id.spinner_day);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        // 年份设定为当年的前后20年
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) + i));
        }
        adapterSpYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataYear);
        adapterSpYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapterSpYear);
        spYear.setSelection(0);// 默认选中今年
        // 12个月
        int currentMonth=0;
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
            if (cal.get(Calendar.MONTH) + 1 == i) {
                currentMonth =i-1;
            }
        }
        adapterSpMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataMonth);
        adapterSpMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(adapterSpMonth);
        spMonth.setSelection(currentMonth);

        int currentDay =0;
        adapterSpDay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataDay);
        adapterSpDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(adapterSpDay);


        spMonth.setOnItemSelectedListener(this);
        spDay.setOnItemSelectedListener(this);
        spYear.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击了确定按钮
            case R.id.btn_confirm:
                String time = spYear.getSelectedItem().toString() +
                        spMonth.getSelectedItem().toString() +
                        spDay.getSelectedItem().toString();
                String time2 = spYear.getSelectedItem().toString() + "年"+
                        spMonth.getSelectedItem().toString() +"月"+
                        spDay.getSelectedItem().toString()+"日";
//                L.e(spYear.getSelectedItem().toString());
//                L.e(spMonth.getSelectedItem().toString());
//                L.e(spDay.getSelectedItem().toString());
                Date now = new Date();
                Date setDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    setDate = sdf.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (setDate.getTime() - now.getTime() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("countdown", time);
                    intent.putExtra("targetTime",time2);
                    setResult(121, intent);
                    finish();
                } else {
                    T.shortShow(this,"设置的日期必须在今天之后");
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_month:
                L.e("spinner_month: "+position);
                ShareUtils.putInt(this,"spinner_month",position);
                dataDay.clear();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(spYear.getSelectedItem().toString()));
                cal.set(Calendar.MONTH, position);
                int dayofm = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= dayofm; i++) {
                    dataDay.add("" + (i < 10 ? "0" + i : i));
                    if (cal.get(Calendar.DAY_OF_MONTH) == i) {
                        spDay.setSelection(i-1);
                    }
                }
                adapterSpDay.notifyDataSetChanged();
                break;
//            case R.id.spinner_year:
////                ShareUtils.putInt(this,"spinner_year",position);
//                L.e("spinner_year: "+position);
//                break;
//            case R.id.spinner_day:
////                ShareUtils.putInt(this,"spinner_day",position);
//                L.e("spinner_day: "+position);
//                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
