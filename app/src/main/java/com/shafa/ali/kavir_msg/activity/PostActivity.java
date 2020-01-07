package com.shafa.ali.kavir_msg.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CommentAdapter;
import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import customview.CustomCommentModal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTv,dateTv,autherTv,commentTv;
    private RecyclerView commentRv;
    private WebView webView;
    private String postId;
    private PostModel postModel;
    private CommentAdapter commentAdapter;
    private ScrollView scrollviewPost;
    private SpinKitView loading;
    private ImageButton backBtn;
    private ImageButton sendComment,showComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initViews();
        getIntents();
        fetchData();
        backBtn.setOnClickListener(this);
        sendComment.setOnClickListener(this);
        showComments.setOnClickListener(this);
    }

    private void fetchData() {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getPost(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),postId).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()){
                    postModel = response.body();
                    setViews();
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
            }
        });
    }

    private void setViews() {
        dateTv.setText(postModel.getDate());
        autherTv.setText(postModel.getAuthor());
        commentTv.setText(getString(R.string.comments)+"("+postModel.getCommentModelList().size()+")");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData("<html dir=\"rtl\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"/>"+postModel.getContent()+"</html>","text/html","UTF-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleTv.setText(Html.fromHtml(postModel.getTitle(),Html.FROM_HTML_MODE_COMPACT));
//            contentTv.setText(Html.fromHtml(postModel.getContent(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            titleTv.setText(Html.fromHtml(postModel.getTitle()));
//            contentTv.setText(Html.fromHtml(postModel.getContent()));
        }
        scrollviewPost.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

    }

    private void setCommentRecycleView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(postModel.getCommentModelList(),this);
        commentRv.setAdapter(commentAdapter);
        commentRv.setVisibility(View.VISIBLE);

    }

    private void getIntents() {
        postId = getIntent().getExtras().getString("postId");
    }

    private void initViews() {
        titleTv = (TextView)findViewById(R.id.title_post_card);
//        contentTv=(TextView)findViewById(R.id.content_post_card);
        dateTv =(TextView)findViewById(R.id.date_post_card);
        autherTv =(TextView)findViewById(R.id.auther_post_card);
        commentRv =(RecyclerView)findViewById(R.id.comment_recyclerview_posts);
        webView =(WebView)findViewById(R.id.webview_post_card);
        scrollviewPost =(ScrollView)findViewById(R.id.scrollview_post);
        loading =(SpinKitView)findViewById(R.id.spin_post);
        backBtn = (ImageButton)findViewById(R.id.back_post_btn);
        sendComment =(ImageButton) findViewById(R.id.add_new_comment);
        showComments=(ImageButton) findViewById(R.id.show_comment);
        commentTv = (TextView)findViewById(R.id.count_comment);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_post_btn:
                finish();
                break;
            case R.id.show_comment:
                setCommentRecycleView();
                break;
            case R.id.add_new_comment:
                new CustomCommentModal().showNewComment(this,postId);
                break;

        }
    }
}
