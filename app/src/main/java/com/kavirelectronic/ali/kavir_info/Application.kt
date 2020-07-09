package com.kavirelectronic.ali.kavir_info

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("kavirDb.realm")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(config)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}