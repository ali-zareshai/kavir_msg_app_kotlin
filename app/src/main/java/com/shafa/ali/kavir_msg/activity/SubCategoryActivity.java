package com.shafa.ali.kavir_msg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.SubCategoryAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.SubCategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataSubCategory;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView subCategoryRecycler;
    private SubCategoryAdapter subCategoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar mTopToolbar;
    private Bundle bundle;
    private String parentId;
    private ImageButton backBtn,homeBtn;
    private List<SubCategoryModel> subCategoryModels;
    private SpinKitView loading;
    private String currentSlug =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subCategoryRecycler =(RecyclerView)findViewById(R.id.sub_category_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        loading =(SpinKitView)findViewById(R.id.spin_sub_cat);
        homeBtn =(ImageButton)findViewById(R.id.home_sub_btn);
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        /////////
        bundle = getIntent().getExtras();
        if (bundle!=null){
            parentId = bundle.getString("parentId");
            String parentName = bundle.getString("parentName");
            ((TextView)findViewById(R.id.parent_cat_tv)).setText(parentName);
        }
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(mTopToolbar);
        ///
        layoutManager = new LinearLayoutManager(this);
        subCategoryRecycler.setLayoutManager(layoutManager);

        getSubCategoryFromServer(parentId);


        subCategoryRecycler.addOnItemTouchListener(new SubCategoryActivity.RecyclerTouchListener(this,
                subCategoryRecycler, new ClickListener() {
        @Override
        public void onClick(View view, final int position) {
            //Values are passing to activity & to fragment as well
            currentSlug = subCategoryModels.get(position).getSlug();
            getSubCategoryFromServer(String.valueOf(subCategoryModels.get(position).getId()));
            setCountPostCount(String.valueOf(subCategoryModels.get(position).getId()),subCategoryModels.get(position).getPost_count());
//            startTitlePostCategory(String.valueOf(subCategoryModels.get(position).getId()));
//            Toast.makeText(SubCategoryActivity.this, "Single Click on position        :"+position, Toast.LENGTH_SHORT).show();
        }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(SubCategoryActivity.this, "Long press on position :"+position,Toast.LENGTH_LONG).show();
            }}));

    }

    private void startHomePage(){
        startActivity(new Intent(SubCategoryActivity.this,CategoryActivity.class));
        finish();
    }

    private  void getSubCategoryFromServer(String parentIdf){
        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        GetDataSubCategory getDataService=retrofit.create(GetDataSubCategory.class);
        getDataService.getAllSubCategorys(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),parentIdf).enqueue(new Callback<List<SubCategoryModel>>() {
            @Override
            public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
//                Log.e("msg",response.body().toString());
                if (response.isSuccessful()){
                    subCategoryModels = new ArrayList<>();
                    subCategoryModels = response.body();
                    if (subCategoryModels.get(0).getId()==0){
                        startTitlePostCategory(currentSlug);
                    }else{
                        generateDataList(subCategoryModels);
                    }

                }

            }

            @Override
            public void onFailure(Call<List<SubCategoryModel>> call, Throwable t) {
                Log.e("Throwable:",t.toString());
                Toast.makeText(SubCategoryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startTitlePostCategory(String slug){
        Intent intent =new Intent(SubCategoryActivity.this,TitlePostsActivity.class);
        intent.putExtra("slug",slug);
        startActivity(intent);
        finish();
    }

    private void generateDataList(List<SubCategoryModel> subCategoryModelList) {
        subCategoryAdapter = null;
        subCategoryAdapter =new SubCategoryAdapter(getApplicationContext(),subCategoryModelList);
        subCategoryRecycler.setAdapter(subCategoryAdapter);
        loading.setVisibility(View.GONE);
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
            case R.id.home_sub_btn:
                startHomePage();
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

    private void setCountPostCount(String postId,int newCountPost){
        SaveItem.setItem(getApplicationContext(),"post:"+postId,String.valueOf(newCountPost));

    }
}
