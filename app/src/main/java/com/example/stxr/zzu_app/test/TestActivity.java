package com.example.stxr.zzu_app.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.stxr.zzu_app.R;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    //    private MyEditText medt_test;
    private RichTextEditor redt_test;
    private Button btn_add_image;
    private Button btn_display;
    private Button btn_translate;
    private Subscription subsLoading;
    private Subscription subsInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        redt_test = (RichTextEditor) findViewById(R.id.redt_test);
        btn_display = (Button) findViewById(R.id.btn_display);
        btn_translate = (Button) findViewById(R.id.btn_translate);
        btn_add_image = (Button) findViewById(R.id.btn_add_image);
        btn_display.setOnClickListener(this);
        btn_translate.setOnClickListener(this);
        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGallery();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 112:
                    //用户关联地址一定要是url网络地址
                    String url = (String) msg.obj;
                    L.e(url);
                    break;
            }
        }
    };

    /**
     * 获取数据，保存到数据库做准备
     *
     * @return RichEditText的内容
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editDataList = redt_test.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editDataList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                final BmobFile image = new BmobFile(new File(itemData.imagePath));
                image.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //保存文件
                            ShareUtils.putString(TestActivity.this, image.getFilename(), image.getUrl());
                            T.shortShow(TestActivity.this, "图片上传成功");
                        } else {
                            T.shortShow(TestActivity.this, "图片上传失败:" + e.getMessage());
                        }
                    }
                });
                content.append("<img src =\"").append(itemData.imagePath).append("\"/>");
            }
        }
        //显示保存的字符串 变成html格式了
//        L.e(content.toString());
        return content.toString();
    }

    private void queryPassageData() {
        BmobQuery<PassageTest> query = new BmobQuery<>();
        query.getObject("11894e7060", new QueryListener<PassageTest>() {
            @Override
            public void done(PassageTest passageTest, BmobException e) {
                L.e(passageTest.getContent());
                showDataSync(passageTest.getContent());
            }
        });
    }

    /**
     * 显示数据
     */
    private void showEditData(final Subscriber<? super String> subscriber, final String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                L.e("text" + text);
                if (text.contains("<img")) {
                    final String imagePath = StringUtils.getImgSrc(text);
                    String fileName = StringUtils.getFileName(imagePath) + ".txr";
                    String fileUrl = ShareUtils.getString(TestActivity.this, fileName, null);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        BmobFile file = new BmobFile(fileName, "", fileUrl);
                        file.download(new File(imagePath), new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    T.shortShow(TestActivity.this, "下载成功");
                                    L.e("下载路径：" + s);
                                    subscriber.onNext(s);
                                    //  showEditData(subscriber,html);
                                } else {
                                    T.shortShow(TestActivity.this, "下载失败");
                                }

                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                        subscriber.onNext(imagePath);
                        T.shortShow(TestActivity.this, "图片" + i + "已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }

            }
            subscriber.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    private void savePassageData() {
        PassageTest passageTest = new PassageTest();
        String content = getEditData();
        passageTest.setContent(content);
        L.e(StringUtils.cutStringByImgTag(content).toString());
        passageTest.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    T.shortShow(TestActivity.this, "上传数据成功");
                } else {
                    T.shortShow(TestActivity.this, "上传数据失败" + e.getMessage());
                }
            }
        });
    }

    /**
     * 异步方式显示数据
     *
     * @param html
     */
    private void showDataSync(final String html) {
//        loadingDialog.show();

        subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
//                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

//                        loadingDialog.dismiss();
                        T.shortShow(TestActivity.this, "网络出了点小问题，请刷新重试" + e.getMessage());
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtil.getPictureDir())) {
                            redt_test.addImageViewAtIndex(redt_test.getLastIndex(), text);
                        } else {
                            redt_test.addEditTextAtIndex(redt_test.getLastIndex(), text);
                        }
                    }
                });
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
                    redt_test.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(TestActivity.this);
                    int height = ScreenUtils.getScreenHeight(TestActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
//                        final BmobFile image = new BmobFile(new File(imagePath));
//                        image.uploadblock(new UploadFileListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if(e==null){
//                                    Message msg = new Message();
//                                    msg.what = 112;
//                                    msg.obj = image.getFileUrl()+"\n"+
//                                                image.getUrl()+"\n"+
//                                                image.getFilename();
//                                    //保存文件
//                                    ShareUtils.putString(TestActivity.this,image.getFilename(),image.getUrl());
//                                    handler.sendMessage(msg);
//                                    T.shortShow(TestActivity.this,"图片上传成功");
//                                }else{
//                                    T.shortShow(TestActivity.this,"图片上传失败:" + e.getMessage());
//                                }
//                            }
//                        });
                        //Log.i("NewActivity", "###imagePath="+imagePath);
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
                        redt_test.addEditTextAtIndex(redt_test.getLastIndex(), " ");
                        L.e("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        insertDialog.dismiss();
                        L.e("图片插入失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        redt_test.insertImage(imagePath, redt_test.getMeasuredWidth());
                    }
                });
    }

    private void callGallery() {
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //显示数据
            case R.id.btn_display:
                savePassageData();
                break;
            //解析数据
            case R.id.btn_translate:
                queryPassageData();
                break;
        }
    }
}
