package com.shafa.ali.kavir_msg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.utility.Utility;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView register;
    private ImageView qrcodeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register =(TextView)findViewById(R.id.registerText);
        qrcodeImg =(ImageView)findViewById(R.id.qrcode);
        register.setOnClickListener(this);
        qrcodeImg.setOnClickListener(this);
        Toast.makeText(this, Utility.getUniqueIMEIId(this), Toast.LENGTH_LONG).show();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerText:
                Intent i2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i2);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                break;
            case R.id.qrcode:
                Intent i = new Intent(LoginActivity.this,QrCodeScanerActivity.class);
                startActivity(i);
        }
    }
}
