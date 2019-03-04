package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.market.R;
import com.example.market.controllers.fragmnet.ConnectionDialog;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.ActivityHeper;
import com.example.market.utils.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity implements ConnectionDialog.CallBacks {


    private ProductLab mProductLab = ProductLab.getInstance();
    private int mNewPage = 1;
    private int mBestPage = 1;
    private int mVisitedPage = 1;
    private int mDefaultPageNum = 2;
    private boolean isStarted = false;
    private List<Product> mBProducts = new ArrayList<>();
    private List<Product> mMVisitedProducts = new ArrayList<>();
    private List<Product> mNewestProducts = new ArrayList<>();
    private Call mBestPRetrofit;
    private Call mMostViitedPRetrofit;
    private Call mNewestPRetrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDownloadDatas();
    }

    private void checkDownloadDatas() {


        //getNewest>getMostVisited>getBests
        getNewest(mNewPage);
        //it means if downLoad action finished by downloading best products
    }

    private void getNewest(int newPage) {
        mNewestPRetrofit = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getNewestProduct(mNewPage);
        mNewestPRetrofit.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    if (result != null && mNewPage <= mDefaultPageNum) {
                        mNewestProducts.addAll(result);
                        getNewest(++mNewPage);
                    } else if (mNewestProducts != null)
                        mProductLab.setNewestProducts(mNewestProducts);
                    getMostVisited(mVisitedPage);


                } else {
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());

                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NetworkConnection.warnConnection
                        (SplashActivity.this, getSupportFragmentManager());
            }
        });
    }

    private void getMostVisited(int visitedPage) {

        mMostViitedPRetrofit = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getMVisitedProducts(mVisitedPage);

        mMostViitedPRetrofit.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    if (result != null && mVisitedPage <= mDefaultPageNum) {
                        mMVisitedProducts.addAll(result);
                        getBests(++mVisitedPage);
                    } else if (mMVisitedProducts != null) {
                        mProductLab.setMVisitedProducts(mMVisitedProducts);
                        getBests(mBestPage);

                    }
                } else
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                NetworkConnection.warnConnection
                        (SplashActivity.this, getSupportFragmentManager());
            }
        });
    }

    private void getBests(int bestPage) {
        mBestPRetrofit = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getBestProducts(mBestPage);

        mBestPRetrofit.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    if (result != null && mBestPage <= mDefaultPageNum) {
                        mBProducts.addAll(result);
                        getBests(++mBestPage);
                    } else if (mBProducts != null) {
                        mProductLab.setBProducts(mBProducts);
                        startApp();
                    }
                } else {
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NetworkConnection.warnConnection
                        (SplashActivity.this, getSupportFragmentManager());
            }

        });
    }

    private void startApp() {

        if (!isStarted) {
            isStarted = true;
            Intent intent = ActivityHeper.Intent_MarketA(SplashActivity.this);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMostViitedPRetrofit != null)
            mMostViitedPRetrofit.cancel();
        if (mNewestPRetrofit != null)
            mNewestPRetrofit.cancel();
        if (mBestPRetrofit != null)
            mBestPRetrofit.cancel();
    }

    @Override
    public void goPreviousFragment() {
        Intent intent = ActivityHeper.Intent_SplashA(this);
        startActivity(intent);
    }
}
