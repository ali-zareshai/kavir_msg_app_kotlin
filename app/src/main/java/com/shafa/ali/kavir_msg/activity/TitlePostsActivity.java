package com.shafa.ali.kavir_msg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TitlePostsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageButton backBtn,nextPageBtn,prePageBtn;
    private TextView pageNumberTv,totalPageTv;
    private String slugName;
    private int pageNumber = 1;
    private TiltlePostsModel tiltlePostsModel;
    private SpinKitView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_posts);
        recyclerView =(RecyclerView)findViewById(R.id.posts_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        TextView slugTitle = (TextView)findViewById(R.id.slug_title);
        nextPageBtn =(ImageButton)findViewById(R.id.next_page);
        prePageBtn =(ImageButton)findViewById(R.id.pre_page);
        pageNumberTv = (TextView)findViewById(R.id.page_number);
        totalPageTv =(TextView)findViewById(R.id.total_page_number);
        loading =(SpinKitView)findViewById(R.id.spin_title_post);

        backBtn.setOnClickListener(this);
        prePageBtn.setOnClickListener(this);
        nextPageBtn.setOnClickListener(this);

        slugName = getIntent().getExtras().getString("slug");
        slugTitle.setText(slugName);// set name slug in toolbar
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getDataFromServer();

        recyclerView.addOnItemTouchListener(new TitlePostsActivity.RecyclerTouchListener(this, recyclerView, new ClickListener() {
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

    private void getDataFromServer() {
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getTiltlePosts(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),slugName,String.valueOf(pageNumber)).enqueue(new Callback<TiltlePostsModel>() {
            @Override
            public void onResponse(Call<TiltlePostsModel> call, Response<TiltlePostsModel> response) {
                if (response.isSuccessful()){
                    tiltlePostsModel = response.body();
                    TitleAdapter titleAdapter = new TitleAdapter(TitlePostsActivity.this,tiltlePostsModel.getPostsModels());
                    recyclerView.setAdapter(titleAdapter);
                    setPage();
                    loading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TiltlePostsModel> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
            }
        });
    }

    private void setPage() {
        pageNumberTv.setText(String.valueOf(pageNumber));
        totalPageTv.setText(tiltlePostsModel.getPages());
        if (tiltlePostsModel.getPages().equals(String.valueOf(pageNumber))){
            nextPageBtn.setVisibility(View.GONE);
        }else {
            nextPageBtn.setVisibility(View.VISIBLE);
        }
        if (pageNumber == 1){
            prePageBtn.setVisibility(View.GONE);
        }else {
            prePageBtn.setVisibility(View.VISIBLE);
        }
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
                onBackPressed();
                finish();
                break;
            case R.id.next_page:
                pageNumber++;
                getDataFromServer();
                break;
            case R.id.pre_page:
                pageNumber--;
                getDataFromServer();
                break;
        }
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
