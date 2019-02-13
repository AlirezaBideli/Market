package com.example.market.controllers.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.market.R;
import com.example.market.controllers.fragmnet.CategoryFragment;
import com.example.market.interfaces.ActivityStart;
import com.example.market.model.Category;
import com.example.market.model.ProductLab;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart {

    public static final String TAG = "CategoryActivity";
    Handler mHandler;
    private TabLayout mTabLayout;
    private ViewPager mCatagoryPager;
    private List<String> mCatagoryTitles;
    private List<Category> mCatagories;
    private ProductLab mProductLab;
    private LottieAnimationView mLoading;
    private AsyncTask mCategoryTask = ProductLab.getInstance().getCategoryTask();
    private boolean isDownloadable = true;
    private Runnable downLoadThread = new Runnable() {
        @Override
        public void run() {
            checkDataDownLoading(mCategoryTask);
            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mHandler = new Handler();
        mHandler.postDelayed(downLoadThread, 1000);

        findViewByIds();


    }

    private void checkDataDownLoading(AsyncTask asyncTask) {
        if (isDownloadable) {
            if (asyncTask.getStatus() == AsyncTask.Status.FINISHED) {

                mLoading.setVisibility(View.INVISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                mCatagoryPager.setVisibility(View.VISIBLE);
                variableInit();
                setListeners();
                isDownloadable = false;

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
                    return CategoryFragment.newInstance(position);
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


}
