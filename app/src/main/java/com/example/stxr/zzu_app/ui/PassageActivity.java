package com.example.stxr.zzu_app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.test.PassageTest;
import com.example.stxr.zzu_app.utils.ImageUtils;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ScreenUtils;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.StringUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.xrichtext.RichTextEditor;
import com.example.stxr.zzu_app.xrichtext.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.ui
 *  文件名:   PassageActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/20 19:44
 *  描述：    
 */
public class PassageActivity extends BaseActivity {
    private EditText edt_title;
    //    private EditText edt_content;
    private RichTextEditor redt_content;
    private Subscription subsLoading;
    private Subscription subsInsert;
    private List<BmobFile> bmobFiles = new ArrayList<>();
    private MyBBS bbs;
    //发送完成的标志位
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        redt_content = (RichTextEditor) findViewById(R.id.redt_content);
    }

    //显示菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //监听菜单操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //上传菜单
            case R.id.menu_push:
                savePassageData();
                break;
            case R.id.menu_insertPic:
                callGallery();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 上传文本数据
     */
    private void savePassageData() {
        String title = edt_title.getText().toString().trim();
        String content = getEditData();
        MyUser author = BmobUser.getCurrentUser(MyUser.class);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            final MyBBS myBBS = new MyBBS();
            myBBS.setContent(content);
            myBBS.setTitle(title);
            myBBS.setAuthor(author);
            myBBS.increment("visits", 0);
            myBBS.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
//                        T.shortShow(PassageActivity.this, "上传数据成功");
                        bbs = myBBS;
                        Message msg = new Message();
                        msg.what = 123;
                        handler.sendMessage(msg);
                    } else {
                        T.shortShow(PassageActivity.this, "上传数据失败" + e.getMessage());
                    }
                }
            });
            setResult(2);
            finish();
        } else {
            T.shortShow(this, "标题或内容不能为空");
        }
    }

    /**
     * 获取数据，保存到数据库做准备
     *
     * @return RichEditText的内容
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editDataList = redt_content.buildEditData();
        StringBuffer content = new StringBuffer();
        int sum = 0;
        for (RichTextEditor.EditData editData : editDataList) {
            if (editData.imagePath != null) {
                sum++;
            }
        }
        for (RichTextEditor.EditData itemData : editDataList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                sum--;
                final BmobFile image = new BmobFile(new File(itemData.imagePath));
                bmobFiles.add(image);
                final int finalSum = sum;
                image.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //保存文件
//                            ShareUtils.putString(PassageActivity.this, image.getFilename(), image.getUrl());
                            if (finalSum == 0) {
                                Message msg = new Message();
                                msg.what = 123;
                                msg.obj = image;
                                handler.sendMessage(msg);
                            }

//                            T.shortShow(PassageActivity.this, "图片上传成功");
                        } else {
                            T.shortShow(PassageActivity.this, "图片上传失败:" + e.getMessage());
                        }
                    }
                });
                content.append("<img src =\"").append(itemData.imagePath).append("\"/>");
            }
        }
        return content.toString();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //图片上传成功
                case 123:
                    flag++;
//                    BmobFile file = (BmobFile) msg.obj;
//                    bmobFiles.add(file);
                    if (flag == 2) {
                        flag = 0;
                        bbs.setPicList(bmobFiles);
                        bbs.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
//                                T.shortShow(PassageActivity.this,"更新成功111");
                                } else {
                                    T.shortShow(PassageActivity.this, "更新失败" + e.getMessage());
                                    L.e("更新失败" + e.getMessage());
                                }
                            }
                        });
                    }
//                    MyUser author = BmobUser.getCurrentUser(MyUser.class);
//                    String title = edt_title.getText().toString().trim();
//                    final MyBBS myBBS = new MyBBS();
                    break;
            }

        }
    };

    private void callGallery() {
        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1) {
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE) {
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     * 插入图片的同时，将图片压缩保存为sd卡指定目录，到要取的时候才取出来
     *
     * @param data
     */
    private void insertImagesSync(final Intent data) {
//        insertDialog.show();
        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    redt_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(PassageActivity.this);
                    int height = ScreenUtils.getScreenHeight(PassageActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
//                        insertDialog.dismiss();
                        redt_content.addEditTextAtIndex(redt_content.getLastIndex(), " ");
                        L.e("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        insertDialog.dismiss();
                        L.e("图片插入失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        redt_content.insertImage(imagePath, redt_content.getMeasuredWidth());
                    }
                });
    }
}
