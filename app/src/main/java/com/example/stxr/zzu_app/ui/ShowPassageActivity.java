package com.example.stxr.zzu_app.ui;/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.fragment
 *  文件名:   ShowPassageActivity
 *  创建者:   Stxr
 *  创建时间:  2017/4/22 19:49
 *  描述：    
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stxr.zzu_app.R;
import com.example.stxr.zzu_app.adapter.CommitAdapter;
import com.example.stxr.zzu_app.adapter.ReplyAdapter;
import com.example.stxr.zzu_app.bean.Comments;
import com.example.stxr.zzu_app.bean.MyBBS;
import com.example.stxr.zzu_app.bean.MyUser;
import com.example.stxr.zzu_app.bean.Reply;
import com.example.stxr.zzu_app.utils.L;
import com.example.stxr.zzu_app.utils.ShareUtils;
import com.example.stxr.zzu_app.utils.StringUtils;
import com.example.stxr.zzu_app.utils.T;
import com.example.stxr.zzu_app.view.CustomDialog;
import com.example.stxr.zzu_app.xrichtext.RichTextView;
import com.example.stxr.zzu_app.xrichtext.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
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
    private TextView tv_creatTime;
    private TextView tv_author;
    private List<Comments> commentsList = new ArrayList<>();
    private List<Reply> replies;
    private Subscription subsLoading;
    private SwipeRefreshLayout srl_passage_refresh;
    private NestedScrollView scrollView;
    private InputMethodManager imm;
    private FrameLayout fl_commit;
    private View.OnClickListener replyCommentsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
//            T.shortShow(ShowPassageActivity.this, "点击了回复" + commentsList.get(position).getObjectId());
        }
    };
    private ReplyAdapter.OnRecyclerViewItemClickListener itemClickListener = new ReplyAdapter.OnRecyclerViewItemClickListener() {

        @Override
        public void onItemClick(View view, final Reply reply) {
            final int parentPosition = (Integer) view.getTag(R.id.replyParentPosition);
            final Comments comments = commentsList.get(parentPosition);
            if (!Objects.equals(reply.getName(), BmobUser.getCurrentUser(MyUser.class).getUsername())) { //不能回复自己
                showInput();
                btn_comment_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = edt_comment.getText().toString().trim();
//                    T.shortShow(ShowPassageActivity.this, "回复的内容是" + reply.getContent()+"父位置id是"+comments.getObjectId());
                        if (!TextUtils.isEmpty(text)) {
                            Reply r = new Reply();
                            r.setContent(text);
                            r.setName(BmobUser.getCurrentUser(MyUser.class).getUsername());
                            r.setToName(reply.getName());
                            comments.getReplyList().add(r);
                            comments.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        T.shortShow(ShowPassageActivity.this, "评论回复成功");
                                    } else {
                                        T.shortShow(ShowPassageActivity.this, "评论回复失败" + e.getMessage());
                                    }
                                }
                            });
                            //恢复成原来的样子
                            hideInput();
                            //显示
                            LinearLayoutManager layout = new LinearLayoutManager(ShowPassageActivity.this);
                            rv_showComments.setLayoutManager(layout);
                            CommitAdapter commitAdapter = new CommitAdapter(ShowPassageActivity.this, commentsList, ShowPassageActivity.this, itemClickListener);
                            rv_showComments.setAdapter(commitAdapter);

                        } else {
                            L.i("回复不能为空");
                            T.shortShow(ShowPassageActivity.this, "回复内容不能为空");
                        }
                    }
                });
            }
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpassage);
        initView();
        initData();
        downloadComment();
    }

    private void initData() {
        imm = (InputMethodManager) getSystemService(ShowPassageActivity.INPUT_METHOD_SERVICE);
        tv_showTitle.setText(getIntent().getStringExtra("title"));
        tv_author.setText(getIntent().getStringExtra("username"));
        tv_creatTime.setText(getIntent().getStringExtra("createdTime"));
        updataPassage();
        //下拉刷新
        srl_passage_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updataPassage();
                srl_passage_refresh.setRefreshing(false);
            }
        });
        //解决scrollView和SwipeRefreshLayout冲突问题
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (srl_passage_refresh != null) {
                        srl_passage_refresh.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }
        fl_commit.setVisibility(View.INVISIBLE);
    }

    private void updataPassage() {
        rtv_showContent.post(new Runnable() {
            @Override
            public void run() {
                rtv_showContent.clearAllLayout();
                showDataSync(getIntent().getStringExtra("content"));
            }
        });

    }

    private void initView() {
        fl_commit = (FrameLayout) findViewById(R.id.fl_commit);
        srl_passage_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_passage_refresh);
        rtv_showContent = (RichTextView) findViewById(R.id.rtv_showContent);
        tv_creatTime = (TextView) findViewById(R.id.tv_creatTime);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_showTitle = (TextView) findViewById(R.id.tv_showTitle);
        tv_showReply = (TextView) findViewById(R.id.tv_showReply);
        rv_showComments = (RecyclerView) findViewById(R.id.rv_showComments);
        ll_comment_send = (LinearLayout) findViewById(R.id.ll_comment_send);
        btn_comment_send = (Button) findViewById(R.id.btn_comment_send);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        tv_showReply.setOnClickListener(this);
        //解决RecycleView 和ScrollView嵌套时的卡的问题
        rv_showComments.setNestedScrollingEnabled(false);
        //防止更新内容时候定位到底部
        rv_showComments.setFocusable(false);
        scrollView = (NestedScrollView) findViewById(R.id.sv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //评论回复
            case R.id.tv_reply:
                int position = (Integer) v.getTag();
//                T.shortShow(ShowPassageActivity.this, "点击了回复" + commentsList.get(position).getObjectId());
                reply(position);
                break;
            //帖子回复
            case R.id.tv_showReply:
                //显示回复的输入框
                reply(-1);
                break;
        }
    }
    private void showInput(){
        tv_showReply.setVisibility(View.GONE);
        ll_comment_send.setVisibility(View.VISIBLE);
        edt_comment.requestFocus();
        if (imm != null) {
            imm.showSoftInput(edt_comment,0);
        }
    }
    private void hideInput() {
        edt_comment.setText("");
        tv_showReply.setVisibility(View.VISIBLE);
        ll_comment_send.setVisibility(View.GONE);
        //隐藏输入框
        //  InputMethodManager imm = (InputMethodManager) getSystemService(ShowPassageActivity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void reply(final int position) {
        //显示回复文本框
        showInput();
        btn_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edt_comment.getText().toString().trim();
                Comments comments = new Comments();
                switch (position) {
                    //回复的是帖子
                    case -1:
                        //如果输入的不为空
                        if (!TextUtils.isEmpty(text)) {
                            //添加并上传评论
                            showComment(commit(text));
                            //恢复成原来的样子
                            hideInput();
                        } else {
                            L.i("回复不能为空");
                            T.shortShow(ShowPassageActivity.this, "回复内容不能为空");
                        }
                        break;
                    //回复的是回复帖子的回复
                    default:
                        if (!TextUtils.isEmpty(text)) {
                            Reply reply = new Reply();
                            Comments c = commentsList.get(position);
                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
                            reply.setName(user.getUsername());
                            reply.setToName(" ");
                            reply.setContent(text);
                            c.getReplyList().add(reply);
                            c.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        T.shortShow(ShowPassageActivity.this, "评论发表成功");
                                    } else {
                                        T.shortShow(ShowPassageActivity.this, "评论发表失败" + e.getMessage());
                                    }
                                }
                            });
                            //恢复成原来的样子
                            hideInput();
                            //显示
                            LinearLayoutManager layout = new LinearLayoutManager(ShowPassageActivity.this);
                            rv_showComments.setLayoutManager(layout);
                            CommitAdapter commitAdapter = new CommitAdapter(ShowPassageActivity.this, commentsList, ShowPassageActivity.this, itemClickListener);

                        } else {
                            L.i("回复不能为空");
                            T.shortShow(ShowPassageActivity.this, "回复内容不能为空");
                        }
                        break;
                }
            }
        });
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
        CommitAdapter commitAdapter = new CommitAdapter(this, commentsList, this, itemClickListener);
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
//        boolean isCache = query.hasCachedResult(Comments.class);
//        if(isCache){ //--此为举个例子，并不一定按这种方式来设置缓存策略
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        }else{
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (list.size() != 0) {
                    fl_commit.setVisibility(View.VISIBLE);
                }
                LinearLayoutManager layout = new LinearLayoutManager(ShowPassageActivity.this);
//                layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//                layout.setReverseLayout(true);//列表翻转
                rv_showComments.setLayoutManager(layout);
                CommitAdapter commitAdapter = new CommitAdapter(ShowPassageActivity.this, list, ShowPassageActivity.this, itemClickListener);
                rv_showComments.setAdapter(commitAdapter);
                Message message = new Message();
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
                    if (commentsList != null && list != null) {
                        commentsList.addAll(list);
                    }
                    break;
                case 111:
                    updataPassage();
                    break;

            }
        }
    };

    /**
     * 显示数据
     *
     * @param html
     */
    private void showEditData(final Subscriber<? super String> subscriber, final String html) {
        try {
            final List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                final String text = textList.get(i);
                final int finalI = i;
                L.e("text" + text);
                if (text.contains("<img")) {
                    final String imagePath = StringUtils.getImgSrc(text);
                    final String fileName = StringUtils.getFileName(imagePath) + ".txr";
//                    String fileUrl = ShareUtils.getString(ShowPassageActivity.this, fileName, null);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        final BmobQuery<MyBBS> bbsquery = new BmobQuery<>();
                        bbsquery.getObject(getIntent().getStringExtra("ObjectID"), new QueryListener<MyBBS>() {
                            @Override
                            public void done(MyBBS myBBS, BmobException e) {
                                if (e == null) {
                                    final List<BmobFile> bmobFiles = myBBS.getPicList();
                                    for (final BmobFile file : bmobFiles) {
                                        if (imagePath.contains(file.getFilename())) {
                                            file.download(new File(imagePath), new DownloadFileListener() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e == null) {
                                                        if (file.equals(bmobFiles.get(bmobFiles.size() - 1))) {
                                                            handler.sendEmptyMessageDelayed(111,100);

                                                        }
                                                    } else {
                                                        T.shortShow(ShowPassageActivity.this, "下载失败");
                                                    }
                                                }

                                                @Override
                                                public void onProgress(Integer integer, long l) {

                                                }
                                            });
                                        }


                                    }
                                }
                            }
                        });
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
                        e.printStackTrace();
                        T.shortShow(ShowPassageActivity.this, "图片不存在或已损坏，请刷新重试");
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtil.getPictureDir())) {
                            rtv_showContent.addImageViewAtIndex(rtv_showContent.getLastIndex(), text);
                        } else {
                            rtv_showContent.addTextViewAtIndex(rtv_showContent.getLastIndex(), text);
                        }
                    }
                });
    }
}
