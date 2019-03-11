package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.market.R;
import com.example.market.controllers.fragment.ConnectionDialog;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.ActivityHeper;
import com.example.market.utils.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity implements ConnectionDialog.CallBacks {


    private static byte mSuccessCounter = 0;
    FragmentManager mFragmentManager;
    private ProductLab mProductLab ;
    private int mDefaultSuccessCount = 4;
    private boolean isStarted = false;
    private List<Product> mBProducts = new ArrayList<>();
    private List<Product> mMVisitedProducts = new ArrayList<>();
    private List<Product> mNewestProducts = new ArrayList<>();
    private Call mBestPCall;
    private Call mMostVisitedPCall;
    private Call mNewestPCall;
    private Call<List<Product>> mFeaturedProductCall;
    private static final String TAG="SplashActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFragmentManager = getSupportFragmentManager();
        mProductLab=ProductLab.getInstance(SplashActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDownloadDatas();

    }

    private void checkDownloadDatas() {


        getFeaturedProducts();
        getNewest();
        getMostVisited();
        getBests();

        //it means if downLoad action finished by downloading best products
    }

    private void getFeaturedProducts() {
        mFeaturedProductCall = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class).getFeaturedProducts();
        mFeaturedProductCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    int size = products.size();
                    List<String> featuredProducts = new ArrayList<>();
                    getProductImages(products, size, featuredProducts);
                    mProductLab.setFeaturedProductsImags(featuredProducts);
                    mSuccessCounter++;
                    checkSuccessfulDownload();
                    Log.i(TAG,"Featured DownLoaded");


                } else {
                    if (SplashActivity.this != null)
                        NetworkConnection.warnConnection(SplashActivity.this, mFragmentManager);
                    Log.i(TAG,"Featured: "+response.errorBody()+"");

                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (SplashActivity.this != null)
                    NetworkConnection.warnConnection(SplashActivity.this, mFragmentManager);
                Log.i(TAG,"Featured Failed: "+t.toString());
            }
        });
    }

    private void getProductImages(List<Product> products, int size, List<String> featuredProducts) {
        for (byte i = 0; i < size; i++) {
            String img = products.get(i).getImages().get(0).getSrc();
            featuredProducts.add(img);
        }
    }

    private void getNewest() {
        mNewestPCall = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getNewestProduct();
        mNewestPCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    mNewestProducts.addAll(result);
                    mProductLab.setNewestProducts(mNewestProducts);
                    mSuccessCounter++;
                    checkSuccessfulDownload();
                    Log.i(TAG,"Newest DownLoaded");

                } else {
                    if (SplashActivity.this != null)
                        NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());

                    Log.i(TAG,"Newest: "+response.errorBody()+"");

                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (SplashActivity.this != null) {
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());
                }
                Log.i(TAG,"Newest Failed: "+t.toString());
            }
        });
    }

    private void getMostVisited() {

        mMostVisitedPCall = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getMVisitedProducts();

        mMostVisitedPCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    mMVisitedProducts.addAll(result);
                    mProductLab.setMVisitedProducts(mMVisitedProducts);
                    mSuccessCounter++;
                    checkSuccessfulDownload();
                    Log.i(TAG,"Most Visited DownLoaded");


                } else if (SplashActivity.this != null)
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());

                Log.i(TAG,"Most Visited: "+response.errorBody()+"");

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                if (SplashActivity.this != null)
                    NetworkConnection.warnConnection
                            (SplashActivity.this, getSupportFragmentManager());
                Log.i(TAG,"Most Visited Failed: "+t.toString());
            }
        });
    }

    private void getBests() {
        mBestPCall = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getBestProducts();

        mBestPCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    mBProducts.addAll(result);
                    mProductLab.setBProducts(mBProducts);
                    mSuccessCounter++;
                    checkSuccessfulDownload();
                    Log.i(TAG,"Best Sellers DownLoaded");
                } else {
                    if (SplashActivity.this != null)
                        NetworkConnection.warnConnection
                                (SplashActivity.this, getSupportFragmentManager());
                    Log.i(TAG,"Best Sellers: "+response.errorBody()+"");

                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (SplashActivity.this != null)
                    NetworkConnection.warnConnection
                        (SplashActivity.this, getSupportFragmentManager());
                   Log.i(TAG,"Best Sellers Failed: "+t.toString());

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
        if (mMostVisitedPCall != null)
            mMostVisitedPCall.cancel();
        if (mNewestPCall != null)
            mNewestPCall.cancel();
        if (mBestPCall != null)
            mBestPCall.cancel();
        if (mFeaturedProductCall != null)
            mFeaturedProductCall.cancel();

    }


    private void checkSuccessfulDownload() {

        if (mSuccessCounter >= mDefaultSuccessCount)
            startApp();
    }


    @Override
    public void goPreviousFragment() {
        Intent intent = ActivityHeper.Intent_SplashA(this);
        startActivity(intent);
    }
}
