package com.kavirelectronic.ali.kavir_info.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.BuildConfig
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.CategoryActivity
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.Utility

class SplashActivity : AppCompatActivity() {
    private var versionNameTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Utility.getNewVersion(this)
        versionNameTv = findViewById<View>(R.id.version_name) as TextView
        versionNameTv!!.text = FormatHelper.toPersianNumber(BuildConfig.VERSION_NAME)
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, CategoryActivity::class.java))
            finish()
        }, 3000)
    }
}