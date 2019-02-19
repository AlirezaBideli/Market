package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.example.market.R;
import com.example.market.interfaces.ActivityStart;
import com.example.market.model.ProductLab;
import com.example.market.utils.ActivityHeper;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity implements ActivityStart {


    private AsyncTask mAsyncTask;
    private boolean mIsFinished = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAsyncTask = ProductLab.getInstance()
                .getProductsTask();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkDownloadDatas();
                if (!mIsFinished)
                    handler.postDelayed(this, 1000);
                else
                    handler.removeCallbacks(this);
            }
        }, 1000);

    }


    private void checkDownloadDatas() {
        if (mAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {

            this.finish();
            startApp();
            mIsFinished = true;
        }

    }

    private void startApp() {
        Intent intent = ActivityHeper.Intent_MarketA(SplashActivity.this);
        startActivity(intent);
    }


    @Override
    public void findViewByIds() {
    }

    @Override
    public void variableInit() {

    }

    @Override
    public void setListeners() {

    }


}
