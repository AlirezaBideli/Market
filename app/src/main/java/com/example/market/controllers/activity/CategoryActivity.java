package com.example.market.controllers.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.example.market.R;
import com.example.market.controllers.fragmnet.CategoryListFragment;
import com.example.market.controllers.fragmnet.DetailFragment;
import com.example.market.controllers.fragmnet.ParentCatFragment;
import com.example.market.controllers.fragmnet.ProductFragment;
import com.example.market.controllers.fragmnet.ProductListFragment;
import com.example.market.interfaces.ActivityStart;
import com.example.market.model.ProductLab;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart
        , CategoryListFragment.CallBacks, ProductListFragment.CallBacks, ProductFragment.CallBacks {

    //Argument Tags
    public static final String TAG = "CategoryActivity";
    //Simple Variables
    Handler mHandler;
    //widgets Variables
    private Toolbar mToolbar;
    //Widgets Variables
    private FrameLayout mLoadingCover;
    private ProductLab mProductLab;
    private boolean mIsDownloadable = true;
    //AsyncTasks
    private AsyncTask mCategoryTask = ProductLab.getInstance().getCategoryTask();
    private Runnable mCategoriesRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewByIds();
        variableInit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler = new Handler();
        mHandler.postDelayed(mCategoriesRunnable, 500);
    }

    private void checkDataDownLoading(AsyncTask asyncTask, Fragment fragment) {
        if (mIsDownloadable) {
            if (asyncTask.getStatus() == AsyncTask.Status.FINISHED) {


                variableInit();
                mLoadingCover.setVisibility(View.INVISIBLE);
                mIsDownloadable = false;
                changePage(asyncTask, fragment);


            } else {

                mLoadingCover.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void findViewByIds() {


        mLoadingCover = findViewById(R.id.cover_CategoryA);
        mToolbar = findViewById(R.id.toolbar_CategoryA);
    }

    @Override
    public void variableInit() {


        final Fragment fragment = ParentCatFragment.newInstance();
        mProductLab = ProductLab.getInstance();
        mCategoriesRunnable = new Runnable() {
            @Override
            public void run() {
                checkDataDownLoading(mCategoryTask, fragment);
                mHandler.postDelayed(this, 500);
            }
        };

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
    public void gotToProductList(final int subCatId) {


        mHandler.removeCallbacks(mCategoriesRunnable);
        final AsyncTask productsTask = mProductLab.getCatProductsTask(subCatId);
        Fragment fragment = ProductListFragment.newInstance();
        changePage(productsTask, fragment);
    }

    private void changePage(final AsyncTask productsTask, final Fragment fragment) {
        final Runnable productsRunnable = new Runnable() {
            @Override
            public void run() {
                if (productsTask.getStatus() == AsyncTask.Status.FINISHED) {
                    mLoadingCover.setVisibility(View.INVISIBLE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.container_CategoryA, fragment)
                            .commitAllowingStateLoss();
                    mHandler.removeCallbacks(this);

                } else {
                    mLoadingCover.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(this, 500);
                }


            }
        };


        mHandler.postDelayed(productsRunnable, 500);

    }

    @Override
    public void showProductDetails(int id) {


        Fragment fragment = ProductFragment.newInstance(id);
        AsyncTask productTask = mProductLab.getProductTask(id);
        changePage(productTask, fragment);

    }

    @Override
    public void showDetails(int id) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_CategoryA, DetailFragment.newInstance(id))
                .commit();


    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment currentFragmnet = fragments.get(fragments.size() - 1);
        if (currentFragmnet instanceof ParentCatFragment)
            super.onBackPressed();
        else
            fragmentManager.beginTransaction()
                    .remove(currentFragmnet)
                    .commit();
    }
}
