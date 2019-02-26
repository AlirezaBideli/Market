package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.market.R;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.ActivityHeper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {


    private ProductLab mProductLab = ProductLab.getInstance();
    private int mNewPage=1;
    private int mBestPage=1;
    private int mVisitedPage=1;
    private int mDefaultPageNum=2;
    private boolean isStarted=false;
    private List<Product> mBProducts=new ArrayList<>();
    private List<Product> mMVisitedProducts=new ArrayList<>();
    private List<Product> mNewestProducts=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDownloadDatas();
    }

    private void checkDownloadDatas() {


        //getNewest>getMostVisited>getBests
        getNewest(mNewPage);
        //it means if downLoad action finished by downloading best products



    }

    private void getNewest(int newPage) {
        RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getNewestProduct(mNewPage).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result=response.body();
                    if (result!=null && mNewPage<=mDefaultPageNum) {
                        mNewestProducts.addAll(result);
                        getNewest(++mNewPage);
                    }
                    else if (mNewestProducts!=null)
                        mProductLab.setNewestProducts(mNewestProducts);
                        getMostVisited(mVisitedPage);


                    }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }

    private void getMostVisited(int visitedPage) {
        RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getMVisitedProducts(mVisitedPage).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    if (result != null && mVisitedPage<=mDefaultPageNum) {
                        mMVisitedProducts.addAll(result);
                        getBests(++mVisitedPage);
                    } else if (mMVisitedProducts != null) {
                       mProductLab.setMVisitedProducts(mMVisitedProducts);
                        getBests(mBestPage);

                    }
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }

    private void getBests(int bestPage) {
        RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getBestProducts(mBestPage).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> result = response.body();
                    if (result != null  && mBestPage<=mDefaultPageNum) {
                        mBProducts.addAll(result);
                        getBests(++mBestPage);
                    } else if (mBProducts != null) {
                        mProductLab.setBProducts(mBProducts);
                        startApp();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }

        });
    }

    private void startApp() {

        if (!isStarted) {
            isStarted=true;
            Intent intent = ActivityHeper.Intent_MarketA(SplashActivity.this);
            startActivity(intent);
            this.finish();

        }
    }


}
