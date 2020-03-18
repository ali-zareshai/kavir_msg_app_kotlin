package com.shafa.ali.kavir_msg.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.AccessAdapter;
import com.shafa.ali.kavir_msg.models.AccessModel;
import com.shafa.ali.kavir_msg.server.LoginServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccessFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private AccessAdapter accessAdapter;


    public AccessFragment() {
    }

    public static AccessFragment newInstance() {
        AccessFragment fragment = new AccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_access, container, false);
        recyclerView =(RecyclerView)view.findViewById(R.id.access_recyclerview);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_access);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        getDataFromServer();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
        return view;
    }

    private void getDataFromServer() {
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        final LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.getAccessList(SaveItem.getItem(getActivity(),SaveItem.APK_ID,""), SaveItem.getItem(getActivity(),SaveItem.S_CODE,""))
                .enqueue(new Callback<List<AccessModel>>() {
                    @Override
                    public void onResponse(Call<List<AccessModel>> call, Response<List<AccessModel>> response) {
                        if (response.isSuccessful()){
                            try{
                                if (response.body().size()==0){
                                    MDToast.makeText(getActivity(),getActivity().getString(R.string.not_access_active),3000,MDToast.TYPE_ERROR).show();
                                }else {
                                    accessAdapter = new AccessAdapter(getActivity(),response.body());
                                    recyclerView.setAdapter(accessAdapter);
                                }

                            }catch (Exception e){
                                Log.e("onResponse access:",e.getMessage());
                            }

                        }
                        if (swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AccessModel>> call, Throwable t) {
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
                    }
                });
    }
}
