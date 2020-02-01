package com.shafa.ali.kavir_msg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.server.LoginServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;

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
        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
        sendQrCode(result.getText());
    }

    private void sendQrCode(String qCode){
        mScannerView.stopCameraPreview();
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.activeUser(qCode, Utility.calSCode(this, SaveItem.getItem(this,SaveItem.REGISTER_PHONE,"").split(""))).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("onResponse",response.toString());
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });
    }
}
