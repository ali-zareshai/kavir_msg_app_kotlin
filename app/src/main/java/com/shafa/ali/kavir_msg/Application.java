package com.shafa.ali.kavir_msg;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("kavir.realm")
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
