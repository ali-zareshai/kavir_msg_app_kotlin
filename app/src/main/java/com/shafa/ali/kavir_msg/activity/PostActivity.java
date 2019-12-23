package com.shafa.ali.kavir_msg.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CommentAdapter;
import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity {
    private TextView titleTv,contentTv,dateTv,autherTv;
    private RecyclerView commentRv;
    private String postId;
    private PostModel postModel;
    private CommentAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initViews();
        getIntents();
        fetchData();
    }

    private void fetchData() {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getPost(postId).enqueue(new Callback<PostModel>() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleTv.setText(Html.fromHtml(postModel.getTitle(),Html.FROM_HTML_MODE_COMPACT));
            contentTv.setText(Html.fromHtml(postModel.getContent(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            titleTv.setText(Html.fromHtml(postModel.getTitle()));
            contentTv.setText(Html.fromHtml(postModel.getContent()));
        }
        ///////////// set comment ////////////
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(postModel.getCommentModelList(),this);
        commentRv.setAdapter(commentAdapter);
    }

    private void getIntents() {
        postId = getIntent().getExtras().getString("postId");
    }

    private void initViews() {
        titleTv = (TextView)findViewById(R.id.title_post_card);
        contentTv=(TextView)findViewById(R.id.content_post_card);
        dateTv =(TextView)findViewById(R.id.date_post_card);
        autherTv =(TextView)findViewById(R.id.auther_post_card);
        commentRv =(RecyclerView)findViewById(R.id.comment_recyclerview_posts);
    }
}
