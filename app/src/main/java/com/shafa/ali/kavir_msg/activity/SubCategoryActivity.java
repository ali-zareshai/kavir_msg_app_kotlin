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
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.adapters.SubCategoryAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.models.SubCategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.server.GetDataSubCategory;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;

import java.util.List;

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
    private ImageButton backBtn;
    private List<SubCategoryModel> subCategoryModels;
    private SpinKitView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subCategoryRecycler =(RecyclerView)findViewById(R.id.sub_category_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        loading =(SpinKitView)findViewById(R.id.spin_sub_cat);
        backBtn.setOnClickListener(this);
        /////////
        bundle = getIntent().getExtras();
        if (bundle!=null){
            parentId = bundle.getString("parentId");
        }
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(mTopToolbar);
        ///
        layoutManager = new LinearLayoutManager(this);
        subCategoryRecycler.setLayoutManager(layoutManager);

        //
        /*Create handle for the RetrofitInstance interface*/
        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        GetDataSubCategory getDataService=retrofit.create(GetDataSubCategory.class);
        getDataService.getAllSubCategorys(parentId).enqueue(new Callback<List<SubCategoryModel>>() {
            @Override
            public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
//                Log.e("msg",response.body().toString());
                if (response.isSuccessful()){
                    subCategoryModels = response.body();
                    generateDataList(subCategoryModels);
                    loading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<SubCategoryModel>> call, Throwable t) {
                Toast.makeText(SubCategoryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        subCategoryRecycler.addOnItemTouchListener(new SubCategoryActivity.RecyclerTouchListener(this,
                subCategoryRecycler, new ClickListener() {
        @Override
        public void onClick(View view, final int position) {
            //Values are passing to activity & to fragment as well
            String slug = subCategoryModels.get(position).getSlug();
            Intent intent =new Intent(SubCategoryActivity.this,TitlePostsActivity.class);
            intent.putExtra("slug",slug);
            startActivity(intent);
//            Toast.makeText(SubCategoryActivity.this, "Single Click on position        :"+position, Toast.LENGTH_SHORT).show();
        }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(SubCategoryActivity.this, "Long press on position :"+position,Toast.LENGTH_LONG).show();
            }}));

    }

    private void generateDataList(List<SubCategoryModel> subCategoryModelList) {
        subCategoryAdapter =new SubCategoryAdapter(getApplicationContext(),subCategoryModelList);
        subCategoryRecycler.setAdapter(subCategoryAdapter);
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
