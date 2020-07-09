package com.kavirelectronic.ali.kavir_info.server

import com.kavirelectronic.ali.kavir_info.models.PostModel
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel.PostsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetPostsServer {
    @GET("get_post_title?dev=0")
    fun getTiltlePostsBySlug(@Query("cookie") cookieValue: String?, @Query("slug") slug: String?, @Query("page") page: String?, @Query("count") pageSize: String?): Call<TiltlePostsModel?>?

    @GET("get_post_by_cat_id_apk?dev=0")
    fun getTitlePostByCatId(@Query("cookie") cookieValue: String?, @Query("id") catId: String?, @Query("page") page: String?, @Query("count") pageSize: String?): Call<TiltlePostsModel?>?

    @GET("get_post_apk?dev=0")
    fun getPost(@Query("cookie") cookieValue: String?, @Query("id") postId: String?): Call<PostModel?>?

    @GET("get_search_results_apk?dev=0")
    fun getSearch(@Query("cookie") cookieValue: String?, @Query("q") query: String?): Call<List<PostsModel?>?>?
}