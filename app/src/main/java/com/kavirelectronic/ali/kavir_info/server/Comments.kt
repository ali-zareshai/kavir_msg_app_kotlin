package com.kavirelectronic.ali.kavir_info.server

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Comments {
    @POST("submit_comment_apk")
    @FormUrlEncoded
    fun postNewComment(@Field("s") sCode: String?, @Field("apk_id") apkId: String?, @Field("post_id") postId: String?, @Field("name") name: String?, @Field("email") email: String?, @Field("content") content: String?): Call<String?>?
}