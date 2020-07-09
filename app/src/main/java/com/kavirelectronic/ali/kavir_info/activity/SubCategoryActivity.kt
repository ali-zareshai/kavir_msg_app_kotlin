package com.kavirelectronic.ali.kavir_info.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.adapters.SubCategoryAdapter
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.SubCategoryModel
import com.kavirelectronic.ali.kavir_info.server.GetDataCategory
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class SubCategoryActivity : Activity(), View.OnClickListener {
    private var subCategoryRecycler: RecyclerView? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mTopToolbar: Toolbar? = null
    private var bundle: Bundle? = null
    private var parentId: String? = null
    private var backBtn: ImageButton? = null
    private var homeBtn: ImageButton? = null
    private var subCategoryModels: List<SubCategoryModel?>? = null
    private var currentSlug: String? = null
    private var parentName: String? = null
    private var currentPageSize = 0
    private var notExist: LinearLayout? = null
    private var dialog: AlertDialog? = null
    private var isSending = false
    private var lastList: String? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        subCategoryRecycler = findViewById<View>(R.id.sub_category_recyclerview) as RecyclerView
        backBtn = findViewById<View>(R.id.back_btn) as ImageButton
        homeBtn = findViewById<View>(R.id.home_sub_btn) as ImageButton
        notExist = findViewById<View>(R.id.not_exsit) as LinearLayout
        homeBtn!!.setOnClickListener(this)
        backBtn!!.setOnClickListener(this)
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.please_wait)
                .build()
        /////////
        bundle = intent.extras
        if (bundle != null) {
            parentId = bundle!!.getString("parentId")
            parentName = bundle!!.getString("parentName")
            (findViewById<View>(R.id.parent_cat_tv) as TextView).text = parentName
        }
        mTopToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        //        setSupportActionBar(mTopToolbar);
        ///
        layoutManager = LinearLayoutManager(this)
        subCategoryRecycler!!.layoutManager = layoutManager
        getSubCategoryFromServer(parentId)
        subCategoryRecycler!!.addOnItemTouchListener(RecyclerTouchListener(this,
                subCategoryRecycler!!, object : ClickListener {
            override fun onClick(view: View?, position: Int) {
                dialog!!.show()
                //Values are passing to activity & to fragment as well
                try {
                    currentSlug = subCategoryModels!![position]!!.slug
                    currentPageSize = subCategoryModels!![position]!!.post_count
                    getSubCategoryFromServer(subCategoryModels!![position]!!.id.toString())
                    setCountPostCount(subCategoryModels!![position]!!.id.toString(), subCategoryModels!![position]!!.post_count)
                } catch (e: Exception) {
                    Log.e("Exception:", e.message)
                }
                dialog!!.dismiss()
            }

            override fun onLongClick(view: View?, position: Int) {
                TODO("Not yet implemented")
            }
        }))
    }

    override fun onResume() {
        if (lastList != null) {
            Log.e("slug:", lastList)
            getSubCategoryFromServer(lastList)
        }
        super.onResume()
    }

    private fun getSubCategoryFromServer(parentIdf: String?) {
        if (isSending) {
            return
        }
        isSending = true
        dialog!!.show()
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getDataService = retrofit!!.create(GetDataCategory::class.java)
        getDataService.getAllSubCategorys(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""), parentIdf)!!.enqueue(object : Callback<List<SubCategoryModel?>?> {

            override fun onResponse(call: Call<List<SubCategoryModel?>?>, response: Response<List<SubCategoryModel?>?>) {
                if (response.isSuccessful) {
                    subCategoryModels = ArrayList()
                    subCategoryModels = response.body()
                    isSending = false
                    if (subCategoryModels!![0]!!.id == 0) {
                        startTitlePostActivity(currentSlug, currentPageSize)
                    } else {
                        lastList = parentIdf
                        generateDataList(subCategoryModels)
                    }
                    dialog!!.dismiss()
                }
            }

            override fun onFailure(call: Call<List<SubCategoryModel?>?>, t: Throwable) {
                val builder = CFAlertDialog.Builder(this@SubCategoryActivity)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER) { dialogInterface, i ->
                            getSubCategoryFromServer(parentId)
                            dialogInterface.dismiss()
                        }
                builder.show()
                dialog!!.dismiss()
                isSending = false
            }
        })
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun startTitlePostActivity(slug: String?, postSize: Int) {
        Log.e("startTitlePostActivity:", "l188")
        if (slug != null) {
            val intent = Intent(this@SubCategoryActivity, TitlePostsActivity::class.java)
            intent.putExtra("post_only", false)
            intent.putExtra("slug", slug)
            intent.putExtra("post_size", postSize)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else {
            notExist!!.visibility = View.VISIBLE
            //            hasPost(parentName,postSize);
        }
    }

    private fun generateDataList(subCategoryModelList: List<SubCategoryModel?>?) {
        subCategoryAdapter = null
        subCategoryAdapter = SubCategoryAdapter(applicationContext, subCategoryModelList)
        subCategoryRecycler!!.adapter = subCategoryAdapter
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_btn, R.id.home_sub_btn -> finishAndRemoveTask()
        }
    }

    private fun setCountPostCount(postId: String, newCountPost: Int) {
        SaveItem.setItem(applicationContext, "post:$postId", newCountPost.toString())
    }
}