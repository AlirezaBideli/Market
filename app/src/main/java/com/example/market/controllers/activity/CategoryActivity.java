package com.example.market.controllers.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.market.R;
import com.example.market.controllers.fragment.CategoryListFragment;
import com.example.market.controllers.fragment.ConnectionDialog;
import com.example.market.controllers.fragment.DetailFragment;
import com.example.market.controllers.fragment.OrderFragment;
import com.example.market.controllers.fragment.ParentCatFragment;
import com.example.market.controllers.fragment.ProductFragment;
import com.example.market.controllers.fragment.ProductListFragment;
import com.example.market.controllers.fragment.RegisterFragment;
import com.example.market.controllers.fragment.ShoppingCartFragment;
import com.example.market.controllers.fragment.SortDialogFragment;
import com.example.market.model.ActivityStart;
import com.example.market.model.DetailCallBack;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.OrderCalllBack;
import com.example.market.model.ProductLab;
import com.example.market.model.RegisterCallBack;
import com.example.market.model.SortType;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart
        , CategoryListFragment.CallBacks, DetailCallBack,
        ProductFragment.CallBacks, LoadingCallBack, ConnectionDialog.CallBacks,
        SortDialogFragment.CallBacks, OrderCalllBack, RegisterCallBack {

    //Argument Tags
    public static final String TAG = "CategoryActivity";
    //widgets Variables
    private Toolbar mToolbar;
    //Simple Variables
    private FrameLayout mLoadingCover;
    private ProductLab mProductLab;
    private boolean mIsDownloadable = true;
    private FragmentManager mFragmentManager;
    //AsyncTasks
    private Runnable mCategoriesRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewByIds();
        variableInit();


    }


    @Override
    public void findViewByIds() {


        mLoadingCover = findViewById(R.id.cover_CategoryA);
        mToolbar = findViewById(R.id.toolbar_CategoryA);

    }

    @Override
    public void variableInit() {
        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction()
                .replace(R.id.container_CategoryA, ParentCatFragment.newInstance())
                .commit();
        setUpNavigation();

    }

    private void setUpNavigation() {
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListeners() {


    }


    @Override
    public void gotToProductList(int subCatId) {

        Fragment fragment = ProductListFragment.newInstance(subCatId);
        changePage(fragment);
    }

    private void changePage(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_CategoryA, fragment)
                .commit();
    }

    @Override
    public void showProductDetails(int id) {


        Fragment fragment = ProductFragment.newInstance(id);
        changePage(fragment);

    }

    @Override
    public void showDetails() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_CategoryA, DetailFragment.newInstance())
                .commit();


    }

    @Override
    public void showShoppingCart() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_CategoryA, ShoppingCartFragment.newInstance())
                .commit();

    }


    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment currentFragmnet = fragments.get(fragments.size() - 1);
        if (currentFragmnet instanceof ParentCatFragment)
            super.onBackPressed();
        else {
            mLoadingCover.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .remove(currentFragmnet)
                    .commit();

        }

    }


    @Override
    public void showLoading() {
        if (mLoadingCover != null)
            mLoadingCover.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (mLoadingCover != null)
            mLoadingCover.setVisibility(View.INVISIBLE);

    }

    @Override
    public void goPreviousFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.container_CategoryA);

        mFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();

    }

    @Override
    public void sort(SortType sortType) {
        ProductListFragment currentFragmnet = (ProductListFragment) mFragmentManager.findFragmentById(R.id.container_CategoryA);
        currentFragmnet.refreshList(sortType);
    }

    @Override
    public void showOrderPage() {
        changePage(OrderFragment.newInstance());
    }

    @Override
    public void showRegisterPage() {
        changePage(RegisterFragment.newInstance());
    }
}
