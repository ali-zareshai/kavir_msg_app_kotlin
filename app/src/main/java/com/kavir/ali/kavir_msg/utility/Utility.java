package com.kavir.ali.kavir_msg.utility;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.kavir.ali.kavir_msg.BuildConfig;
import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.models.SecretCodeModel;
import com.kavir.ali.kavir_msg.server.LoginServer;
import com.tapadoo.alerter.Alerter;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Utility {
    public static String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }



    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().trim().replace(":","");
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public static String calSCode(Context context,String[] numberPhone){
        try {
            int n10 = Integer.parseInt(numberPhone[(numberPhone.length)-1]);
            int n9  = Integer.parseInt(numberPhone[(numberPhone.length)-2]);
            String sCode = SaveItem.getItem(context,SaveItem.raw_Scode,"");
            Log.e("sCode:",sCode);
            if (sCode.length()>0){
                int index = n9+n10;
                return sCode.substring(index,index+10);
            }
            return "";
        }catch (Exception e){
            return "";
        }
    }

    public static void getNewVersion(final Activity context){
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.getSecretCode(String.valueOf(BuildConfig.VERSION_CODE)).enqueue(new Callback<SecretCodeModel>() {
            @Override
            public void onResponse(Call<SecretCodeModel> call, Response<SecretCodeModel> response) {
                if (response.body().getResult().equalsIgnoreCase("success")){
                    checkNewVersion(context,response.body());
                }else {
                    MDToast.makeText(context,response.body().getMessage(),2500,MDToast.TYPE_INFO).show();
                }
            }

            @Override
            public void onFailure(Call<SecretCodeModel> call, Throwable t) {
                Log.e("onFailure:",t.toString());
            }
        });
    }

    public static void getSecretCode(final Activity context){
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.getSecretCode(String.valueOf(BuildConfig.VERSION_CODE)).enqueue(new Callback<SecretCodeModel>() {
            @Override
            public void onResponse(Call<SecretCodeModel> call, Response<SecretCodeModel> response) {
                if (response.body().getResult().equalsIgnoreCase("success")){
                    String s_raw = response.body().getSecretCode().trim();
                    SaveItem.setItem(context,SaveItem.raw_Scode,s_raw.substring(0,30));
                }else {
                    MDToast.makeText(context,response.body().getMessage(),2500,MDToast.TYPE_INFO).show();
                }
            }

            @Override
            public void onFailure(Call<SecretCodeModel> call, Throwable t) {
                Log.e("onFailure:",t.toString());
            }
        });
    }

    private static void checkNewVersion(Activity context,SecretCodeModel body) {
        if (body.getVersionCode()==null){
            return;
        }
        if (Integer.parseInt(body.getVersionCode())> BuildConfig.VERSION_CODE){
            SaveItem.setItem(context,SaveItem.COME_NEW_VERSION,"1");
            SaveItem.setItem(context,SaveItem.NEW_VERSION_NAME,body.getVersionName());
            SaveItem.setItem(context,SaveItem.NEW_VERSION_URL,body.getUpdateUrl());
        }else{
            SaveItem.setItem(context,SaveItem.COME_NEW_VERSION,"0");
        }
    }

    public static void showAlertNewVersion(final Activity context, String versionName, final String updateUrl) {
        Alerter.create(context)
                .setTitle(context.getString(R.string.new_version))
                .setText(context.getString(R.string.version)+" "+versionName+" "+context.getString(R.string.available))
                .setDuration(10000)
                .setContentGravity(Gravity.CENTER)
                .setTitleTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Vazir.ttf"))
                .setTextTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/sans.ttf"))
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(updateUrl);
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(updateUrl)));
                        }
                    }
                })
                .show();
    }

    public static String calMID(String[] numberPhone){
        try{
            int n10 = Integer.parseInt(numberPhone[(numberPhone.length)-1]);
            int n9  = Integer.parseInt(numberPhone[(numberPhone.length)-2]);
            String[] mac = Utility.getMacAddr().split("");
            return n9+mac[n9+1]+n10+mac[n10+1]+"";


        }catch (Exception e){
            return "";
        }
    }

    public static String calApkId(Context context,String[] numberPhone){
        try {
            String mid = calMID(numberPhone);
            String uid = SaveItem.getItem(context,SaveItem.USER_ID,"");
            int len = uid.length();
            SaveItem.setItem(context,SaveItem.APK_ID,mid+len+uid);
            return mid+len+uid;
        }catch (Exception e){
            return "";
        }
    }





}
