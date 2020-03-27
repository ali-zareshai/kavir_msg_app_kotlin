package com.kavir.ali.kavir_msg.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kavir.ali.kavir_msg.BuildConfig;
import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.utility.FormatHelper;
import com.kavir.ali.kavir_msg.utility.Utility;

public class SplashActivity extends AppCompatActivity {
    private TextView versionNameTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utility.getNewVersion(this);
        versionNameTv =(TextView)findViewById(R.id.version_name);
        versionNameTv.setText(FormatHelper.toPersianNumber(BuildConfig.VERSION_NAME));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,CategoryActivity.class));
                finish();
            }
        },3000);
    }
}
