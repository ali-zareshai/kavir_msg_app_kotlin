package com.shafa.ali.kavir_msg.server;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Comments {
    @POST("submit_comment_apk")
    @FormUrlEncoded
    Call<String> postNewComment(@Field("post_id") String postId, @Field("name")String name, @Field("email") String email, @Field("content")String content);
}
