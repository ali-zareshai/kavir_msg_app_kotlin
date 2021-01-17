package com.kavirelectronic.ali.kavir_info.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.*
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.github.ybq.android.spinkit.SpinKitView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.CategoryActivity
import com.kavirelectronic.ali.kavir_info.adapters.CommentAdapter
import com.kavirelectronic.ali.kavir_info.db.models.Post
import com.kavirelectronic.ali.kavir_info.models.PostModel
import com.kavirelectronic.ali.kavir_info.server.GetPostsServer
import com.kavirelectronic.ali.kavir_info.utility.CustomTypeFaceSpan
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.valdesekamdem.library.mdtoast.MDToast
import customview.CustomCommentModal
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostActivity : AppCompatActivity(), View.OnClickListener {
    private var titleTv: TextView? = null
    private var dateTv: TextView? = null
    private var autherTv: TextView? = null
    private var commentRv: RecyclerView? = null
    private var webView: WebView? = null
    private var postId: String? = null
    private var postModel: PostModel? = null
    private var commentAdapter: CommentAdapter? = null
    private var scrollviewPost: ScrollView? = null
    private var loading: SpinKitView? = null
    private var backBtn: ImageButton? = null
    private var saveBtn: ImageButton? = null
    private var deleteBtn: ImageButton? = null
    private var homeBtn: ImageButton? = null
    private var sendComment: Button? = null
    private var showComments: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        initViews()
        intents()
        backBtn!!.setOnClickListener(this)
        sendComment!!.setOnClickListener(this)
        showComments!!.setOnClickListener(this)
        saveBtn!!.setOnClickListener(this)
        deleteBtn!!.setOnClickListener(this)
        homeBtn!!.setOnClickListener(this)
        val mTopToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(mTopToolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        for (i in 0 until menu.size()) {
            val mi = menu.getItem(i)
            applyFontToMenuItem(mi)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.open_in_browser -> {
                openInWeb()
                true
            }
            R.id.share_link -> {
                shareLink()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareLink() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Uri.parse(postModel!!.url))
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)))
    }

    private fun openInWeb() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(postModel!!.url))
        startActivity(browserIntent)
    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "fonts/Vazir.ttf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(CustomTypeFaceSpan("", font, Color.BLACK), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mi.title = mNewTitle
    }

    private fun fetchDataDb() {
        val realm = Realm.getDefaultInstance()
        val post = realm.where(Post::class.java).equalTo("id", postId).findFirst()
        postModel = PostModel()
        postModel!!.id = post?.id
        postModel!!.title = post?.title
        postModel!!.author = post?.author
        postModel!!.content = post?.content
        postModel!!.date = post?.date
        postModel!!.url = post?.url
        postModel!!.commentModelList = ArrayList()
        setViews()
    }

    private fun fetchDataNet() {
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getPostsServer = retrofit!!.create(GetPostsServer::class.java)
        getPostsServer.getPost(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""), postId)!!.enqueue(object : Callback<PostModel?> {
            override fun onResponse(call: Call<PostModel?>, response: Response<PostModel?>) {
                if (response.isSuccessful) {
                    postModel = response.body()
                    setViews()
                }
            }

            override fun onFailure(call: Call<PostModel?>, t: Throwable) {
                val builder = CFAlertDialog.Builder(this@PostActivity)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER) { dialogInterface, i ->
                            fetchDataNet()
                            dialogInterface.dismiss()
                        }
                builder.show()
            }
        })
    }

    private fun setViews() {
        dateTv!!.text = FormatHelper.toPersianNumber(postModel!!.date)
        autherTv!!.text = postModel!!.author
        showComments!!.text = getString(R.string.comments) + "(" + FormatHelper.toPersianNumber(postModel!!.commentModelList!!.size.toString()) + ")"
        webView!!.settings.javaScriptEnabled = true
        webView!!.loadData("<html dir=\"rtl\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"/>" + postModel!!.content + "</html>", "text/html", "UTF-8")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleTv!!.text = Html.fromHtml(postModel!!.title, Html.FROM_HTML_MODE_COMPACT)
            //            contentTv.setText(Html.fromHtml(postModel.getContent(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            Log.e("titleTv:", postModel!!.title)
            titleTv!!.text = Html.fromHtml(postModel!!.title)
            //            contentTv.setText(Html.fromHtml(postModel.getContent()));
        }
        scrollviewPost!!.visibility = View.VISIBLE
        loading!!.visibility = View.GONE
        if (postModel!!.commentStatus.equals("close", ignoreCase = true)) {
            (findViewById<View>(R.id.rel_comments) as RelativeLayout).visibility = View.GONE
        } else {
            (findViewById<View>(R.id.rel_comments) as RelativeLayout).visibility = View.VISIBLE
        }
    }

    private fun setCommentRecycleView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        commentRv!!.layoutManager = layoutManager
        commentAdapter = CommentAdapter(postModel!!.commentModelList, this)
        commentRv!!.adapter = commentAdapter
        commentRv!!.visibility = View.VISIBLE
    }

    private fun intents(){
        postId = intent.extras?.getString("postId")
        if (intent.extras?.getString("source").equals("net", ignoreCase = true)) {
            fetchDataNet()
            deleteBtn!!.isEnabled = false
            deleteBtn!!.visibility = View.GONE
            checkSaved()
        } else {
            fetchDataDb()
            (findViewById<View>(R.id.rel_comments) as RelativeLayout).visibility = View.GONE
            saveBtn!!.isEnabled = false
            saveBtn!!.visibility = View.GONE
        }
    }


    private fun checkSaved(): Boolean {
        val realm = Realm.getDefaultInstance()
        if (realm.where(Post::class.java).equalTo("id", postId).count() != 0L) {
            saveBtn!!.setImageResource(R.drawable.ic_schedule_black)
            return true
        }
        saveBtn!!.setImageResource(R.drawable.ic_schedule_white)
        return false
    }

    private fun startHomePage() {
        startActivity(Intent(this@PostActivity, CategoryActivity::class.java))
        finish()
    }

    private fun initViews() {
        titleTv = findViewById<View>(R.id.title_post_card) as TextView
        //        contentTv=(TextView)findViewById(R.id.content_post_card);
        dateTv = findViewById<View>(R.id.date_post_card) as TextView
        autherTv = findViewById<View>(R.id.auther_post_card) as TextView
        commentRv = findViewById<View>(R.id.comment_recyclerview_posts) as RecyclerView
        webView = findViewById<View>(R.id.webview_post_card) as WebView
        scrollviewPost = findViewById<View>(R.id.scrollview_post) as ScrollView
        loading = findViewById<View>(R.id.spin_post) as SpinKitView
        backBtn = findViewById<View>(R.id.back_post_btn) as ImageButton
        sendComment = findViewById<View>(R.id.add_new_comment) as Button
        showComments = findViewById<View>(R.id.show_comment) as Button
        saveBtn = findViewById<View>(R.id.save_post_btn) as ImageButton
        deleteBtn = findViewById<View>(R.id.delete_post_btn) as ImageButton
        homeBtn = findViewById<View>(R.id.home_post_btn) as ImageButton
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_post_btn -> finish()
            R.id.show_comment -> if (postModel!!.commentModelList!!.size > 0) {
                setCommentRecycleView()
            } else {
                MDToast.makeText(this, getString(R.string.no_post), 2500, MDToast.TYPE_INFO).show()
            }
            R.id.add_new_comment -> CustomCommentModal().showNewComment(this, postId)
            R.id.save_post_btn -> if (checkSaved()) {
                deleteCurrentPost()
            } else {
                saveCurrentPost()
            }
            R.id.delete_post_btn -> deleteCurrentPost()
            R.id.home_post_btn -> startHomePage()
        }
    }

    private fun deleteCurrentPost() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val res = realm.where(Post::class.java).equalTo("id", postId).findAll().deleteAllFromRealm()
        realm.commitTransaction()
        if (res) {
            MDToast.makeText(this, getString(R.string.delete_success), 2500, MDToast.TYPE_SUCCESS).show()
            //            ((CategoryActivity)CategoryActivity.context).finish();
//            startActivity(new Intent(PostActivity.this,CategoryActivity.class));
            checkSaved()
            //            onBackPressed();
        }
    }

    private fun saveCurrentPost() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val post = realm.createObject(Post::class.java)
        post.id = postModel!!.id
        post.title = postModel!!.title
        post.content = postModel!!.content
        post.date = postModel!!.date
        post.author = postModel!!.author
        post.url = postModel!!.url
        realm.commitTransaction()
        MDToast.makeText(this, getString(R.string.add_to_list), 3000, MDToast.TYPE_SUCCESS).show()
        checkSaved()
    }
}