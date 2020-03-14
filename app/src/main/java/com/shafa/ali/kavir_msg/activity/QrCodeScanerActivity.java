package com.shafa.ali.kavir_msg.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.ActiveRespone;
import com.shafa.ali.kavir_msg.server.LoginServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QrCodeScanerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qr_code_scaner);
        if( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},5);
            }
        }
        Utility.getSecretCode(getApplicationContext());
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        Log.e("qr code:",result.getText());
        if (checkQr(result.getText())){
            sendQrCode(result.getText());
        }else {
            MDToast.makeText(this,getString(R.string.qr_is_wrong),2500,MDToast.TYPE_ERROR).show();
        }

    }

    private boolean checkQr(String qr){
        if (SaveItem.getItem(this,SaveItem.APK_ID,"").equalsIgnoreCase(calApkCodeFromQr(qr.trim().split("")))){
            return true;
        }
        return false;
    }

    private String calApkCodeFromQr(String[] qr){
        try {
            String mid = qr[1]+qr[2]+qr[3]+qr[4]+"";
            int q5 = Integer.parseInt(qr[5]);
            String uid = "";
            for (int i=0;i<q5;i++){
                uid = uid + qr[i+6];
            }
            uid = qr[5]+uid;
            return mid+uid;
        }catch (Exception e){
            Log.e("calApkCodeFromQr:",e.getMessage());
            return "";
        }

    }

    private void sendQrCode(String qCode){
        mScannerView.stopCameraPreview();
        Log.e("secrt code:",SaveItem.getItem(this,SaveItem.S_CODE,""));
        Log.e("scode:",Utility.calSCode(this, SaveItem.getItem(this,SaveItem.REGISTER_PHONE,"").split("")));
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.activeUser(qCode, Utility.calSCode(this, SaveItem.getItem(this,SaveItem.REGISTER_PHONE,"").split(""))).enqueue(new Callback<ActiveRespone>() {
            @Override
            public void onResponse(Call<ActiveRespone> call, Response<ActiveRespone> response) {
                MDToast.makeText(getApplicationContext(),response.body().getMessage(),2500,MDToast.TYPE_INFO).show();
                finish();
            }

            @Override
            public void onFailure(Call<ActiveRespone> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
            }
        });

    }
}
