package com.shafa.ali.kavir_msg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.SubCategoryAdapter;
import com.shafa.ali.kavir_msg.adapters.TitleAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.SubCategoryModel;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;
import com.shafa.ali.kavir_msg.server.GetDataSubCategory;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.RecyclerTouchListener;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Setting;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
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
//                getSubCategoryFromServer(subCategoryModels.get(position).getSlug());


            }
            dialog.dismiss();

//            startTitlePostCategory(String.valueOf(subCategoryModels.get(position).getId()));
//            Toast.makeText(SubCategoryActivity.this, "Single Click on position        :"+position, Toast.LENGTH_SHORT).show();
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



    private void hasPost(final String slug, final int postSize){
        dialog.show();
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        getPostsServer.getTiltlePosts(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),slug,"0","10").enqueue(new Callback<TiltlePostsModel>() {
            @Override
            public void onResponse(Call<TiltlePostsModel> call, Response<TiltlePostsModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getPostsModels()!= null && response.body().getPostsModels().size()>0){
                        Intent intent =new Intent(SubCategoryActivity.this,TitlePostsActivity.class);
                        intent.putExtra("slug",slug);
                        intent.putExtra("post_size",postSize);
                        lastList =null;
                        if (!Setting.isVistied){
                            Setting.isVistied  = true;
                            Log.e("isVistied:","false");
                            startActivity(intent);
                        }



//                        dialog.dismiss();
                        finish();
                        return;
                    }
                }
                MDToast.makeText(SubCategoryActivity.this,getString(R.string.not_exit_subcat),3000,MDToast.TYPE_WARNING).show();
                startHomePage();

            }

            @Override
            public void onFailure(Call<TiltlePostsModel> call, Throwable t) {
                MDToast.makeText(SubCategoryActivity.this,getString(R.string.not_exit_subcat),3000,MDToast.TYPE_WARNING).show();
                startHomePage();
            }
        });
    }


    private void startHomePage(){
        startActivity(new Intent(SubCategoryActivity.this,CategoryActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startHomePage();
        super.onBackPressed();
    }

    private  void getSubCategoryFromServer(final String parentIdf){
        if (isSending){
            return;
        }
        isSending=true;
        dialog.show();
        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        GetDataSubCategory getDataService=retrofit.create(GetDataSubCategory.class);
        getDataService.getAllSubCategorys(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),parentIdf).enqueue(new Callback<List<SubCategoryModel>>() {
            @Override
            public void onResponse(Call<List<SubCategoryModel>> call, Response<List<SubCategoryModel>> response) {
                if (response.isSuccessful()){
                    subCategoryModels = new ArrayList<>();
                    subCategoryModels = response.body();

                    if (subCategoryModels.get(0).getId()==0){
                        startTitlePostActivity(currentSlug,currentPageSize);
                    }else{
                        Log.e("cat parentIdf:",parentIdf);
                        lastList = parentIdf;
                        generateDataList(subCategoryModels);
                    }

                    dialog.dismiss();
                    isSending=false;

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
        if (slug!=null){
            Intent intent =new Intent(SubCategoryActivity.this,TitlePostsActivity.class);
            intent.putExtra("slug",slug);
            intent.putExtra("post_size",postSize);
            startActivity(intent);
        }else {
//            MDToast.makeText(this,getString(R.string.not_exit_subcat),3000,MDToast.TYPE_WARNING).show();
////            startHomePage();
            notExist.setVisibility(View.VISIBLE);
            hasPost(parentName,postSize);
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
                startHomePage();
                break;
        }
    }


    private void setCountPostCount(String postId,int newCountPost){
        SaveItem.setItem(getApplicationContext(),"post:"+postId,String.valueOf(newCountPost));

    }
}
