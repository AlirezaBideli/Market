package com.example.market;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.market.model.DaoMaster;
import com.example.market.model.DaoSession;

public class App extends Application {

    private DaoSession mDaoSession;
    private  static  App app;

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public static App getApp()
    {
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Orders_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        app=this;
    }





}
