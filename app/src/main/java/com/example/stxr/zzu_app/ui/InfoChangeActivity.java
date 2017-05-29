package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   InfoChangeActivity
 *  创建者:   Stxr
 *  创建时间:  2017/5/28 11:09
 *  描述：    资料更改
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.utils.UtilTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;

import static com.example.stxr.zzu_app.statics.StaticConstant.BMOB_IAMGE_CACHE;
import static com.example.stxr.zzu_app.statics.StaticConstant.GET_ZOOM_PHOTO;
import static com.example.stxr.zzu_app.statics.StaticConstant.SET_NAME_CODE;
import static com.example.stxr.zzu_app.statics.StaticConstant.SET_SPECIALITY_CODE;

public class InfoChangeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_sex;
    private TextView tv_name;
    private TextView tv_speciality;
    private ImageView iv_profile;
    private LinearLayout ll_sex;
    private LinearLayout ll_name;
    private LinearLayout ll_speciality;
    private LinearLayout ll_profile;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infochagne);
        initView();
        initData();
    }

    private void initData() {
        ll_profile.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        ll_speciality.setOnClickListener(this);
        ll_sex.setOnClickListener(this);

        tv_speciality.setText(getIntent().getStringExtra("speciality"));
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_sex.setText(getIntent().getStringExtra("sex"));
        Bitmap bitmap = getIntent().getParcelableExtra("image");
        iv_profile.setImageBitmap(bitmap);
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_userName1);
        tv_speciality = (TextView) findViewById(R.id.tv_speciality);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        ll_name = (LinearLayout) findViewById(R.id.ll_userName);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        ll_speciality = (LinearLayout) findViewById(R.id.ll_userSpeciality);
        ll_profile = (LinearLayout) findViewById(R.id.ll_userProfile);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //头像
            case R.id.ll_userProfile:
                callGallery();
                break;
            //名字更改
            case R.id.ll_userName:
                intent = new Intent(InfoChangeActivity.this, ChangeNameAcitvity.class);
                intent.putExtra("name", tv_name.getText().toString().trim());
                intent.putExtra("id", "changeName");
                startActivityForResult(intent, SET_NAME_CODE);
                break;
            //性别
            case R.id.ll_sex:
                sexSelected();
                break;
            //专业
            case R.id.ll_userSpeciality:
                intent = new Intent(InfoChangeActivity.this, ChangeNameAcitvity.class);
                intent.putExtra("speciality", tv_speciality.getText().toString().trim());
                intent.putExtra("id", "changeSpeciality");
                startActivityForResult(intent, SET_SPECIALITY_CODE);
                break;

        }
    }

    private void sexSelected() {
        //选择默认选中的框
        int select = tv_sex.getText().toString().trim().equals("男")?0:1;
        new AlertDialog.Builder(this).setSingleChoiceItems(
                new String[]{"男", "女"}, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_sex.setText(which==0?"男":"女");
                        MyUser user = new MyUser();
                        user.setSex(which==0);
                        updataUser(user);
                        dialog.dismiss();
                    }
                }).create().show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(InfoChangeActivity.this);
//        builder.setTitle("提示");
//        builder.setMessage("确定删除此帖？");
//        builder.setCancelable(false);
//        builder.setNegativeButton("取消", null);
//        builder.setPositiveButton("确定", null);
//        builder.create().show();
    }

    private void callGallery() {
        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(1)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(false)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void setImageToView(Intent data) throws IOException {
        Bundle bundle = data.getExtras();
        BmobUser user = BmobUser.getCurrentUser(MyUser.class);
        if (bundle != null) {
            bitmap = bundle.getParcelable("data");
            iv_profile.setImageBitmap(bitmap);
            UtilTools.saveImageToSD(bitmap, user.getObjectId() + ".jpg");
        }
    }

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
        startActivityForResult(intent, GET_ZOOM_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取设置的名字
            case SET_NAME_CODE:
                if (data != null) {
                    String name = data.getStringExtra("name");
                    tv_name.setText(name);
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    updataUser(user);
                }
                break;
            //获取设置的院系信息
            case SET_SPECIALITY_CODE:
                if (data != null) {
                    String speciality = data.getStringExtra("speciality");
                    tv_speciality.setText(speciality);
                    MyUser user = new MyUser();
                    user.setDesc(speciality);
                    updataUser(user);
                }
                break;
            //获取选择的图片，进行裁剪
            case PhotoPicker.REQUEST_CODE:
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    startPhotoZoom(Uri.parse("file://" + photos.get(0)));//只有一个
                }
                break;
            //设置头像
            case GET_ZOOM_PHOTO:
                //有可能点击舍弃
                if (data != null) {
                    //拿到图片设置
                    try {
                        setImageToView(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updataImage();
//                    //既然已经设置了图片，我们原先的就应该删除
//                    if (tempFile != null) {
//                        tempFile.delete();
//                    }
                }
                break;
        }
    }

    private void updataUser(MyUser user) {
        BmobUser now = BmobUser.getCurrentUser(MyUser.class);
        user.update(now.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    T.shortShow(InfoChangeActivity.this,"更新成功");
                }else{
                    T.shortShow(InfoChangeActivity.this,"更新失败"+e.getMessage());
                }
            }
        });
    }

    private void updataImage() {
        BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        String picPath = new File(BMOB_IAMGE_CACHE + bmobUser.getObjectId() + ".jpg").toString();
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Message msg = new Message();
                    msg.what = 111;
                    msg.obj = bmobFile.getFileUrl();
                    handler.sendMessage(msg);
//                  bmobFile.getFileUrl();//--返回的上传文件的完整地址
                    T.shortShow(InfoChangeActivity.this, "图片上传成功");
                } else {
                    T.shortShow(InfoChangeActivity.this, "图片上传失败:" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    MyUser newUser = new MyUser();
                    BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                    //用户关联地址一定要是url网络地址
                    String url = (String) msg.obj;
                    BmobFile file = new BmobFile(bmobUser.getObjectId() + ".jpg", null, url);
                    newUser.setImg(file);
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                T.shortShow(InfoChangeActivity.this, "更新用户信息成功");
                            } else {
                                T.shortShow(InfoChangeActivity.this, "更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //按了返回键就关掉界面
            case android.R.id.home:
                callBackData();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                callBackData();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    //返回数据
    private void callBackData() {
        Intent intent = new Intent();
        intent.putExtra("name", tv_name.getText().toString().trim());
        intent.putExtra("speciality", tv_speciality.getText().toString().trim());
        intent.putExtra("sex", tv_sex.getText().toString().trim());
        intent.putExtra("image", bitmap);
        setResult(1, intent);
    }

}
