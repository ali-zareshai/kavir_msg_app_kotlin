package com.kavirelectronic.ali.kavir_info.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.adapters.TitleAdapter
import com.kavirelectronic.ali.kavir_info.interfaces.ClickListener
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel.PostsModel
import com.kavirelectronic.ali.kavir_info.server.GetPostsServer
import com.kavirelectronic.ali.kavir_info.utility.RecyclerTouchListener
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null
    private var backBtn: ImageButton? = null
    private var postsModelList: List<PostsModel?>? = null
    private var searchFram: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclerView = findViewById<View>(R.id.search_result_rc) as RecyclerView
        searchView = findViewById<View>(R.id.search_dt) as SearchView
        backBtn = findViewById<View>(R.id.back_search_btn) as ImageButton
        searchFram = findViewById<View>(R.id.frame_et) as FrameLayout
        backBtn!!.setOnClickListener { onBackPressed() }
        searchView!!.setOnSearchClickListener { searchFram!!.setBackgroundColor(Color.WHITE) }
        searchView!!.setOnCloseListener {
            searchFram!!.setBackgroundColor(Color.parseColor("#d32f2f"))
            false
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                getsearchResult(s)
                return false
            }
        })
        val searchEditText = searchView!!.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        //        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.textSize = 24f
        //        searchEditText.setActivated(true);
        val type2 = Typeface.createFromAsset(assets, "fonts/sans.ttf")
        searchEditText.setTypeface(type2, Typeface.NORMAL)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView!!, object : ClickListener {
            override fun onClick(view: View?, position: Int) {
                val postsModel = postsModelList!![position]
                val intent = Intent(this@SearchActivity, PostActivity::class.java)
                intent.putExtra("postId", postsModel!!.id)
                intent.putExtra("source", "net")
                startActivity(intent)
            }

            override fun onLongClick(view: View?, position: Int) {
                TODO("Not yet implemented")
            }
        }))
    }

    private fun getsearchResult(query: String) {
        recyclerView!!.visibility = View.GONE
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getPostsServer = retrofit!!.create(GetPostsServer::class.java)
        getPostsServer.getSearch(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""), query)!!.enqueue(object : Callback<List<PostsModel?>?> {
            override fun onFailure(call: Call<List<PostsModel?>?>, t: Throwable) {
                Log.e("fail search actiity,",t.message)
            }

            override fun onResponse(call: Call<List<PostsModel?>?>, response: Response<List<PostsModel?>?>) {
                if (response.isSuccessful) {
                    val titleAdapter = TitleAdapter(this@SearchActivity, response.body())
                    postsModelList = response.body()
                    recyclerView!!.adapter = titleAdapter
                    recyclerView!!.visibility = View.VISIBLE
                }
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}