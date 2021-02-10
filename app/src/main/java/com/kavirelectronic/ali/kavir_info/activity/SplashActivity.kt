package com.kavirelectronic.ali.kavir_info.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.kavirelectronic.ali.kavir_info.BuildConfig
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.CategoryActivity
import com.kavirelectronic.ali.kavir_info.db.models.Category
import com.kavirelectronic.ali.kavir_info.db.models.Post
import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.server.GetDataCategory
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Utility
import com.valdesekamdem.library.mdtoast.MDToast
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    private var versionNameTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Utility.getNewVersion(this)
        getCategoryDate()
        versionNameTv = findViewById<View>(R.id.version_name) as TextView
        versionNameTv!!.text = FormatHelper.toPersianNumber(BuildConfig.VERSION_NAME)
    }

    private fun getCategoryDate(){
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getDataService = retrofit!!.create(GetDataCategory::class.java)
        getDataService.getAllCategorys(SaveItem.getItem(this, SaveItem.USER_COOKIE, ""))?.enqueue(object : Callback<List<CategoryModel?>?> {
            override fun onResponse(call: Call<List<CategoryModel?>?>, response: Response<List<CategoryModel?>?>) {
                if (response.isSuccessful) {
                    saveCategoryInDataBase(response.body())
                }
            }

            override fun onFailure(call: Call<List<CategoryModel?>?>, t: Throwable) {
                Log.e("onFailure:", t.message)

            }
        })
    }

    private fun saveCategoryInDataBase(body: List<CategoryModel?>?) {
        if (body!=null && body.size>0){
           if (deleteAllCategoryTable()){
               val realm = Realm.getDefaultInstance()
               for (cat in body){
                   realm.beginTransaction()
                   val catDb = realm.createObject(Category::class.java)
                   catDb.id =cat?.id
                   catDb.description =cat?.description
                   catDb?.parent =cat?.parent
                   catDb?.post_count =cat?.post_count
                   catDb?.slug =cat?.slug
                   catDb?.subCount =cat?.subCount
                   catDb?.title =cat?.title
                   realm.commitTransaction()
               }
           }
            startNextActivity()
        }else{
            MDToast.makeText(this,getString(R.string.error_in_internet),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show()
        }

    }

    private fun startNextActivity(){
        startActivity(Intent(this@SplashActivity, CategoryActivity::class.java))
        finish()
    }

    private fun deleteAllCategoryTable():Boolean {
        var res =false
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val rows =realm.where(Category::class.java).findAll()
        if (rows.size==0){
            res =true
        }else{
            res =rows.deleteAllFromRealm()
        }
        realm.commitTransaction()
        return res
    }
}