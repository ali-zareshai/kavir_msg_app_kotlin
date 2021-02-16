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
import com.kavirelectronic.ali.kavir_info.utility.*
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
        versionNameTv = findViewById<View>(R.id.version_name) as TextView
        versionNameTv!!.text = FormatHelper.toPersianNumber(BuildConfig.VERSION_NAME)

        val rep = RepositoryService(this)
        rep.clearDatabase()
        rep.getCategory("0",object:RepositoryService.CategoryCallback{
            override fun getCategoryList(categoryList: List<CategoryModel?>?) {
                if (categoryList!=null && categoryList.size>0){
                    startNextActivity()
                }
            }
        })

    }





    private fun startNextActivity(){
        startActivity(Intent(this@SplashActivity, CategoryActivity::class.java))
        finish()
    }


}