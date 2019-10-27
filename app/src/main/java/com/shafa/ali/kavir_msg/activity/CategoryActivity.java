package com.shafa.ali.kavir_msg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecycler;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryRecycler =(RecyclerView)findViewById(R.id.category_recyclerview);
        ///
        layoutManager = new LinearLayoutManager(this);
        categoryRecycler.setLayoutManager(layoutManager);

        //
        /*Create handle for the RetrofitInstance interface*/
        Retrofit retrofit=RetrofitClientInstance.getRetrofitInstance();
        GetDataCategory getDataService=retrofit.create(GetDataCategory.class);
        getDataService.getAllCategorys().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
//                Log.e("msg",response.body().toString());
                if (response.isSuccessful()){
                    generateDataList(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
//                Log.e("msg1",t.getLocalizedMessage());
                Log.e("msg2",t.getMessage());
                Toast.makeText(CategoryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void generateDataList(List<CategoryModel> categoryModelList){
        Log.e("category list",categoryModelList.toString());
        categoryAdapter =new CategoryAdapter(getApplicationContext(),categoryModelList);
        categoryRecycler.setAdapter(categoryAdapter);

    }
}
