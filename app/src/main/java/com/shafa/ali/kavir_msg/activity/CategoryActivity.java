package com.shafa.ali.kavir_msg.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.adapters.CategoryAdapter;
import com.shafa.ali.kavir_msg.fragments.AboutFragment;
import com.shafa.ali.kavir_msg.fragments.AccessFragment;
import com.shafa.ali.kavir_msg.fragments.ActiveFragment;
import com.shafa.ali.kavir_msg.fragments.CategoryFragment;
import com.shafa.ali.kavir_msg.fragments.ReadyReadFragment;
import com.shafa.ali.kavir_msg.fragments.RegisterFragment;
import com.shafa.ali.kavir_msg.interfaces.ClickListener;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.server.GetDataCategory;
import com.shafa.ali.kavir_msg.utility.CustomTypeFaceSpan;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mTopToolbar;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private TextView toolbarTitle,displayNameTv;
    private NavigationView navigationView;
    private ImageButton searchBtn;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        context = this;
        Log.e("mac    ",Utility.getMacAddr());
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle =(TextView) findViewById(R.id.category_title);
        searchBtn =(ImageButton) findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this,SearchActivity.class));
            }
        });
        setSupportActionBar(mTopToolbar);

        //***************************
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        displayNameTv =(TextView)headerView.findViewById(R.id.display_name_header);
        String displayName  =SaveItem.getItem(this,SaveItem.USER_NAME,"");
        if (displayName.equals("")){
            displayName = getString(R.string.guest);
        }
        displayNameTv.setText(FormatHelper.toPersianNumber(displayName));
        navigationView.setNavigationItemSelectedListener(this);
        setNameLoginItem();
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
                } else
                    drawer.openDrawer(GravityCompat.END);
            }
        });

        setFontNavig();


    }

    @Override
    protected void onResume() {
        try{
            CategoryFragment fragment = (CategoryFragment) getFragmentManager().findFragmentById(R.id.fragment);
            fragment.getDataFromServer();
        }catch (Exception e){
            Log.e("onResume:",e.getMessage());
        }

        super.onResume();
    }

    private void setFontNavig() {
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }

    private void loadFragment(Fragment fragmentNew) {
        transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if (fragment!=null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        // load fragment
        transaction.replace(R.id.fragment, fragmentNew);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.login) {
            loginProcess();
        } else if (id == R.id.home_page) {
            loadFragment(CategoryFragment.newInstance());
            toolbarTitle.setText(getString(R.string.home));
        } else if (id == R.id.ready_read_page) {
            loadFragment(ReadyReadFragment.newInstance());
            toolbarTitle.setText(getString(R.string.redy_read));
        }else if (id == R.id.about_we){
            loadFragment(AboutFragment.newInstance());
            toolbarTitle.setText(getString(R.string.about_we));
        }else if (id == R.id.register){
            loadFragment(RegisterFragment.newInstance());
            toolbarTitle.setText(getString(R.string.register));
        }else if (id==R.id.active){
            loadFragment(ActiveFragment.newInstance());
            toolbarTitle.setText(getString(R.string.active));
        }else if (id==R.id.access){
            loadFragment(AccessFragment.newInstance());
            toolbarTitle.setText(getString(R.string.access_list));
        }

        drawer.closeDrawer(Gravity.END);
        return true;
    }

    private void loginProcess() {
        Menu menu = navigationView.getMenu();
        MenuItem loginItem = menu.findItem(R.id.login);
        if (loginItem.getTitle().toString().equalsIgnoreCase(getString(R.string.login))){
            startActivity(new Intent(CategoryActivity.this,LoginActivity.class));
        }else{
            logout();
        }
    }

    private void logout() {
        clearSeesion();
        finish();
    }

    private void clearSeesion(){
        SaveItem.setItem(this,SaveItem.USER_FIRST_NAME,"");
        SaveItem.setItem(this,SaveItem.USER_LAST_NAME,"");
        SaveItem.setItem(this,SaveItem.USER_EMAIL,"");
        SaveItem.setItem(this,SaveItem.USER_NAME,"");
        SaveItem.setItem(this,SaveItem.USER_MOBILE,"");
        SaveItem.setItem(this,SaveItem.USER_ID,"");
        SaveItem.setItem(this,SaveItem.USER_COOKIE,"");
    }

    private void setNameLoginItem(){
        Menu menu = navigationView.getMenu();
        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem activeItem= menu.findItem(R.id.active);
        MenuItem registerItem = menu.findItem(R.id.register);
        MenuItem accessItem = menu.findItem(R.id.access);
        if (SaveItem.getItem(this,SaveItem.USER_COOKIE,"").equals("")){
            loginItem.setTitle(getString(R.string.login));
            activeItem.setVisible(false);
            registerItem.setVisible(true);
            accessItem.setVisible(false);
        }else{
            loginItem.setTitle(getString(R.string.logout));
            activeItem.setVisible(true);
            registerItem.setVisible(false);
            accessItem.setVisible(true);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Vazir.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, Color.WHITE), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }



}
