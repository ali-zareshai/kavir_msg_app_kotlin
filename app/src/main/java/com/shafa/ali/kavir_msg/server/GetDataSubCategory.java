package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.SubCategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataSubCategory {
    @GET("get_cat_and_sub_apk/?type=sub&dev=0")
    Call<List<SubCategoryModel>> getAllSubCategorys(@Query ("cookie") String cookieValue,@Query("cat_id")String parentId);
}
