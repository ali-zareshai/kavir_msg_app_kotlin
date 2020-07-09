package com.kavirelectronic.ali.kavir_info.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.CategoryActivity
import com.kavirelectronic.ali.kavir_info.adapters.TitleAdapter
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel
import com.kavirelectronic.ali.kavir_info.server.GetPostsServer
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.valdesekamdem.library.mdtoast.MDToast
import customview.PaginationView
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class TitlePostsActivity : AppCompatActivity(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    private var backBtn: ImageButton? = null
    private var homeBtn: ImageButton? = null
    private var slugName: String? = null
    private var catId: String? = null
    private var tiltlePostsModel: TiltlePostsModel? = null
    private var notExistPostcardView: CardView? = null
    private var paginationView: PaginationView? = null
    private var dialog: AlertDialog? = null
    private var isPostOnly = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_posts)
        recyclerView = findViewById<View>(R.id.posts_recyclerview) as RecyclerView
        backBtn = findViewById<View>(R.id.back_btn) as ImageButton
        val slugTitle = findViewById<View>(R.id.slug_title) as TextView
        homeBtn = findViewById<View>(R.id.home_title_post_btn) as ImageButton
        notExistPostcardView = findViewById<View>(R.id.not_exist_post_card) as CardView
        paginationView = findViewById<View>(R.id.pager_size_spinner) as PaginationView
        backBtn!!.setOnClickListener(this)
        homeBtn!!.setOnClickListener(this)
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.please_wait)
                .build()
        isPostOnly = intent.extras.getBoolean("post_only")
        if (isPostOnly) {
            catId = intent.extras.getString("cat_id")
            slugTitle.text = intent.extras.getString("cat_name")
        } else {
            slugName = intent.extras.getString("slug")
            slugTitle.text = slugName // set name slug in toolbar
        }
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        getDataFromServer(1, 10)
        paginationView!!.setPager(intent.extras.getInt("post_size"))
        paginationView!!.setOnPagerUpdate { pageNumber, pageSize -> getDataFromServer(pageNumber + 1, pageSize) }
        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                if (dialog!!.isShowing()) {
                    return
                }
                dialog!!.show()
                val postsModel = tiltlePostsModel!!.postsModels[position]
                val intent = Intent(this@TitlePostsActivity, PostActivity::class.java)
                intent.putExtra("postId", postsModel.id)
                intent.putExtra("source", "net")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                dialog!!.dismiss()
            }

            override fun onLongClick(view: View, position: Int) {}
        }))
    }

    private fun getDataFromServer(pageNumber: Int, pageSize: Int) {
        recyclerView!!.visibility = View.GONE
        dialog!!.show()
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getPostsServer = retrofit.create(GetPostsServer::class.java)
        val tiltlePostsModelCall: Call<TiltlePostsModel>
        tiltlePostsModelCall = if (isPostOnly) {
            getPostsServer.getTitlePostByCatId(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""), catId, pageNumber.toString(), pageSize.toString())
        } else {
            getPostsServer.getTiltlePostsBySlug(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""), slugName, pageNumber.toString(), pageSize.toString())
        }
        tiltlePostsModelCall.enqueue(object : Callback<TiltlePostsModel?> {
            override fun onResponse(call: Call<TiltlePostsModel?>, response: Response<TiltlePostsModel?>) {
                if (response.isSuccessful) {
                    tiltlePostsModel = response.body()
                    if (tiltlePostsModel!!.postsModels == null || tiltlePostsModel!!.postsModels.size == 0) {
                        fininshActivity()
                        return
                    }
                    notExistPostcardView!!.visibility = View.GONE
                    val titleAdapter = TitleAdapter(this@TitlePostsActivity, tiltlePostsModel!!.postsModels)
                    recyclerView!!.adapter = titleAdapter
                    recyclerView!!.visibility = View.VISIBLE
                    dialog!!.dismiss()
                }
            }

            override fun onFailure(call: Call<TiltlePostsModel?>, t: Throwable) {
                val builder = CFAlertDialog.Builder(this@TitlePostsActivity)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER) { dialogInterface, i ->
                            getDataFromServer(pageNumber + 1, pageSize)
                            dialogInterface.dismiss()
                        }
                builder.show()
                dialog!!.dismiss()
            }
        })
    }

    private fun fininshActivity() {
        MDToast.makeText(this, getString(R.string.no_exit_post), 2500, MDToast.TYPE_INFO).show()
        dialog!!.dismiss()
        notExistPostcardView!!.visibility = View.VISIBLE
        //        finish();
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun startHomePage() {
        finishAndRemoveTask()
        startActivity(Intent(this@TitlePostsActivity, CategoryActivity::class.java))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_btn -> finishAndRemoveTask()
            R.id.home_title_post_btn -> startHomePage()
        }
    }
}