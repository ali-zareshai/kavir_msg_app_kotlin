package com.kavirelectronic.ali.kavir_info.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.kavirelectronic.ali.kavir_info.R;
import com.kavirelectronic.ali.kavir_info.adapters.TitleAdapter;
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener;
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel;
import com.kavirelectronic.ali.kavir_info.server.GetPostsServer;
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener;
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance;
import com.kavirelectronic.ali.kavir_info.utility.SaveItem;
import com.valdesekamdem.library.mdtoast.MDToast;

import customview.PaginationView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TitlePostsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageButton backBtn,homeBtn;
    private String slugName,catId;
    private TiltlePostsModel tiltlePostsModel;
    private CardView notExistPostcardView;
    private PaginationView paginationView;
    private AlertDialog dialog;
    private boolean isPostOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_posts);
        recyclerView =(RecyclerView)findViewById(R.id.posts_recyclerview);
        backBtn =(ImageButton) findViewById(R.id.back_btn);
        TextView slugTitle = (TextView)findViewById(R.id.slug_title);
        homeBtn =(ImageButton)findViewById(R.id.home_title_post_btn);
        notExistPostcardView =(CardView)findViewById(R.id.not_exist_post_card);
        paginationView =(PaginationView) findViewById(R.id.pager_size_spinner);

        backBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.please_wait)
                .build();
        isPostOnly = getIntent().getExtras().getBoolean("post_only");
        if (isPostOnly){
            catId = getIntent().getExtras().getString("cat_id");
            slugTitle.setText(getIntent().getExtras().getString("cat_name"));
        }else {
            slugName = getIntent().getExtras().getString("slug");
            slugTitle.setText(slugName);// set name slug in toolbar
        }

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
                if (dialog.isShowing()){
                    return;
                }
                dialog.show();
                TiltlePostsModel.PostsModel postsModel = tiltlePostsModel.getPostsModels().get(position);
                Intent intent = new Intent(TitlePostsActivity.this,PostActivity.class);
                intent.putExtra("postId",postsModel.getId());
                intent.putExtra("source","net");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getDataFromServer(final int pageNumber , final int pageSize) {
        recyclerView.setVisibility(View.GONE);
        dialog.show();
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetPostsServer getPostsServer = retrofit.create(GetPostsServer.class);
        Call<TiltlePostsModel> tiltlePostsModelCall;
        if (isPostOnly){
            tiltlePostsModelCall = getPostsServer.getTitlePostByCatId(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),catId,String.valueOf(pageNumber),String.valueOf(pageSize));
        }else {
            tiltlePostsModelCall = getPostsServer.getTiltlePostsBySlug(SaveItem.getItem(this,SaveItem.USER_COOKIE,""),slugName,String.valueOf(pageNumber),String.valueOf(pageSize));
        }
        tiltlePostsModelCall.enqueue(new Callback<TiltlePostsModel>() {
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
                        recyclerView.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<TiltlePostsModel> call, Throwable t) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(TitlePostsActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                            .setTitle(getString(R.string.not_respone))
                            .setIcon(R.drawable.access_server)
                            .setTextGravity(Gravity.CENTER)
                            .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getDataFromServer(pageNumber+1,pageSize);
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                    dialog.dismiss();
                }
            });


    }

    private void fininshActivity() {
        MDToast.makeText(this,getString(R.string.no_exit_post),2500,MDToast.TYPE_INFO).show();
        dialog.dismiss();
        notExistPostcardView.setVisibility(View.VISIBLE);
//        finish();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void startHomePage(){
        finishAndRemoveTask();
        startActivity(new Intent(TitlePostsActivity.this,CategoryActivity.class));

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finishAndRemoveTask();
//                finishAffinity();
                break;
            case R.id.home_title_post_btn:
                startHomePage();
                break;
        }
    }


}
