package com.shafa.ali.kavir_msg.server;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Comments {
    @POST("comment.php")
    @FormUrlEncoded
    Call<String> postNewComment(@Field("id") String postId, @Field("name")String name, @Field("email") String email, @Field("content")String content);
}
