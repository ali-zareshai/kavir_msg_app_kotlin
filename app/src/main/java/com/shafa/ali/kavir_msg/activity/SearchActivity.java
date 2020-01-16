package com.shafa.ali.kavir_msg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.TitleAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.RecyclerTouchListener;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageButton backBtn;
    private List<TiltlePostsModel.PostsModel> postsModelList;
    private FrameLayout searchFram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView =(RecyclerView)findViewById(R.id.search_result_rc);
        searchView = (SearchView)findViewById(R.id.search_dt);
        backBtn = (ImageButton)findViewById(R.id.back_search_btn);
        searchFram=(FrameLayout)findViewById(R.id.frame_et);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFram.setBackgroundColor(Color.WHITE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchFram.setBackgroundColor(Color.parseColor("#d32f2f"));
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getsearchResult(s);
                return false;
            }
        });
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setTextSize(24);
//        searchEditText.setActivated(true);
        Typeface type2 = Typeface.createFromAsset(getAssets(),"fonts/sans.ttf");
        searchEditText.setTypeface(type2,Typeface.NORMAL);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TiltlePostsModel.PostsModel postsModel = postsModelList.get(position);
                Intent intent = new Intent(SearchActivity.this,PostActivity.class);
                intent.putExtra("postId",postsModel.getId());
                intent.putExtra("source","net");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getsearchResult(String query) {
        recyclerView.setVisibility(View.GONE);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getSearch(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),query).enqueue(new Callback<List<TiltlePostsModel.PostsModel>>() {
            @Override
            public void onResponse(Call<List<TiltlePostsModel.PostsModel>> call, Response<List<TiltlePostsModel.PostsModel>> response) {
                if (response.isSuccessful()){
                    TitleAdapter titleAdapter = new TitleAdapter(SearchActivity.this,response.body());
                    postsModelList = response.body();
                    recyclerView.setAdapter(titleAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<TiltlePostsModel.PostsModel>> call, Throwable t) {
//                Log.e("onFailure:",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
