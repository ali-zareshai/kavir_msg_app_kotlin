package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetPostsServer {
    @GET("post-title.php")
    Call<TiltlePostsModel> getTiltlePosts(@Query ("cookie") String cookieValue,@Query("slug") String slug, @Query("page") String page,@Query("page-size")String pageSize);

    @GET("post.php")
    Call<PostModel> getPost(@Query ("cookie") String cookieValue,@Query("id") String postId);

    @GET("search.php")
    Call<List<TiltlePostsModel.PostsModel>> getSearch(@Query ("cookie") String cookieValue, @Query("q")String query);

}
