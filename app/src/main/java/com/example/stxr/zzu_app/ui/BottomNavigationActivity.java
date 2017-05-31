package com.example.stxr.zzu_app.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.print.PrintHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.fragment.ClassFragment;
import com.example.stxr.zzu_app.fragment.CourseOnlineFragment;
import com.example.stxr.zzu_app.fragment.CourseTableFragment;
import com.example.stxr.zzu_app.fragment.ExchangeFragment;
import com.example.stxr.zzu_app.fragment.UserFragment;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.utils.UtilTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

import static com.example.stxr.zzu_app.statics.StaticConstant.COUNTDOWN_REQUEST_CODE;
import static com.example.stxr.zzu_app.statics.StaticConstant.INFO_CHAGNE_CODE;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   BottomNavigationActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 17:01
 *  描述：    
 */
public class BottomNavigationActivity extends AppCompatActivity implements View.OnClickListener {
    //    private Course course;
    private LinearLayout bottom_nav_content;//内容区域
    private BottomNavigationBar bottom_navigation_bar_container;//底部导航栏
    private ClassFragment classFragment;
    private CourseOnlineFragment courseOnlineFragment;
    private ExchangeFragment exchangeFragment;
    private CourseTableFragment courseTableFragment;
    //    private UserFragment userFragment;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView tv_name;
    private CircleImageView profileImage;

    //消息初始化
    private NotificationManager notificationManager;

    private Notification.Builder builder;
    private Intent mIntent;
    private PendingIntent pendingIntent;
    //退出的时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        initView();
        initData();
        initBottomNavBar();
    }

    private void initData() {
        fab.setVisibility(View.GONE);
        tv_name.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        //让toolbar产生汉堡菜单图标
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //侧拉菜单
        setupDrawerContent(navigationView);
        String endTime = ShareUtils.getString(this, "targetTime", null);
        if (endTime != null) {
            updateCountDown(endTime);
        }
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        setUserInfo(userInfo.getUsername(), userInfo.isSex() ? "男" : "女", userInfo.getDesc(), null);

        String bind = ShareUtils.getString(BottomNavigationActivity.this, "xuehao", null);
        if (bind == null) {
            bind = "学生账号绑定";
        }else{
            bind = "账号"+ShareUtils.getString(BottomNavigationActivity.this, "xuehao", null)+"已绑定";
        }
        navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setTitle(bind);

        UtilTools.getImageFromSD(this, userInfo.getObjectId() + ".jpg", profileImage);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //离线查询
                    case R.id.nav_search:
                        startActivity(new Intent(BottomNavigationActivity.this, CourseOutLine.class));
                        break;
                    //倒计时
                    case R.id.nav_countDown:
                        intent = new Intent(BottomNavigationActivity.this, CountDownActivity.class);
                        startActivityForResult(intent, COUNTDOWN_REQUEST_CODE);
                        break;
                    //教务处账号绑定
                    case R.id.nav_bind:
                        startActivity(new Intent(BottomNavigationActivity.this, BindActivity.class));
                        break;
                    //切换账号
                    case R.id.nav_switch:
                        BmobUser.logOut();   //清除缓存用户对象
                        startActivity(new Intent(BottomNavigationActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case R.id.nav_sex:
                    case R.id.nav_info:
                        sendData(new Intent());
                        break;
                }
                //取消菜单的选中状态
                item.setCheckable(false);
                item.setChecked(false);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setUserInfo(String name, String sex, String doc, Bitmap bitmap) {
        String info = "性别：" + sex;
        String docu = "院系：" + doc;
        if (bitmap != null) {
            profileImage.setImageBitmap(bitmap);
        }
        tv_name.setText(name);
        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setTitle(info);
        navigationView.getMenu().getItem(0).getSubMenu().getItem(1).setTitle(docu);
    }


    private void initBottomNavBar() {
        //设置模式
        bottom_navigation_bar_container.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottom_navigation_bar_container.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_navigation_bar_container.setBarBackgroundColor(R.color.colorPrimary);//背景颜色
        bottom_navigation_bar_container.setInActiveColor(R.color.colorPrimaryDark);//未选中时的颜色
        bottom_navigation_bar_container.setActiveColor(R.color.colorPrimaryLight);//选中时的颜色
//        badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setHideOnSelect(true);//角标
        bottom_navigation_bar_container
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_white_24dp, R.string.title1))
                .addItem(new BottomNavigationItem(R.drawable.ic_question_answer_white_24dp, R.string.title2))
                .addItem(new BottomNavigationItem(R.drawable.ic_contact_mail_black_24dp, getString(R.string.title4)))
//                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_white_24dp, R.string.title3))
                .initialise();
        bottom_navigation_bar_container.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                hideAllFrag();
                switch (position) {
//                    case 0:
//                        fab.setVisibility(View.GONE);
//                        if (classFragment == null) {
//                            classFragment = new ClassFragment();
//                        }
//                        addFrag(classFragment);
//                        getSupportFragmentManager().beginTransaction().show(classFragment).commit();
//                        getSupportActionBar().setTitle(getString(R.string.title1));
//                        break;
                    case 0:
                        fab.setVisibility(View.GONE);
                        if (courseOnlineFragment == null) {
                            courseOnlineFragment = new CourseOnlineFragment();
                        }
                        addFrag(courseOnlineFragment);
                        getSupportFragmentManager().beginTransaction().show(courseOnlineFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title1));
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        if (exchangeFragment == null) {
                            exchangeFragment = new ExchangeFragment();
                        }
                        addFrag(exchangeFragment);
                        getSupportFragmentManager().beginTransaction().show(exchangeFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title2));
                        break;
                    case 2:
                        fab.setVisibility(View.GONE);
                        if (courseTableFragment == null) {
                            courseTableFragment = new CourseTableFragment();
                        }
                        addFrag(courseTableFragment);
                        getSupportFragmentManager().beginTransaction().show(courseTableFragment).commit();
                        getSupportActionBar().setTitle(getString(R.string.title4));
                        break;
//                    case 3:
//                        fab.setVisibility(View.GONE);
//                        if (userFragment == null) {
//                            userFragment = new UserFragment();
//                        }
//                        addFrag(userFragment);
//                        getSupportFragmentManager().beginTransaction().show(userFragment).commit();
//                        getSupportActionBar().setTitle(getString(R.string.title3));
//                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中

            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中

            }
        });
        setDefaultFrag();
    }

    /*设置默认Fragment*/
    private void setDefaultFrag() {
        if (courseOnlineFragment == null) {
            courseOnlineFragment = new CourseOnlineFragment();
        }
        addFrag(courseOnlineFragment);
        /*默认显示msgFrag*/
        getSupportFragmentManager().beginTransaction().show(courseOnlineFragment).commit();
    }

    /*添加Frag*/
    private void addFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && !frag.isAdded()) {
            ft.add(R.id.bottom_nav_content, frag);
        }
        ft.commit();
    }

    /*隐藏所有fragment*/
    private void hideAllFrag() {
        hideFrag(courseOnlineFragment);
//        hideFrag(classFragment);
        hideFrag(exchangeFragment);
        hideFrag(courseTableFragment);
//        hideFrag(userFragment);
    }

    /*隐藏frag*/
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
        }
        ft.commit();
    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        bottom_nav_content = (LinearLayout) findViewById(R.id.bottom_nav_content);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottom_navigation_bar_container = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_container);
        tv_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_userName);
        profileImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.userProfile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INFO_CHAGNE_CODE: //修改资料
                if (data != null) {
                    String name = data.getStringExtra("name");
                    String speciality = data.getStringExtra("speciality");
                    String sex = data.getStringExtra("sex");
                    Bitmap bitmap = data.getParcelableExtra("image");
                    setUserInfo(name, sex, speciality, bitmap);
                }
                break;
            //考试倒计时
            case COUNTDOWN_REQUEST_CODE:
                if (data != null) {
                    countDown(data);
                }
                break;
            case 1:
                exchangeFragment.showData(10);
                break;
        }
    }

    private void countDown(Intent data) {
        String endTime = data.getExtras().getString("targetTime");
        ShareUtils.putString(BottomNavigationActivity.this, "targetTime", endTime);
        updateCountDown(endTime);
//        navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setTitle("距离考试时间还有" + updateCountDown(endTime) + "天");
    }

    //根据设置日期更新时间
    private String updateCountDown(String endTime) {
        Date startDate = new Date();
        Date endDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setTitle("距离考试时间还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");
        showNormalNotification("距离考试时间" + endTime + "还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");
        return DataUtils.twoDateDistance(startDate, endDate);
    }

    //通知
    private void showNormalNotification(String leftTime) {
        builder = new Notification.Builder(BottomNavigationActivity.this);//创建builder对象
        //指定点击通知后的动作，软件界面
        mIntent = new Intent(BottomNavigationActivity.this, BottomNavigationActivity.class);
        pendingIntent = PendingIntent.getActivity(BottomNavigationActivity.this, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent); //跳转
        builder.setSmallIcon(R.drawable.classroom);//小图标
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true); //顾名思义，左右滑动可删除通知
        notificationManager = (NotificationManager) BottomNavigationActivity.this.getSystemService(NOTIFICATION_SERVICE);
        builder.setContentTitle("考试倒计时");
//        builder.setSubText(targetTime);
        builder.setContentText(leftTime);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this, PassageActivity.class), 1);
                break;
            case R.id.tv_userName:
            case R.id.userProfile:
                sendData(new Intent());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String bind = ShareUtils.getString(BottomNavigationActivity.this, "xuehao", null);
        if (bind == null) {
            bind = "学生账号绑定";
        }else{
            bind = "账号"+ShareUtils.getString(BottomNavigationActivity.this, "xuehao", null)+"已绑定";
        }
        navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setTitle(bind);
    }

    //发送个人信息数据给修改的activity
    private void sendData(Intent intent) {
        String sex = navigationView.getMenu().getItem(0).getSubMenu().getItem(0).getTitle().toString();
        String speciality = navigationView.getMenu().getItem(0).getSubMenu().getItem(1).getTitle().toString();
        sex = sex.replace("性别：", "").trim();
        speciality = speciality.replace("院系：", "").trim();
        String name = tv_name.getText().toString().trim();
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        intent = new Intent(BottomNavigationActivity.this, InfoChangeActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("sex", sex);
        intent.putExtra("speciality", speciality);
        intent.putExtra("image", bitmap);
        startActivityForResult(intent, INFO_CHAGNE_CODE);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {   //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
