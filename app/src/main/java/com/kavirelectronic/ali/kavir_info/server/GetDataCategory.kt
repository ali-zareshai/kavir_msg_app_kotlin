package com.kavirelectronic.ali.kavir_info.server

import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.models.SubCategoryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataCategory {
    @GET("get_cat_and_sub_all_apk/?type=cat&dev=0")
    fun getAllCategorysAndSub(@Query("cookie") cookieValue: String?): Call<List<CategoryModel?>?>?

    @GET("get_cat_and_sub_apk/?type=cat&dev=0")
    fun getAllCategorys(@Query("cookie") cookieValue: String?): Call<List<CategoryModel?>?>?

    @GET("get_cat_and_sub_apk/?type=sub&dev=0")
    fun getAllSubCategorys(@Query("cookie") cookieValue: String?, @Query("cat_id") parentId: String?): Call<List<SubCategoryModel?>?>?
}