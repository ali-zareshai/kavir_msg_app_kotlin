package com.shafa.ali.kavir_msg.activity;

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
//        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
        sendQrCode(result.getText());
    }

    private void sendQrCode(String qCode){
        mScannerView.stopCameraPreview();
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstanceNew();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.activeUser(qCode, Utility.calSCode(this, SaveItem.getItem(this,SaveItem.REGISTER_PHONE,"").split(""))).enqueue(new Callback<ActiveRespone>() {
            @Override
            public void onResponse(Call<ActiveRespone> call, Response<ActiveRespone> response) {
                if (response.body().getMessage().equalsIgnoreCase("success")){
                    MDToast.makeText(getApplicationContext(),response.body().getMessage(),2500,MDToast.TYPE_INFO).show();
                }else {
                    Log.e("onResponse:",response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ActiveRespone> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
            }
        });

    }
}
