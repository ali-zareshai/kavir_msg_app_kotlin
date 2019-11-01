package com.shafa.ali.kavir_msg.server;

import com.shafa.ali.kavir_msg.models.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataCategory {
    @GET("test.php?type=cat")
    Call<List<CategoryModel>> getAllCategorys();
}
