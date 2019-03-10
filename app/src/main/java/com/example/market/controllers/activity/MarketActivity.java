package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.example.market.R;
import com.example.market.controllers.fragmnet.ConnectionDialog;
import com.example.market.controllers.fragmnet.DetailFragment;
import com.example.market.controllers.fragmnet.MarketFragment;
import com.example.market.controllers.fragmnet.ProductFragment;
import com.example.market.controllers.fragmnet.SearchProductFragment;
import com.example.market.controllers.fragmnet.ShoppingCartFragment;
import com.example.market.model.ActivityStart;
import com.example.market.model.DetailCallBack;
import com.example.market.model.LoadingCallBack;
import com.example.market.utils.ActivityHeper;
import com.example.market.utils.KeyBoardUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MarketActivity extends SingleFragmentActivity implements ActivityStart
        , NavigationView.OnNavigationItemSelectedListener, MarketFragment.CallBacks
        , ProductFragment.CallBacks, LoadingCallBack, ConnectionDialog.CallBacks, DetailCallBack  {


    //simple Variables
    Handler mHandler = new Handler();
    //Widgets Variables
    private Toolbar mToolbar;
    private FrameLayout mLoadingCover;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;

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
        mLoadingCover = findViewById(R.id.cover_marketA);

    }

    @Override
    public void variableInit() {

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MarketActivity.this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mFragmentManager = getSupportFragmentManager();


    }

    @Override
    public void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        List<Fragment> fragments = mFragmentManager.getFragments();
        Fragment currentFragment = fragments.get(fragments.size() - 1);

        if (currentFragment instanceof MarketFragment)
            super.onBackPressed();
        else {
            int size = fragments.size();
            int onBeforeTheLAstFragment = size - 2;
            if (size >= 2) {
                if (fragments.get(onBeforeTheLAstFragment) instanceof MarketFragment)
                    getSupportActionBar().show();
            } else
                getSupportActionBar().hide();
            mLoadingCover.setVisibility(View.INVISIBLE);
            removeCurrentFragment(currentFragment);
        }


    }

    private void removeCurrentFragment(Fragment currentFragment) {
        mFragmentManager.beginTransaction()
                .remove(currentFragment)
                .commitNow();
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
        changePage(ProductFragment.newInstance(id));
    }

    @Override
    public void showDetails() {
        changePage(DetailFragment.newInstance());

    }

    @Override
    public void showShoppingCart() {
        mFragmentManager.beginTransaction()
                .add(R.id.container_MarkerA, ShoppingCartFragment.newInstance())
                .commit();
        getSupportActionBar().hide();

    }


    private void changePage(Fragment fragment) {

        mFragmentManager.beginTransaction()
                .add(R.id.container_MarkerA, fragment)
                .commitAllowingStateLoss();


    }


    @Override
    public void showLoading() {
        if (mLoadingCover != null) {
            mLoadingCover.setVisibility(View.VISIBLE);
            KeyBoardUtils.hideKeyboard(MarketActivity.this);
            getSupportActionBar().hide();

        }

    }

    @Override
    public void hideLoading() {
        if (mLoadingCover != null) {
            mLoadingCover.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
            KeyBoardUtils.hideKeyboard(MarketActivity.this);


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.market_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_marketA:

                changePage(SearchProductFragment.newInstance());
                break;
            case R.id.shop_marketA:
                showShoppingCart();
                break;
        }
        return true;

    }


    @Override
    public void goPreviousFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.container_MarkerA);

        mFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();

    }



}
