package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.TiltlePostsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPostsServer {
    @GET("post-title.php")
    Call<TiltlePostsModel> getTiltlePosts(@Query("slug") String slug, @Query("page") String page);
}
