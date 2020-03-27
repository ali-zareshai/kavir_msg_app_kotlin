package com.kavir.ali.kavir_msg.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ybq.android.spinkit.SpinKitView;
import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.activity.PostActivity;
import com.kavir.ali.kavir_msg.adapters.TitleAdapter;
import com.kavir.ali.kavir_msg.db.models.Post;
import com.kavir.ali.kavir_msg.interfaces.ClickListener;
import com.kavir.ali.kavir_msg.models.TiltlePostsModel;
import com.kavir.ali.kavir_msg.utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ReadyReadFragment extends Fragment {
    private static Fragment fragment = null;
    private RecyclerView categoryRecycler;
    private TitleAdapter titleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SpinKitView loading;
    private List<Post> modelListDb;
    private SwipeRefreshLayout swipeRefreshLayout;


    public ReadyReadFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        if (fragment == null){
            fragment = new ReadyReadFragment();
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
        View view = inflater.inflate(R.layout.fragment_ready_read, container, false);
        categoryRecycler =(RecyclerView)view.findViewById(R.id.ready_read_recyclerview);
        loading = (SpinKitView)view.findViewById(R.id.spin_cat);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_ready_red);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        categoryRecycler.setLayoutManager(layoutManager);
        getDataFromDb();
        initRecycle();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromDb();
            }
        });

        return view;
    }
    private void hideSwipRefresh(){
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initRecycle() {
        categoryRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), categoryRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostActivity.class);
                intent.putExtra("postId",modelListDb.get(position).getId());
                intent.putExtra("source","db");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void getDataFromDb() {
        Realm realm = Realm.getDefaultInstance();
        modelListDb = realm.where(Post.class).findAll();
        List<TiltlePostsModel.PostsModel> postsModels = new ArrayList<>();
        for(Post post:modelListDb){
            TiltlePostsModel.PostsModel postModel = new TiltlePostsModel.PostsModel();

            postModel.setTitle(post.getTitle());
            postModel.setAuthor(post.getAuthor());
            postModel.setContent(post.getContent());
            postModel.setDate(post.getDate());
            postModel.setId(post.getId());
            postModel.setCommentCount("0");

            postsModels.add(postModel);
        }
        titleAdapter =new TitleAdapter(getActivity().getApplicationContext(),postsModels);
        categoryRecycler.setAdapter(titleAdapter);
        loading.setVisibility(View.GONE);
        hideSwipRefresh();
    }


}
