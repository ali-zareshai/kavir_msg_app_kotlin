package com.kavirelectronic.ali.kavir_info.fragments

import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.github.ybq.android.spinkit.SpinKitView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.SubCategoryActivity
import com.kavirelectronic.ali.kavir_info.activity.TitlePostsActivity
import com.kavirelectronic.ali.kavir_info.adapters.CategoryAdapter
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.server.GetDataCategory
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Setting
import com.valdesekamdem.library.mdtoast.MDToast
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment() {
    private var categoryRecycler: RecyclerView? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var loading: SpinKitView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var categoryModelList: List<CategoryModel?>? = null
    private var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        Setting.isVistied = false
        categoryRecycler = view.findViewById<View>(R.id.category_recyclerview) as RecyclerView
        loading = view.findViewById<View>(R.id.spin_cat) as SpinKitView
        swipeRefreshLayout = view.findViewById<View>(R.id.swip_refresh_category) as SwipeRefreshLayout
        dialog = SpotsDialog.Builder()
                .setContext(activity)
                .setMessage(R.string.please_wait)
                .build()
        layoutManager = LinearLayoutManager(activity.applicationContext)
        categoryRecycler!!.layoutManager = layoutManager
        dataFromServer
        swipeRefreshLayout!!.setOnRefreshListener { dataFromServer }
        return view
    }

    private fun hideSwipRefresh() {
        if (swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    //                Log.e("msg",response.body().toString());
    val dataFromServer: Unit
        get() {
            dialog!!.show()
            val retrofit = RetrofitClientInstance.retrofitInstance
            val getDataService = retrofit!!.create(GetDataCategory::class.java)
            getDataService.getAllCategorys(SaveItem.getItem(activity.applicationContext, SaveItem.USER_COOKIE, ""))?.enqueue(object : Callback<List<CategoryModel?>?> {
                override fun onResponse(call: Call<List<CategoryModel?>?>, response: Response<List<CategoryModel?>?>) {
//                Log.e("msg",response.body().toString());
                    if (response.isSuccessful) {
                        categoryModelList = response.body()
                        generateDataList(categoryModelList as List<CategoryModel>)
                        loading!!.visibility = View.GONE
                        hideSwipRefresh()
                    }
                    dialog!!.dismiss()
                }

                override fun onFailure(call: Call<List<CategoryModel?>?>, t: Throwable) {
                    Log.e("onFailure:", t.message)
                    val builder = CFAlertDialog.Builder(activity)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                            .setTitle(getString(R.string.not_respone))
                            .setIcon(R.drawable.access_server)
                            .setTextGravity(Gravity.CENTER)
                            .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER) { dialogInterface, i ->
                                dataFromServer
                                dialogInterface.dismiss()
                            }
                    builder.show()
                    dialog!!.dismiss()
                }
            })
            categoryRecycler!!.addOnItemTouchListener(RecyclerTouchListener(activity.applicationContext,
                    categoryRecycler!!, object : ClickListener {
                override fun onClick(view: View?, position: Int) {
                    dialog!!.show()
                    val model = categoryModelList!![position]
                    setCountPostCount(model?.id.toString() + "", categoryModelList!![position]!!.post_count)
                    if (model!!.subCount > 0) {
                        val intent = Intent(activity, SubCategoryActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("parentId", model?.id.toString() + "")
                        bundle.putString("parentName", model?.title)
                        intent.putExtras(bundle)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    } else if (model?.subCount == 0 && model?.post_count > 0) {
                        val intent = Intent(activity, TitlePostsActivity::class.java)
                        intent.putExtra("post_only", true)
                        intent.putExtra("cat_id", model?.id.toString() + "")
                        intent.putExtra("cat_name", model?.title)
                        intent.putExtra("post_size", model?.post_count)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    } else {
                        MDToast.makeText(activity, activity.getString(R.string.empty_cat), 2500, MDToast.TYPE_WARNING).show()
                    }
                    dialog!!.dismiss()
                }

                override fun onLongClick(view: View?, position: Int) {}
            }))
        }

    private fun generateDataList(categoryModelList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(activity, categoryModelList)
        categoryRecycler!!.adapter = categoryAdapter
    }

    private fun setCountPostCount(postId: String, newCountPost: Int) {
        SaveItem.setItem(activity.applicationContext, "post:$postId", newCountPost.toString())
    }

    companion object {
        private var fragment: Fragment? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = CategoryFragment()
            }
            return fragment
        }
    }
}