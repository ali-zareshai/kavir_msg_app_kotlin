package com.kavirelectronic.ali.kavir_info

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("kavirDb.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}