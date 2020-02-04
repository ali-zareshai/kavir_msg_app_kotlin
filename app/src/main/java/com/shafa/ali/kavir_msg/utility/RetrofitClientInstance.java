package com.shafa.ali.kavir_msg.utility;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit,retrofit2;
    private static OkHttpClient okHttpClient,okHttpClient2;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Setting.API_WORDPRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceNew() {

        if (retrofit2 == null) {
            okHttpClient2 = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
            retrofit2 = new retrofit2.Retrofit.Builder()
                    .baseUrl(Setting.API_WORDPRESS_NEW)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit2;
    }
}