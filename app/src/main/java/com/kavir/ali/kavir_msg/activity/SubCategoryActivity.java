package com.kavir.ali.kavir_msg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.adapters.SubCategoryAdapter;
import com.kavir.ali.kavir_msg.interfaces.ClickListener;
import com.kavir.ali.kavir_msg.models.SubCategoryModel;
import com.kavir.ali.kavir_msg.server.GetDataCategory;
import com.kavir.ali.kavir_msg.utility.RecyclerTouchListener;
import com.kavir.ali.kavir_msg.utility.RetrofitClientInstance;
import com.kavir.ali.kavir_msg.utility.SaveItem;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubCategoryActivity extends Activity implements View.OnClickListener {
    private RecyclerView subCategoryRecycler;
    private SubCategoryAdapter subCategoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar mTopToolbar;
    private Bundle bundle;
    private String parentId;
    private ImageButton backBtn,homeBtn;
    private List<SubCategoryModel> subCategoryModels;
    private String currentSlug =null;
    private String parentName;
    private int currentPageSize =0;
    private LinearLayout notExist;
    private AlertDialog dialog;
    private boolean isSending =false;
    private String lastList =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subCategoryRecycler =(RecyclerView)findViewById(R.id.sub_category_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        homeBtn =(ImageButton)findViewById(R.id.home_sub_btn);
        notExist=(LinearLayout) findViewById(R.id.not_exsit);
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);



        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.please_wait)
                .build();
        /////////
        bundle = getIntent().getExtras();
        if (bundle!=null){
            parentId = bundle.getString("parentId");
            parentName = bundle.getString("parentName");
            ((TextView)findViewById(R.id.parent_cat_tv)).setText(parentName);
        }
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(mTopToolbar);
        ///
        layoutManager = new LinearLayoutManager(this);
        subCategoryRecycler.setLayoutManager(layoutManager);

        getSubCategoryFromServer(parentId);

        subCategoryRecycler.addOnItemTouchListener(new RecyclerTouchListener(this,
                subCategoryRecycler, new ClickListener() {
        @Override
        public void onClick(View view, final int position) {
            dialog.show();
            //Values are passing to activity & to fragment as well
            try{
                currentSlug = subCategoryModels.get(position).getSlug();
                currentPageSize =subCategoryModels.get(position).getPost_count();
                getSubCategoryFromServer(String.valueOf(subCategoryModels.get(position).getId()));
                setCountPostCount(String.valueOf(subCategoryModels.get(position).getId()),subCategoryModels.get(position).getPost_count());
            }catch (Exception e){
                Log.e("Exception:",e.getMessage());
            }
            dialog.dismiss();

        }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(SubCategoryActivity.this, "Long press on position :"+position,Toast.LENGTH_LONG).show();
            }}));

    }

    @Override
    protected void onResume() {
        if (lastList!=null){
            Log.e("slug:",lastList);
            getSubCategoryFromServer(lastList);
        }

        super.onResume();
    }





    private  void getSubCategoryFromServer(final String parentIdf){
        if (isSending){
            return;
        }
        isSending=true;
        dialog.show();
        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        GetDataCategory getDataService=retrofit.create(GetDataCategory.class);
        getDataService.getAllSubCategorys(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),parentIdf).enqueue(new Callback<List<SubCategoryModel>>() {
            @Override
            public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
                if (response.isSuccessful()){
                    subCategoryModels = new ArrayList<>();
                    subCategoryModels = response.body();
                    isSending=false;
                    if (subCategoryModels.get(0).getId()==0){
                        startTitlePostActivity(currentSlug,currentPageSize);
                    }else{
                        lastList = parentIdf;
                        generateDataList(subCategoryModels);
                    }

                    dialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<SubCategoryModel>> call, Throwable t) {
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(SubCategoryActivity.this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getSubCategoryFromServer(parentId);
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
                dialog.dismiss();
                isSending=false;
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void startTitlePostActivity(String slug,int postSize){
        Log.e("startTitlePostActivity:","l188");
        if (slug!=null){
            Intent intent =new Intent(SubCategoryActivity.this,TitlePostsActivity.class);
            intent.putExtra("post_only",false);
            intent.putExtra("slug",slug);
            intent.putExtra("post_size",postSize);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            notExist.setVisibility(View.VISIBLE);
//            hasPost(parentName,postSize);
        }
    }

    private void generateDataList(List<SubCategoryModel> subCategoryModelList) {
        subCategoryAdapter = null;
        subCategoryAdapter =new SubCategoryAdapter(getApplicationContext(),subCategoryModelList);
        subCategoryRecycler.setAdapter(subCategoryAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
            case R.id.home_sub_btn:
                finishAndRemoveTask();
                break;
        }
    }


    private void setCountPostCount(String postId,int newCountPost){
        SaveItem.setItem(getApplicationContext(),"post:"+postId,String.valueOf(newCountPost));

    }
}
