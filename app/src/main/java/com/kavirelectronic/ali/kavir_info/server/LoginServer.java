package com.kavirelectronic.ali.kavir_info.server;

import com.kavirelectronic.ali.kavir_info.models.AccessModel;
import com.kavirelectronic.ali.kavir_info.models.ActiveRespone;
import com.kavirelectronic.ali.kavir_info.models.LoginModel;
import com.kavirelectronic.ali.kavir_info.models.RegisterModel;
import com.kavirelectronic.ali.kavir_info.models.SecretCodeModel;

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
