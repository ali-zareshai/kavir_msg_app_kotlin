package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.AccessModel;
import com.shafa.ali.kavir_msg.models.ActiveRespone;
import com.shafa.ali.kavir_msg.models.LoginModel;
import com.shafa.ali.kavir_msg.models.RegisterModel;
import com.shafa.ali.kavir_msg.models.SecretCodeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginServer {
    @POST("login_apk")
    @FormUrlEncoded
    Call<LoginModel> loginUser(@Field("user_login")String userName,@Field("user_password")String password,@Field("s")String sCode);

    @POST("register_wp")
    @FormUrlEncoded
    Call<RegisterModel> registerUser(@Field("first_name")String name,
                                     @Field("mobile")String mobile,
                                     @Field("user_email")String email,
                                     @Field("user_pass")String password,
                                     @Field("mid")String mid,
                                     @Field("s")String sCode);

    @POST("active_apk")
    @FormUrlEncoded
    Call<ActiveRespone> activeUser(@Field("active_code")String activeCode,
                                   @Field("s")String sCode);

    @GET("get_secret_code/?action=kavir")
    Call<SecretCodeModel> getSecretCode(@Query("version_code")String versionCode);

    @GET("get_access_user")
    Call<List<AccessModel>> getAccessList(@Query("apk_id") String apkId, @Query("s")String sCode);
}
