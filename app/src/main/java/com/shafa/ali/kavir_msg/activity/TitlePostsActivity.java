package com.shafa.ali.kavir_msg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.TitleAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.RecyclerTouchListener;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.valdesekamdem.library.mdtoast.MDToast;

import customview.PaginationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TitlePostsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageButton backBtn,homeBtn;
    private String slugName;
    private TiltlePostsModel tiltlePostsModel;
    private SpinKitView loading;
    private CardView notExistPostcardView;
    private PaginationView paginationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_posts);
        recyclerView =(RecyclerView)findViewById(R.id.posts_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        TextView slugTitle = (TextView)findViewById(R.id.slug_title);
        loading =(SpinKitView)findViewById(R.id.spin_title_post);
        homeBtn =(ImageButton)findViewById(R.id.home_title_post_btn);
        notExistPostcardView =(CardView)findViewById(R.id.not_exist_post_card);
        paginationView =(PaginationView) findViewById(R.id.pager_size_spinner);

        backBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        slugName = getIntent().getExtras().getString("slug");
        slugTitle.setText(slugName);// set name slug in toolbar
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        getDataFromServer(1,10);

        paginationView.setPager(getIntent().getExtras().getInt("post_size"));
        paginationView.setOnPagerUpdate(new PaginationView.OnPagerUpdate() {
            @Override
            public void onUpdate(int pageNumber, int pageSize) {
                getDataFromServer(pageNumber+1,pageSize);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TiltlePostsModel.PostsModel postsModel = tiltlePostsModel.getPostsModels().get(position);
                Intent intent = new Intent(TitlePostsActivity.this,PostActivity.class);
                intent.putExtra("postId",postsModel.getId());
                intent.putExtra("source","net");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getDataFromServer(int pageNumber ,int pageSize) {
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getTiltlePosts(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),slugName,String.valueOf(pageNumber),String.valueOf(pageSize)).enqueue(new Callback<TiltlePostsModel>() {
            @Override
            public void onResponse(Call<TiltlePostsModel> call, Response<TiltlePostsModel> response) {
                if (response.isSuccessful()){
                    tiltlePostsModel = response.body();
                    if (tiltlePostsModel.getPostsModels() == null || tiltlePostsModel.getPostsModels().size()==0){
                        fininshActivity();
                        return;
                    }
                    notExistPostcardView.setVisibility(View.GONE);
                    TitleAdapter titleAdapter = new TitleAdapter(TitlePostsActivity.this,tiltlePostsModel.getPostsModels());
                    recyclerView.setAdapter(titleAdapter);
                    loading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TiltlePostsModel> call, Throwable t) {
                fininshActivity();
            }
        });
    }

    private void fininshActivity() {
        MDToast.makeText(this,getString(R.string.no_exit_post),2500,MDToast.TYPE_INFO).show();
        loading.setVisibility(View.GONE);
        notExistPostcardView.setVisibility(View.VISIBLE);
//        finish();
    }

    private void startHomePage(){
        startActivity(new Intent(TitlePostsActivity.this,CategoryActivity.class));
        finish();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
//                onBackPressed();
                finish();
                break;
            case R.id.home_title_post_btn:
                startHomePage();
                break;
        }
    }


}
