package com.example.stxr.zzu_app.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.ui.BindActivity;
import com.example.stxr.zzu_app.ui.BottomNavigationActivity;
import com.example.stxr.zzu_app.ui.CountDownActivity;
import com.example.stxr.zzu_app.ui.LoginActivity;
import com.example.stxr.zzu_app.utils.DataUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.utils.UtilTools;
import com.example.stxr.zzu_app.view.CustomDialog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.stxr.zzu_app.statics.StaticConstant.BMOB_IAMGE_CACHE;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragnment
 *  文件名:   UserFragment
 *  创建者:   Stxr
 *  创建时间:  2017/4/19 8:53
 *  描述：    个人设置
 */
public class UserFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private EditText edt_sex;
    private EditText edt_desc;
    private EditText edt_name;
    private Button btn_exit;
    private Button btn_commit;
    private View view;
    private Drawable edt_defaultBK;
    private Button btn_bind;
    private CircleImageView profile_image;
    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private TextView tv_countdown;

    //消息初始化
    private NotificationManager notificationManager;

    private Notification.Builder builder;
    private Intent mIntent;
    private PendingIntent pendingIntent;


    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    public static final int COUNTDOWN_REQUEST_CODE = 122;
    private File tempFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.franment_user, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        setEnable(false);
        boolean isBind = ShareUtils.getBoolean(getActivity(), "idBind", false);
        if (isBind) {
            btn_bind.setText("更改绑定");
        } else {
            btn_bind.setText("绑定学校账号");
        }
        //显示信息
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        edt_name.setText(userInfo.getUsername());
        edt_desc.setText(userInfo.getDesc());
        edt_sex.setText(userInfo.isSex() ? "男" : "女");
        BmobFile b= userInfo.getImg();
        String ad = b.getUrl();
        tv_countdown.setText(ShareUtils.getString(getActivity(), "countDown", "点击设置倒计时"));
    }

    private void initView() {
        tv_countdown = (TextView) view.findViewById(R.id.tv_countdown);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        btn_bind = (Button) view.findViewById(R.id.btn_bind);
        btn_commit = (Button) view.findViewById(R.id.btn_commit);
        edt_desc = (EditText) view.findViewById(R.id.et_desc);
        edt_sex = (EditText) view.findViewById(R.id.et_sex);
        edt_name = (EditText) view.findViewById(R.id.edit_user);
        btn_exit = (Button) view.findViewById(R.id.btn_exit_user);
        edt_defaultBK = edt_desc.getBackground();
        btn_exit.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        edt_desc.setOnLongClickListener(this);
        edt_name.setOnLongClickListener(this);
        edt_sex.setOnLongClickListener(this);
        btn_bind.setOnClickListener(this);
        tv_countdown.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        //拿照片
//        UtilTools.getImageToShare(getActivity(), profile_image);
        final MyUser user = BmobUser.getCurrentUser(MyUser.class);
        //先从本地读取照片，如果没有再从网上下载
        boolean is = UtilTools.getImageFromSD(getActivity(),user.getObjectId()+".jpg", profile_image);
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
//        dialog.setCancelable(false);
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    private void setEnable(boolean is) {
        edt_desc.setFocusableInTouchMode(is);
        edt_sex.setFocusableInTouchMode(is);
        edt_name.setFocusableInTouchMode(is);
        edt_desc.setBackground(null);
        edt_name.setBackground(null);
        edt_sex.setBackground(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                BmobUser.logOut();   //清除缓存用户对象
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_commit:
                MyUser user = new MyUser();
                String name = edt_name.getText().toString().trim();
                String desc = edt_desc.getText().toString().trim();
                String sex = edt_sex.getText().toString().trim();
                if (sex.equals("男")) {
                    user.setSex(true);
                } else {
                    user.setSex(false);
                }
                user.setDesc(desc);
                user.setUsername(name);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //修改成功
                            setEnable(false);
                            btn_commit.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //绑定校园网账号
            case R.id.btn_bind:
                startActivity(new Intent(getActivity(), BindActivity.class));
                break;
            //考试倒计时
            case R.id.tv_countdown:
                Intent intent = new Intent(getActivity(), CountDownActivity.class);
                startActivityForResult(intent, COUNTDOWN_REQUEST_CODE);
                break;

            //圆形头像
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
        }
    }

    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        L.e("resuestCode:"+requestCode);
//        L.e("resultCodeL" + resultCode);
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        try {
                            setImageToView(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updataImage();
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
                //倒计时
                case COUNTDOWN_REQUEST_CODE:
//                    L.e(data.getExtras().getString("countdown"));
                    Date startDate = new Date();
                    Date endDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String endTime = data.getExtras().getString("countdown");
                    try {
                        endDate = sdf.parse(endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tv_countdown.setText("距离考试时间还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");
//                    L.e(DataUtils.twoDateDistance(startDate, endDate)+"天");
                    showNormalNotification("距离考试时间"+data.getExtras().getString("targetTime")+"还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");
                    ShareUtils.putString(getActivity(), "countDown", "距离考试时间还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");
                    ShareUtils.putString(getActivity(), "countDownNotification", "距离考试时间"+data.getExtras().getString("targetTime")+"还有" + DataUtils.twoDateDistance(startDate, endDate) + "天");

                    break;

            }
        }
    }

    private void updataImage() {
        BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        String picPath = new File(BMOB_IAMGE_CACHE+bmobUser.getObjectId()+".jpg").toString();
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Message msg = new Message();
                    msg.what = 111;
                    msg.obj = bmobFile.getFileUrl();
                    handler.sendMessage(msg);
//                  bmobFile.getFileUrl();//--返回的上传文件的完整地址
                    T.shortShow(getActivity(),"图片上传成功");
                }else{
                    T.shortShow(getActivity(),"图片上传失败:" + e.getMessage());
                }
            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
//        BmobFile file=new BmobFile(bmobUser.getObjectId()+".jpg",null,new File(BMOB_IAMGE_CACHE+bmobUser.getObjectId()+".jpg").toString());
        BmobFile file = new BmobFile(bmobUser.getObjectId() + ".jpg", null, "http://bmob-cdn-11005.b0.upaiyun.com/2017/04/29/6e9b8d3ca8944a4c9a28809b701b5665.jpg");

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    MyUser newUser = new MyUser();
                    BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                    //用户关联地址一定要是url网络地址
                    String url = (String) msg.obj;
                    BmobFile file = new BmobFile(bmobUser.getObjectId() + ".jpg", null,url );
                    newUser.setImg(file);
                    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                T.shortShow(getActivity(),"更新用户信息成功");
                            }else{
                                T.shortShow(getActivity(),"更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });
                    break;
            }
        }
    };

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) throws IOException {
        Bundle bundle = data.getExtras();
        BmobUser user =  BmobUser.getCurrentUser(MyUser.class);
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
            UtilTools.saveImageToSD(bitmap,user.getObjectId()+".jpg");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(getActivity(), profile_image);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isBind = ShareUtils.getBoolean(getActivity(), "idBind", false);
        if (isBind) {
            btn_bind.setText("更改绑定");
        } else {
            ShareUtils.putString(getActivity(), "mima", "");
            ShareUtils.putString(getActivity(), "year", "");
            ShareUtils.putString(getActivity(), "xuehao", "");
            btn_bind.setText("绑定学校账号");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        L.e(v.getId() + "");
        btn_commit.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.et_desc:
                edt_desc.setFocusableInTouchMode(true);
                //设置为默认背景
                edt_desc.setBackground(edt_defaultBK);
                break;
            case R.id.et_sex:
                edt_sex.setFocusableInTouchMode(true);
                edt_sex.setBackground(edt_defaultBK);
                break;
            case R.id.edit_user:
                edt_name.setFocusableInTouchMode(true);
                edt_name.setBackground(edt_defaultBK);
                break;
        }
        return true;
    }

    //通知
    private void showNormalNotification(String leftTime) {

        builder = new Notification.Builder(getActivity());//创建builder对象
        //指定点击通知后的动作，软件界面
        mIntent = new Intent(getActivity(), BottomNavigationActivity.class);
        pendingIntent = PendingIntent.getActivity(getActivity(), 0, mIntent, 0);
        builder.setContentIntent(pendingIntent); //跳转
        builder.setSmallIcon(R.drawable.classroom);//小图标
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true); //顾名思义，左右滑动可删除通知
        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        builder.setContentTitle("考试倒计时");
//        builder.setSubText(targetTime);
        builder.setContentText(leftTime);
        notificationManager.notify(0, builder.build());
    }
}
