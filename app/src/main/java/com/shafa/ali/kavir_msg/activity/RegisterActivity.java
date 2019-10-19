package com.shafa.ali.kavir_msg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = (TextView)findViewById(R.id.registerBtn);
    }

    @Override
    public void onClick(View view) {

    }
}
