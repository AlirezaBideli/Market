package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.market.R;
import com.example.market.controllers.fragmnet.DetailFragment;
import com.example.market.controllers.fragmnet.MarketFragment;
import com.example.market.controllers.fragmnet.ProductFragment;
import com.example.market.interfaces.ActivityStart;
import com.example.market.interfaces.NetworkControll;
import com.example.market.model.ProductLab;
import com.example.market.utils.ActivityHeper;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MarketActivity extends SingleFragmentActivity implements ActivityStart
        , NetworkControll, NavigationView.OnNavigationItemSelectedListener, MarketFragment.CallBacks,ProductFragment.CallBacks {


    //Widgets Variables
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


    @Override
    public Fragment createFragment() {
        return MarketFragment.newInstance();
    }

    @Override
    public int getContainerId() {
        return R.id.container_MarkerA;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_market;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewByIds();
        variableInit();
        setListeners();


    }


    @Override
    public void findViewByIds() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
    }

    @Override
    public void variableInit() {

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MarketActivity.this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void checkConnection() {

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {

            case R.id.categories_list:
                goToCategories();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goToCategories() {
        Intent intent = ActivityHeper.Intent_CategoryA(MarketActivity.this);
        startActivity(intent);
    }


    @Override
    public void showProductDetails(int id) {
        AsyncTask productTask= ProductLab.getInstance().getProductTask(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProductFragment fragment = ProductFragment.newInstance(id);
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_MarkerA,fragment)
                .commit();
    }

    @Override
    public void showDetails(int id) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_MarkerA, DetailFragment.newInstance(id))
                .commit();


    }
}
