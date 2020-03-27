package com.kavir.ali.kavir_msg.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.activity.SubCategoryActivity;
import com.kavir.ali.kavir_msg.activity.TitlePostsActivity;
import com.kavir.ali.kavir_msg.adapters.CategoryAdapter;
import com.kavir.ali.kavir_msg.interfaces.ClickListener;
import com.kavir.ali.kavir_msg.models.CategoryModel;
import com.kavir.ali.kavir_msg.server.GetDataCategory;
import com.kavir.ali.kavir_msg.utility.RecyclerTouchListener;
import com.kavir.ali.kavir_msg.utility.RetrofitClientInstance;
import com.kavir.ali.kavir_msg.utility.SaveItem;
import com.kavir.ali.kavir_msg.utility.Setting;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import dmax.dialog.SpotsDialog;
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
    private AlertDialog dialog;


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
        Setting.isVistied = false;
        categoryRecycler =(RecyclerView)view.findViewById(R.id.category_recyclerview);
        loading = (SpinKitView)view.findViewById(R.id.spin_cat);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_category);
        dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage(R.string.please_wait)
                .build();
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
        dialog.show();
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

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity())
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle(getString(R.string.not_respone))
                        .setIcon(R.drawable.access_server)
                        .setTextGravity(Gravity.CENTER)
                        .addButton(getString(R.string.refresh_page), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getDataFromServer();
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
                dialog.dismiss();
            }
        });

        categoryRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(),
                categoryRecycler, new ClickListener() {    @Override
        public void onClick(View view, final int position) {
            dialog.show();
            CategoryModel model = categoryModelList.get(position);
            setCountPostCount(model.getId()+"",categoryModelList.get(position).getPost_count());
            if (model.getSubCount()>0){
                Intent intent =new Intent(getActivity(), SubCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("parentId",model.getId()+"");
                bundle.putString("parentName",model.getTitle());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else if (model.getSubCount()==0 && model.getPost_count()>0){
                Intent intent =new Intent(getActivity(), TitlePostsActivity.class);
                intent.putExtra("post_only",true);
                intent.putExtra("cat_id",model.getId()+"");
                intent.putExtra("cat_name",model.getTitle());
                intent.putExtra("post_size",model.getPost_count());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                MDToast.makeText(getActivity(),getActivity().getString(R.string.empty_cat),2500,MDToast.TYPE_WARNING).show();
            }
            dialog.dismiss();
        }

            @Override
            public void onLongClick(View view, int position) {

            }}));

    }

    private void generateDataList(List<CategoryModel> categoryModelList){
        categoryAdapter =new CategoryAdapter(getActivity(),categoryModelList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    private void setCountPostCount(String postId,int newCountPost){
        SaveItem.setItem(getActivity().getApplicationContext(),"post:"+postId,String.valueOf(newCountPost));

    }



}
