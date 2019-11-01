package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.SubCategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataSubCategory {
    @GET("test.php?type=sub")
    Call<List<SubCategoryModel>> getAllSubCategorys(@Query("cat-id")String parentId);
}
