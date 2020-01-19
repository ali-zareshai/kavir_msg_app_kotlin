package com.shafa.ali.kavir_msg.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;

public class SplashActivity extends AppCompatActivity {
    private SpinKitView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loader = findViewById(R.id.spin_kit);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,CategoryActivity.class));
                finish();
            }
        },3000);
    }
}
