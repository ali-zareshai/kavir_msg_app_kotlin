package com.shafa.ali.kavir_msg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView categoryRecycler;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar mTopToolbar;
    private SpinKitView loading;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryRecycler =(RecyclerView)findViewById(R.id.category_recyclerview);
        loading = (SpinKitView)findViewById(R.id.spin_cat);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        ///
        layoutManager = new LinearLayoutManager(this);
        categoryRecycler.setLayoutManager(layoutManager);

        //***************************
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        //////***************************************
        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer

                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                else
                    drawer.openDrawer(GravityCompat.END);
            }
        });

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
                    loading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
//                Log.e("msg1",t.getLocalizedMessage());
                Log.e("msg2",t.getMessage());
                Toast.makeText(CategoryActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        categoryRecycler.addOnItemTouchListener(new RecyclerTouchListener(this,
                categoryRecycler, new ClickListener() {    @Override
        public void onClick(View view, final int position) {
            TextView textView =(TextView)view.findViewById(R.id.id_category);
            String id =textView.getText().toString().trim();
            Intent intent =new Intent(CategoryActivity.this,SubCategoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("parentId",id);
            Log.e("parent id:::::::", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(CategoryActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }}));

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    private void generateDataList(List<CategoryModel> categoryModelList){
        Log.e("category list",categoryModelList.toString());
        categoryAdapter =new CategoryAdapter(getApplicationContext(),categoryModelList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(Gravity.END);
        return true;
    }


    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
