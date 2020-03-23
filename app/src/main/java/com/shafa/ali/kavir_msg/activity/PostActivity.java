package com.shafa.ali.kavir_msg.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CommentAdapter;
import com.shafa.ali.kavir_msg.db.models.Post;
import com.shafa.ali.kavir_msg.fragments.ReadyReadFragment;
import com.shafa.ali.kavir_msg.models.CommentModel;
import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.CustomTypeFaceSpan;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

import customview.CustomCommentModal;
import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTv,dateTv,autherTv,commentTv;
    private RecyclerView commentRv;
    private WebView webView;
    private String postId;
    private PostModel postModel;
    private CommentAdapter commentAdapter;
    private ScrollView scrollviewPost;
    private SpinKitView loading;
    private ImageButton backBtn,saveBtn,deleteBtn,homeBtn;
    private LinearLayout sendComment,showComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initViews();
        getIntents();
        backBtn.setOnClickListener(this);
        sendComment.setOnClickListener(this);
        showComments.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            applyFontToMenuItem(mi);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                openInWeb();
                return true;
            case R.id.share_link:
                shareLink();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareLink() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  Uri.parse(postModel.getUrl()));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }

    private void openInWeb() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(postModel.getUrl()));
        startActivity(browserIntent);
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Vazir.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void fetchDataDb(){
        Realm realm = Realm.getDefaultInstance();
        Post post = realm.where(Post.class).equalTo("id",postId).findFirst();
        postModel = new PostModel();
        postModel.setId(post.getId());
        postModel.setTitle(post.getTitle());
        postModel.setAuthor(post.getAuthor());
        postModel.setContent(post.getContent());
        postModel.setDate(post.getDate());
        postModel.setUrl(post.getUrl());
        postModel.setCommentModelList(new ArrayList<CommentModel>());
        setViews();
    }



    private void fetchDataNet() {
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
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(PostActivity.this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchDataNet();
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            }
        });
    }

    private void setViews() {
        dateTv.setText(FormatHelper.toPersianNumber(postModel.getDate()));
        autherTv.setText(postModel.getAuthor());
        commentTv.setText(getString(R.string.comments)+"("+ FormatHelper.toPersianNumber(String.valueOf(postModel.getCommentModelList().size()))+")");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData("<html dir=\"rtl\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"/>"+postModel.getContent()+"</html>","text/html","UTF-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleTv.setText(Html.fromHtml(postModel.getTitle(),Html.FROM_HTML_MODE_COMPACT));
//            contentTv.setText(Html.fromHtml(postModel.getContent(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            Log.e("titleTv:",postModel.getTitle());
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
        if (getIntent().getExtras().getString("source").equalsIgnoreCase("net")){
            fetchDataNet();
            deleteBtn.setEnabled(false);
            deleteBtn.setVisibility(View.GONE);
            checkSaved();
        }else {
            fetchDataDb();
            ((RelativeLayout)findViewById(R.id.rel_comments)).setVisibility(View.GONE);
            saveBtn.setEnabled(false);
            saveBtn.setVisibility(View.GONE);
        }
    }

    private boolean checkSaved() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Post.class).equalTo("id",postId).count()!=0){
            saveBtn.setImageResource(R.drawable.ic_schedule_black);
            return true;
        }
        saveBtn.setImageResource(R.drawable.ic_schedule_white);
        return false;
    }

    private void startHomePage(){
        startActivity(new Intent(PostActivity.this,CategoryActivity.class));
        finish();
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
        sendComment =(LinearLayout) findViewById(R.id.add_new_comment);
        showComments=(LinearLayout) findViewById(R.id.show_comment);
        commentTv = (TextView) findViewById(R.id.count_comment);
        saveBtn =(ImageButton)findViewById(R.id.save_post_btn);
        deleteBtn=(ImageButton)findViewById(R.id.delete_post_btn);
        homeBtn =(ImageButton)findViewById(R.id.home_post_btn);

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
                if (postModel.getCommentModelList().size()>0){
                    setCommentRecycleView();
                }else{
                    MDToast.makeText(this,getString(R.string.no_post),2500,MDToast.TYPE_INFO).show();
                }

                break;
            case R.id.add_new_comment:
                new CustomCommentModal().showNewComment(this,postId);
                break;
            case R.id.save_post_btn:
                if (checkSaved()){
                    deleteCurrentPost();
                }else {
                    saveCurrentPost();
                }
                break;
            case R.id.delete_post_btn:
                deleteCurrentPost();
                break;
            case R.id.home_post_btn:
                startHomePage();
                break;

        }
    }

    private void deleteCurrentPost() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Boolean res =realm.where(Post.class).equalTo("id",postId).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        if (res){
            MDToast.makeText(this,getString(R.string.delete_success),2500,MDToast.TYPE_SUCCESS).show();
//            ((CategoryActivity)CategoryActivity.context).finish();
//            startActivity(new Intent(PostActivity.this,CategoryActivity.class));
            checkSaved();
//            onBackPressed();
        }
    }

    private void saveCurrentPost() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Post post = realm.createObject(Post.class);
        post.setId(postModel.getId());
        post.setTitle(postModel.getTitle());
        post.setContent(postModel.getContent());
        post.setDate(postModel.getDate());
        post.setAuthor(postModel.getAuthor());
        post.setUrl(postModel.getUrl());
        realm.commitTransaction();
        MDToast.makeText(this,getString(R.string.add_to_list),3000,MDToast.TYPE_SUCCESS).show();
        checkSaved();

    }
}
