package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.fragment.CategoryListFragment;
import com.example.market.controllers.fragment.ConnectionDialog;
import com.example.market.controllers.fragment.CreateReviewFragment;
import com.example.market.controllers.fragment.DetailFragment;
import com.example.market.controllers.fragment.FilterFragment;
import com.example.market.controllers.fragment.OrderFragment;
import com.example.market.controllers.fragment.ParentCatFragment;
import com.example.market.controllers.fragment.ProductFragment;
import com.example.market.controllers.fragment.ProductListFragment;
import com.example.market.controllers.fragment.RegisterFragment;
import com.example.market.controllers.fragment.ReviewListFragment;
import com.example.market.controllers.fragment.ShoppingCartFragment;
import com.example.market.controllers.fragment.SortDialogFragment;
import com.example.market.model.ActivityStart;
import com.example.market.model.DetailCallBack;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.OrderCallBack;
import com.example.market.model.RegisterCallBack;
import com.example.market.model.SortType;
import com.example.market.model.repositories.ProductLab;
import com.example.market.utils.ActivityHeper;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart
        , CategoryListFragment.CallBacks, DetailCallBack,
        ProductFragment.CallBacks, LoadingCallBack, ConnectionDialog.CallBacks,
        SortDialogFragment.CallBacks, OrderCallBack, RegisterCallBack,
        ProductListFragment.CallBacks, FilterFragment.CallBacks,
        OrderFragment.CallBacks ,ReviewListFragment.CallBacks{

    //Argument Tags
    public static final String TAG = "CategoryActivity";
    //widgets Variables
    private Toolbar mToolbar;
    private TextView mTxtSortDesc;
    //Simple Variables
    private FrameLayout mLoadingCover;
    private ProductLab mProductLab;
    private boolean mIsDownloadable = true;
    private FragmentManager mFragmentManager;
    public static boolean mCalledByNotification;


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
        mTxtSortDesc = findViewById(R.id.txt_sort_desc);

    }

    @Override
    public void variableInit() {
        mFragmentManager = getSupportFragmentManager();

        initialFirstFragment();
        setUpNavigation();

    }

    private void initialFirstFragment() {
        Intent intent=getIntent();
        boolean isCalledByNotification=getIntent()
                .getBooleanExtra(ActivityHeper.EXTRA_BY_NOTIFICATION,false);
        int productId=getIntent()
                .getIntExtra(ActivityHeper.EXTRA_PRODUCT_ID,0);

        if (isCalledByNotification)
            changePage(ProductFragment.newInstance(productId));
        else
            changePage(ParentCatFragment.newInstance());
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
                .add(R.id.container_CategoryA, ShoppingCartFragment.newInstance())
                .commit();

    }

    @Override
    public void showReviews(int productId) {
        changePage(ReviewListFragment.newInstance(productId));
    }


    @Override
    public void onBackPressed() {

        if (mCalledByNotification)
            super.onBackPressed();

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

        mTxtSortDesc = findViewById(R.id.txt_sort_desc);

        switch (sortType) {
            case BEST_SELLERS:
                mTxtSortDesc.setText(R.string.best_sellers);
                break;
            case PRICE_ASC:
                mTxtSortDesc.setText(R.string.price_asc);
                break;
            case NEWEST:
                mTxtSortDesc.setText(R.string.newest);
                break;
            case PRICE_DESC:
                mTxtSortDesc.setText(R.string.price_desc);
                break;
        }
        ProductListFragment currentFragmnet =
                (ProductListFragment) mFragmentManager.findFragmentById(R.id.container_CategoryA);
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

    @Override
    public void showFilterPage() {
        changePage(new FilterFragment());
    }

    @Override
    public void showProductList(FilterFragment filterFragment) {
        mFragmentManager.beginTransaction().remove(filterFragment).commit();

    }


    @Override
    public void goToShoppingCart() {
        int fragmentExictedsize = mFragmentManager.getFragments().size();
        OrderFragment currentFragment = (OrderFragment) mFragmentManager.getFragments()
                .get(fragmentExictedsize - 1);
        ShoppingCartFragment previousFragment = (ShoppingCartFragment) mFragmentManager.getFragments()
                .get(fragmentExictedsize - 2);
        mFragmentManager.beginTransaction()
                .remove(currentFragment)
                .commit();
        mFragmentManager.beginTransaction()
                .detach(previousFragment)
                .attach(previousFragment)
                .commit();

    }


    @Override
    public void showCreateReviewFragment() {
        changePage(CreateReviewFragment.newInstance());
    }
}
