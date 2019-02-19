package com.example.market.controllers.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.market.R;
import com.example.market.controllers.fragmnet.CategoryListFragment;
import com.example.market.controllers.fragmnet.DetailFragment;
import com.example.market.controllers.fragmnet.ProductFragment;
import com.example.market.controllers.fragmnet.ProductListFragment;
import com.example.market.interfaces.ActivityStart;
import com.example.market.model.Category;
import com.example.market.model.ProductLab;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart
        , CategoryListFragment.CallBacks, ProductListFragment.CallBacks, ProductFragment.CallBacks {

    public static final String TAG = "CategoryActivity";
    Handler mHandler;
    private TabLayout mTabLayout;
    private ViewPager mCatagoryPager;
    private List<String> mCatagoryTitles;
    private List<Category> mCatagories;
    private ProductLab mProductLab;
    private LottieAnimationView mLoading;
    private AsyncTask mCategoryTask = ProductLab.getInstance().getCategoryTask();

    private boolean mIsDownloadable = true;
    private Runnable mCategoriesRunable = new Runnable() {
        @Override
        public void run() {
            checkDataDownLoading(mCategoryTask);
            mHandler.postDelayed(this, 500);
        }
    };
    private boolean mBackClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        findViewByIds();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler = new Handler();
        mHandler.postDelayed(mCategoriesRunable, 500);
    }

    private void checkDataDownLoading(AsyncTask asyncTask) {
        if (mIsDownloadable) {
            if (asyncTask.getStatus() == AsyncTask.Status.FINISHED) {

                mTabLayout.setVisibility(View.VISIBLE);
                mCatagoryPager.setVisibility(View.VISIBLE);
                variableInit();
                setListeners();
                mIsDownloadable = false;

                mLoading.setVisibility(View.INVISIBLE);

            } else {

                mLoading.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void findViewByIds() {


        mTabLayout = findViewById(R.id.category_tab);
        mCatagoryPager = findViewById(R.id.pager_category_CategoryA);
        mLoading = findViewById(R.id.loading_categoryA);
    }

    @Override
    public void variableInit() {


        mProductLab = ProductLab.getInstance();
        mCatagories = mProductLab.getCategories();
        mCatagoryTitles = mProductLab.getCatagoryTitles();


    }

    @Override
    public void setListeners() {


        if (mCatagoryTitles != null) {
            mCatagoryPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return CategoryListFragment.newInstance(position);
                }

                @Override
                public int getCount() {

                    return mCatagoryTitles.size();
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return mCatagoryTitles.get(position);
                }


            });


            mTabLayout.setupWithViewPager(mCatagoryPager);


        }


    }


    @Override
    public void gotToProductList(final int subCatId) {


        mHandler.removeCallbacks(mCategoriesRunable);
        final AsyncTask productsTask = mProductLab.getCatProductsTask(subCatId);
        Fragment fragment = ProductListFragment.newInstance();
        changePage(productsTask, fragment);
    }

    private void changePage(final AsyncTask productsTask, final Fragment fragment) {
        final Runnable productsRunnable = new Runnable() {
            @Override
            public void run() {
                if (productsTask.getStatus() == AsyncTask.Status.FINISHED) {
                    mLoading.setVisibility(View.INVISIBLE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container_frame_categoryA, fragment)
                            .commitAllowingStateLoss();

                } else {
                    mLoading.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(this, 500);
                }


            }
        };


        mHandler.postDelayed(productsRunnable, 500);
        mCatagoryPager.setVisibility(View.INVISIBLE);
        mTabLayout.setVisibility(View.GONE);
    }

    @Override
    public void showProductDetails(int id) {


        AsyncTask productTask = mProductLab.getProductTask(id);
        Fragment fragment = ProductFragment.newInstance(id);
        changePage(productTask, fragment);

    }

    @Override
    public void showDetails(int id) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_frame_categoryA, DetailFragment.newInstance(id))
                .commit();


    }
/*
    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame_categoryA);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
            mBackClicked = true;
        } else {
            super.onBackPressed();
        }


    }*/
}
