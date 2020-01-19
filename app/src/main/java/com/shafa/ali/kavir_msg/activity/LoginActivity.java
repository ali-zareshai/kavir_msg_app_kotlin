package com.shafa.ali.kavir_msg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.LoginModel;
import com.shafa.ali.kavir_msg.server.LoginServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import net.igenius.customcheckbox.CustomCheckBox;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView register,loginBtn;
    private ImageView qrcodeImg;
    private EditText userNameEdit,passwordEdit;
    private CustomCheckBox saveLoginCk;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register =(TextView)findViewById(R.id.registerText);
        loginBtn =(TextView)findViewById(R.id.loginBtn);
        qrcodeImg =(ImageView)findViewById(R.id.qrcode);
        userNameEdit =(EditText)findViewById(R.id.usernameEd);
        passwordEdit =(EditText)findViewById(R.id.passwordEd);
        saveLoginCk =(CustomCheckBox)findViewById(R.id.save_pass_ckb);
        progressBarLogin =(ProgressBar)findViewById(R.id.progress_login);
        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        qrcodeImg.setOnClickListener(this);
//        Toast.makeText(this, Utility.getUniqueIMEIId(this), Toast.LENGTH_LONG).show();
        /////////////// set save user pass
        userNameEdit.setText(SaveItem.getItem(this,SaveItem.USERNAME_LOGIN,""));
        passwordEdit.setText(SaveItem.getItem(this,SaveItem.PASSWORD_LOGIN,""));
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
                break;
            case R.id.loginBtn:
                loginProcess();
                break;
        }
    }

    private void saveUsername(String userName,String password) {
        if (saveLoginCk.isChecked()){
            SaveItem.setItem(this,SaveItem.USERNAME_LOGIN,userName);
            SaveItem.setItem(this,SaveItem.PASSWORD_LOGIN,password);
        }
    }

    private void loginProcess() {
        if (!checkUserPass()){
            MDToast.makeText(this,getString(R.string.full_all_fields),2500,MDToast.TYPE_ERROR).show();
            return;
        }
        String userName = userNameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        loginUser(userName,password);
    }

    private boolean checkUserPass() {
        if (userNameEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void loginUser(final String username, final String password){
        progressBarLogin.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.loginUser(username,password).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResult().equalsIgnoreCase("success")){
                        saveLoginData(response.body());
                        saveUsername(username,password);
                        CategoryActivity categoryActivity = (CategoryActivity)CategoryActivity.context;
                        categoryActivity.finish();
                        startActivity(new Intent(LoginActivity.this,CategoryActivity.class));
                        MDToast.makeText(getApplicationContext(),getString(R.string.welcome),2500,MDToast.TYPE_SUCCESS).show();
                        finish();
                    }else {
                        MDToast.makeText(getApplicationContext(),getString(R.string.user_or_pass_is_wrong),2500,MDToast.TYPE_WARNING).show();
                    }
                }else {
                    MDToast.makeText(getApplicationContext(),getString(R.string.error_in_connection),2500,MDToast.TYPE_WARNING).show();

                }
                progressBarLogin.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
//                Log.e("onFailure:",t.getMessage());
                progressBarLogin.setVisibility(View.GONE);
                MDToast.makeText(getApplicationContext(),getString(R.string.error_in_connection),2500,MDToast.TYPE_WARNING).show();
            }
        });
    }



    private void saveLoginData(LoginModel body) {
        SaveItem.setItem(this,SaveItem.USER_FIRST_NAME,body.getFirstName());
        SaveItem.setItem(this,SaveItem.USER_LAST_NAME,body.getLastName());
        SaveItem.setItem(this,SaveItem.USER_EMAIL,body.getEmail());
        SaveItem.setItem(this,SaveItem.USER_NAME,body.getDisplayName());
        SaveItem.setItem(this,SaveItem.USER_MOBILE,body.getMobile());
        SaveItem.setItem(this,SaveItem.USER_ID,body.getUserId());
        SaveItem.setItem(this,SaveItem.USER_COOKIE,body.getCookie());
    }
}
