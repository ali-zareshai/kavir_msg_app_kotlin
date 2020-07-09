package com.kavirelectronic.ali.kavir_info.server

import com.kavirelectronic.ali.kavir_info.models.*
import retrofit2.Call
import retrofit2.http.*

interface LoginServer {
    @POST("login_apk")
    @FormUrlEncoded
    fun loginUser(@Field("user_login") userName: String?, @Field("user_password") password: String?, @Field("s") sCode: String?): Call<LoginModel?>?

    @POST("register_wp")
    @FormUrlEncoded
    fun registerUser(@Field("first_name") name: String?,
                     @Field("mobile") mobile: String?,
                     @Field("user_email") email: String?,
                     @Field("user_pass") password: String?,
                     @Field("mid") mid: String?,
                     @Field("s") sCode: String?): Call<RegisterModel?>?

    @POST("active_apk")
    @FormUrlEncoded
    fun activeUser(@Field("active_code") activeCode: String?,
                   @Field("s") sCode: String?): Call<ActiveRespone?>?

    @GET("get_secret_code/?action=kavir")
    fun getSecretCode(@Query("version_code") versionCode: String?): Call<SecretCodeModel?>?

    @GET("get_access_user")
    fun getAccessList(@Query("apk_id") apkId: String?, @Query("s") sCode: String?): Call<List<AccessModel?>?>?
}