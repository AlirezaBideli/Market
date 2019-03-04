package com.example.market;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.market.model.DaoMaster;
import com.example.market.model.DaoSession;

public class App extends Application {

    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Bought_Products", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
