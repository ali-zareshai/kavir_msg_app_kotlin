package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.ActiveRespone;
import com.shafa.ali.kavir_msg.models.LoginModel;
import com.shafa.ali.kavir_msg.models.RegisterModel;
import com.shafa.ali.kavir_msg.models.SecretCodeModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginServer {
    @POST("login.php")
    @FormUrlEncoded
    Call<LoginModel> loginUser(@Field("username")String userName,@Field("password")String password);

    @POST("register_wp")
    @FormUrlEncoded
    Call<RegisterModel> registerUser(@Field("first_name")String name,
                                     @Field("mobile")String mobile,
                                     @Field("user_email")String email,
                                     @Field("user_pass")String password,
                                     @Field("mid")String mid,
                                     @Field("s")String sCode);

    @POST("active")
    @FormUrlEncoded
    Call<ActiveRespone> activeUser(@Field("active_code")String activeCode,
                                   @Field("s")String sCode);

    @GET("get_secret_code/?action=kavir")
    Call<SecretCodeModel> getSecretCode();
}
