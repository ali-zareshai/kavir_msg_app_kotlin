package com.shafa.ali.kavir_msg.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.activity.CategoryActivity;
import com.shafa.ali.kavir_msg.activity.SubCategoryActivity;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.db.models.Post;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.utility.RecyclerTouchListener;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryFragment extends Fragment {
    private static Fragment fragment = null;
    private RecyclerView categoryRecycler;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SpinKitView loading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<CategoryModel> categoryModelList;


    public CategoryFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        if (fragment == null){
            fragment = new CategoryFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryRecycler =(RecyclerView)view.findViewById(R.id.category_recyclerview);
        loading = (SpinKitView)view.findViewById(R.id.spin_cat);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_category);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        categoryRecycler.setLayoutManager(layoutManager);
        getDataFromServer();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });

        return view;
    }

    private void hideSwipRefresh(){
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void getDataFromServer() {
        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        GetDataCategory getDataService=retrofit.create(GetDataCategory.class);
        getDataService.getAllCategorys(SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.USER_COOKIE,"")).enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
//                Log.e("msg",response.body().toString());
                if (response.isSuccessful()){
                    categoryModelList = response.body();
                    generateDataList(categoryModelList);
                    loading.setVisibility(View.GONE);
                    hideSwipRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
//                Log.e("msg1",t.getLocalizedMessage());
//                Log.e("msg2",t.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        categoryRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(),
                categoryRecycler, new ClickListener() {    @Override
        public void onClick(View view, final int position) {
            TextView nameTv   = (TextView)view.findViewById(R.id.name_category);
            String id = String.valueOf(categoryModelList.get(position).getId());
            Intent intent =new Intent(getActivity().getApplicationContext(), SubCategoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("parentId",id);
            bundle.putString("parentName",nameTv.getText().toString());
            intent.putExtras(bundle);
            setCountPostCount(id,categoryModelList.get(position).getPost_count());
            startActivity(intent);
        }

            @Override
            public void onLongClick(View view, int position) {

            }}));

    }

    private void generateDataList(List<CategoryModel> categoryModelList){
        categoryAdapter =new CategoryAdapter(getActivity().getApplicationContext(),categoryModelList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    private void setCountPostCount(String postId,int newCountPost){
        SaveItem.setItem(getActivity().getApplicationContext(),"post:"+postId,String.valueOf(newCountPost));

    }



}
