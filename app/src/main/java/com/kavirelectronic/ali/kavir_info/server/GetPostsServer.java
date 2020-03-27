package com.kavirelectronic.ali.kavir_info.server;

import com.kavirelectronic.ali.kavir_info.models.PostModel;
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPostsServer {
    @GET("get_post_title?dev=0")
    Call<TiltlePostsModel> getTiltlePostsBySlug(@Query ("cookie") String cookieValue,@Query("slug") String slug, @Query("page") String page,@Query("count")String pageSize);

    @GET("get_post_by_cat_id_apk?dev=0")
    Call<TiltlePostsModel> getTitlePostByCatId(@Query ("cookie") String cookieValue,@Query("id") String catId, @Query("page") String page,@Query("count")String pageSize);

    @GET("get_post_apk?dev=0")
    Call<PostModel> getPost(@Query ("cookie") String cookieValue,@Query("id") String postId);

    @GET("get_search_results_apk?dev=0")
    Call<List<TiltlePostsModel.PostsModel>> getSearch(@Query ("cookie") String cookieValue, @Query("q")String query);

}
