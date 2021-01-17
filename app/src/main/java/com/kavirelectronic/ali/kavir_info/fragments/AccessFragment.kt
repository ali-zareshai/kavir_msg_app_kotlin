package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.adapters.AccessAdapter
import com.kavirelectronic.ali.kavir_info.models.AccessModel
import com.kavirelectronic.ali.kavir_info.server.LoginServer
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.valdesekamdem.library.mdtoast.MDToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccessFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var accessAdapter: AccessAdapter? = null
    private var notAccessActiveTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_access, container, false)
        recyclerView = view.findViewById<View>(R.id.access_recyclerview) as RecyclerView
        swipeRefreshLayout = view.findViewById<View>(R.id.swip_refresh_access) as SwipeRefreshLayout
        notAccessActiveTv = view.findViewById<View>(R.id.no_access_active_tv) as TextView
        layoutManager = LinearLayoutManager(activity.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        dataFromServer
        swipeRefreshLayout!!.setOnRefreshListener { dataFromServer }
        return view
    }

    private val dataFromServer: Unit
        private get() {
            swipeRefreshLayout!!.isRefreshing = true
            val retrofit = RetrofitClientInstance.retrofitInstance
            val loginServer = retrofit!!.create(LoginServer::class.java)
            loginServer.getAccessList(SaveItem.getItem(activity, SaveItem.APK_ID, ""), SaveItem.getItem(activity, SaveItem.S_CODE, ""))?.enqueue(object : Callback<List<AccessModel?>?> {
                        override fun onResponse(call: Call<List<AccessModel?>?>, response: Response<List<AccessModel?>?>) {
                            if (response.isSuccessful) {
                                try {
                                    if (response.body() == null || response.body()!!.size == 0) {
                                        MDToast.makeText(activity, activity.getString(R.string.not_access_active), 3000, MDToast.TYPE_ERROR).show()
                                        recyclerView!!.visibility = View.GONE
                                        notAccessActiveTv!!.visibility = View.VISIBLE
                                    } else {
                                        recyclerView!!.visibility = View.VISIBLE
                                        notAccessActiveTv!!.visibility = View.GONE
                                        accessAdapter = AccessAdapter(activity, response.body())
                                        recyclerView!!.adapter = accessAdapter
                                    }
                                } catch (e: Exception) {
                                    Log.e("onResponse access:", e.message)
                                }
                            }
                            if (swipeRefreshLayout!!.isRefreshing) {
                                swipeRefreshLayout!!.isRefreshing = false
                            }
                        }

                        override fun onFailure(call: Call<List<AccessModel?>?>, t: Throwable) {
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
                        }
                    })
        }

    companion object {
        fun newInstance(): AccessFragment {
            return AccessFragment()
        }
    }
}