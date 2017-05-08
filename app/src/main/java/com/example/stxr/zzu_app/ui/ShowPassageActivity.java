package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragment
 *  文件名:   ShowPassageActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/22 19:49
 *  描述：    
 */

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CommitAdapter;
import com.example.stxr.zzu_app.bean.Comments;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.StringUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.xrichtext.RichTextView;
import com.example.stxr.zzu_app.xrichtext.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowPassageActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_showReply;
    private TextView tv_showTitle;
//    private TextView tv_showContent;
    private RichTextView rtv_showContent;
    private RecyclerView rv_showComments;
    private LinearLayout ll_comment_send;
    private Button btn_comment_send;
    private EditText edt_comment;
    private List<Comments> commentsList = new ArrayList<>();
    private Subscription subsLoading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpassage);
        initView();
        initData();
        downloadComment();
    }

    private void initData() {
        tv_showTitle.setText(getIntent().getStringExtra("title"));
//        rtv_showContent.setText(getIntent().getStringExtra("content"));
//        rtv_showContent.createTextView()
        showDataSync(getIntent().getStringExtra("content"));
    }

    private void initView() {
        rtv_showContent = (RichTextView) findViewById(R.id.rtv_showContent);
        tv_showTitle = (TextView) findViewById(R.id.tv_showTitle);
        tv_showReply = (TextView) findViewById(R.id.tv_showReply);
        rv_showComments = (RecyclerView) findViewById(R.id.rv_showComments);
        ll_comment_send = (LinearLayout) findViewById(R.id.ll_comment_send);
        btn_comment_send = (Button) findViewById(R.id.btn_comment_send);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        tv_showReply.setOnClickListener(this);
        btn_comment_send.setOnClickListener(this);
        //解决RecycleView 和ScrollView嵌套时的卡的问题
        rv_showComments.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //评论回复
            case R.id.tv_showReply:
                //显示回复的输入框
                tv_showReply.setVisibility(View.GONE);
                ll_comment_send.setVisibility(View.VISIBLE);
                break;
            //发送回复
            case R.id.btn_comment_send:
                String text = edt_comment.getText().toString().trim();
                Comments comments = new Comments();
                //如果输入的不为空
                if (!TextUtils.isEmpty(text)) {
                    showComment(commit(text));
                    //恢复成原来的样子
                    edt_comment.setText("");
                    tv_showReply.setVisibility(View.VISIBLE);
                    ll_comment_send.setVisibility(View.GONE);
                    //隐藏输入框
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    L.i("回复不能为空");
                    T.shortShow(this, "回复内容不能为空");
                }
                break;
        }
    }

    //上传回复的数据
    private Comments commit(String text) {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        MyBBS post = new MyBBS();
        post.setObjectId(getIntent().getStringExtra("ObjectID"));
        Comments comments = new Comments();
        comments.setAuthor(user);
        comments.setContents(text);
        comments.setPost(post);
        comments.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    T.shortShow(ShowPassageActivity.this, "发表成功");
                } else {
                    T.shortShow(ShowPassageActivity.this, "发表失败" + e.getMessage());
                }
            }
        });
        return comments;
    }

    //显示回复的数据
    private void showComment(Comments comments) {
        if (comments != null) {
            commentsList.add(comments);
        }
        LinearLayoutManager layout = new LinearLayoutManager(this);
//        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//        layout.setReverseLayout(true);//列表翻转
        rv_showComments.setLayoutManager(layout);
        CommitAdapter commitAdapter = new CommitAdapter(this, commentsList);
        rv_showComments.setAdapter(commitAdapter);
    }

    //从服务器上下载评论
    private void downloadComment() {
        BmobQuery<Comments> query = new BmobQuery<Comments>();
        MyBBS post = new MyBBS();
        post.setObjectId(getIntent().getStringExtra("ObjectID"));
        //时间倒序
        //query.order("-updatedAt");
        //查询当前帖子所有的评论
        query.addWhereEqualTo("post", new BmobPointer(post));
        //查询发评论的作者和帖子的作者
        query.include("author,post.author");
        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                LinearLayoutManager layout = new LinearLayoutManager(ShowPassageActivity.this);
//                layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//                layout.setReverseLayout(true);//列表翻转
                rv_showComments.setLayoutManager(layout);
                CommitAdapter commitAdapter = new CommitAdapter(ShowPassageActivity.this, list);
                rv_showComments.setAdapter(commitAdapter);
                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("comment", (ArrayList<? extends Parcelable>) list);
//                message.setData(bundle);
                message.what = 321;
                message.obj = list;
                handler.sendMessage(message);
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 321:
                    List<Comments> list = (List<Comments>) msg.obj;
                    commentsList.addAll(list);
                    break;
            }
        }
    };
    /**
     * 显示数据
     * @param html
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
                    String fileUrl = ShareUtils.getString(ShowPassageActivity.this, fileName, null);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        BmobFile file = new BmobFile(fileName, "", fileUrl);
                        file.download(new File(imagePath), new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    T.shortShow(ShowPassageActivity.this, "下载成功");
                                    L.e("下载路径：" + s);
//                                    subscriber.onNext(s);
                                    //  showEditData(subscriber,html);
                                } else {
                                    T.shortShow(ShowPassageActivity.this, "下载失败");
                                }
                            }
                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
//                        subscriber.onNext(imagePath);
                        T.shortShow(ShowPassageActivity.this, "图片" + i + "已丢失，请重新插入！");
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
    /**
     * 异步方式显示数据
     * @param html
     */
    private void showDataSync(final String html){
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
                        e.printStackTrace();
                        T.shortShow(ShowPassageActivity.this,"解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtil.getPictureDir())){
                            rtv_showContent.addImageViewAtIndex(rtv_showContent.getLastIndex(), text);
                        } else {
                            rtv_showContent.addTextViewAtIndex(rtv_showContent.getLastIndex(), text);
                        }
                    }
                });
    }
}
