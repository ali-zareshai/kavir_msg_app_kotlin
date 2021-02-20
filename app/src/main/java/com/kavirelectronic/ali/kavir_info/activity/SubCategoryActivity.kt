package com.kavirelectronic.ali.kavir_info.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.adapters.SubCategoryAdapter
import com.kavirelectronic.ali.kavir_info.db.models.Category
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.models.SubCategoryModel
import com.kavirelectronic.ali.kavir_info.server.GetDataCategory
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import com.kavirelectronic.ali.kavir_info.utility.RepositoryService
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import dmax.dialog.SpotsDialog
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SubCategoryActivity : Activity(), View.OnClickListener {
    private var subCategoryRecycler: RecyclerView? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mTopToolbar: Toolbar? = null
    private var bundle: Bundle? = null
    private var parentId: String? = null
    private var backBtn: ImageButton? = null
    private var homeBtn: ImageButton? = null
    private var subCategoryModels: MutableList<SubCategoryModel?> =ArrayList()
    private var currentSlug: String? = null
    private var parentName: String? = null
    private var currentPageSize = 0
    private var notExist: LinearLayout? = null
    private var dialog: AlertDialog? = null
    private var isSending = false
    private var lastList: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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

                try {
                    currentSlug = subCategoryModels.get(position)?.slug
                    currentPageSize = subCategoryModels.get(position)?.post_count?:0
                    getSubCategoryFromServer(subCategoryModels.get(position)?.id.toString())
                    setCountPostCount(subCategoryModels.get(position)?.id.toString(), subCategoryModels.get(position)?.post_count?:0)
                } catch (e: Exception) {
                    Log.e("Exception92:", e.message)
                }
                dialog!!.dismiss()
            }

            override fun onLongClick(view: View?, position: Int) {
                Log.e("long click:",position.toString())
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

        val rep = RepositoryService(this)
        rep.getCategory(parentIdf!!,object : RepositoryService.CategoryCallback{
            override fun getCategoryList(categoryList: List<CategoryModel?>?) {
                isSending = false
                if (categoryList?.get(0)?.id == 0) {
                    startTitlePostActivity(currentSlug, currentPageSize)
                } else {
                    lastList = parentIdf
                    if (categoryList?.size!! >0){
                        subCategoryModels =ArrayList()
                        for (row in categoryList){
                            val cat = SubCategoryModel(
                                    id = row?.id!!,
                                    slug = row.slug,
                                    title = row.title,
                                    description = row.description,
                                    post_count = row.post_count,
                                    parent = row.parent,
                                    sub = row.subCount
                            )
                            subCategoryModels.add(cat)
                        }
                    }
                    generateDataList(categoryList)
                }
                dialog!!.dismiss()
            }

        })

    }

    private fun startTitlePostActivity(slug: String?, postSize: Int) {
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

    private fun generateDataList(subCategoryModelList: List<CategoryModel?>?) {
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