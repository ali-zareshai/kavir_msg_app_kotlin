package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ybq.android.spinkit.SpinKitView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.PostActivity
import com.kavirelectronic.ali.kavir_info.adapters.TitleAdapter
import com.kavirelectronic.ali.kavir_info.db.models.Post
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel.PostsModel
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class ReadyReadFragment : Fragment() {
    private var categoryRecycler: RecyclerView? = null
    private var titleAdapter: TitleAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var loading: SpinKitView? = null
    private var modelListDb: List<Post>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle): View {
        val view = inflater.inflate(R.layout.fragment_ready_read, container, false)
        categoryRecycler = view.findViewById<View>(R.id.ready_read_recyclerview) as RecyclerView
        loading = view.findViewById<View>(R.id.spin_cat) as SpinKitView
        swipeRefreshLayout = view.findViewById<View>(R.id.swip_refresh_ready_red) as SwipeRefreshLayout
        layoutManager = LinearLayoutManager(activity.applicationContext)
        categoryRecycler!!.layoutManager = layoutManager
        dataFromDb
        initRecycle()
        swipeRefreshLayout!!.setOnRefreshListener { dataFromDb }
        return view
    }

    private fun hideSwipRefresh() {
        if (swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    private fun initRecycle() {
        categoryRecycler!!.addOnItemTouchListener(RecyclerTouchListener(activity.applicationContext, categoryRecycler!!, object : ClickListener {
            override fun onClick(view: View?, position: Int) {
                val intent = Intent(activity.applicationContext, PostActivity::class.java)
                intent.putExtra("postId", modelListDb!![position].id)
                intent.putExtra("source", "db")
                startActivity(intent)
            }

            override fun onLongClick(view: View?, position: Int) {}
        }))
    }

    val dataFromDb: Unit
        get() {
            val realm = Realm.getDefaultInstance()
            modelListDb = realm.where(Post::class.java).findAll()
            val postsModels: MutableList<PostsModel> = ArrayList()
            for (post in modelListDb as RealmResults<Post?>) {
                val postModel = PostsModel()
                postModel.title = post!!.title
                postModel.author = post!!.author
                postModel.content = post!!.content
                postModel.date = post!!.date
                postModel.id = post!!.id
                postModel.commentCount = "0"
                postsModels.add(postModel)
            }
            titleAdapter = TitleAdapter(activity.applicationContext, postsModels)
            categoryRecycler!!.adapter = titleAdapter
            loading!!.visibility = View.GONE
            hideSwipRefresh()
        }

    companion object {
        private var fragment: Fragment? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = ReadyReadFragment()
            }
            return fragment
        }
    }
}