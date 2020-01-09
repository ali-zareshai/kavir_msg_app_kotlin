package com.shafa.ali.kavir_msg.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.shafa.ali.kavir_msg.activity.SubCategoryActivity;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.adapters.TitleAdapter;
import com.shafa.ali.kavir_msg.db.models.Post;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.models.CommentModel;
import com.shafa.ali.kavir_msg.models.PostModel;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.utility.RecyclerTouchListener;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReadyReadFragment extends Fragment {
    private static Fragment fragment = null;
    private RecyclerView categoryRecycler;
    private TitleAdapter titleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SpinKitView loading;


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
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        categoryRecycler.setLayoutManager(layoutManager);
        getDataFromDb();

        return view;
    }

    private void getDataFromDb() {
        Realm realm = Realm.getDefaultInstance();
        List<Post> ModelList = realm.where(Post.class).findAll();
        List<TiltlePostsModel.PostsModel> postsModels = new ArrayList<>();
        for(Post post:ModelList){
            TiltlePostsModel.PostsModel postModel = new TiltlePostsModel.PostsModel();

            postModel.setTitle(post.getTitle());
            postModel.setAuthor(post.getAuthor());
            postModel.setContent(post.getContent());
            postModel.setDate(post.getDate());
            postModel.setId(post.getId());

            postsModels.add(postModel);
        }
        titleAdapter =new TitleAdapter(getActivity().getApplicationContext(),postsModels);
        categoryRecycler.setAdapter(titleAdapter);
    }


}
