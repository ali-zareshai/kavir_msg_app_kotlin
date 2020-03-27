package com.kavirelectronic.ali.kavir_info.server;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Comments {
    @POST("submit_comment_apk")
    @FormUrlEncoded
    Call<String> postNewComment(@Field("s")String sCode,@Field("apk_id")String apkId, @Field("post_id") String postId, @Field("name")String name, @Field("email") String email, @Field("content")String content);
}
