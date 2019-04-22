package com.example.market;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.example.market.model.DaoMaster;
import com.example.market.model.DaoSession;
import com.example.market.recievers.ConnectionReciever;

import org.greenrobot.eventbus.EventBus;

public class App extends Application {

    public static final boolean isBusEvent = true;
    private static final String DIALOG_TAG = "warn_connection_dialog_tag";
    private static final String TAG = "AppTag";

    private static Context mContext;
    private static App app;
    private DaoSession mDaoSession;
    private BroadcastReceiver mConnectionReciever;
    private IntentFilter mIntentFilter;

    public static Context getmContext() {
        return mContext;
    }

    public static App getApp() {
        return app;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Orders_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mContext=getApplicationContext();
        mDaoSession = daoMaster.newSession();
        app = this;
        createNotificationChannel();

        registerRecievers();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mConnectionReciever);
        EventBus.getDefault().unregister(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.channel_id);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);





        }

    }

    private void registerRecievers() {
        mConnectionReciever=new ConnectionReciever();
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mConnectionReciever,mIntentFilter);
    }









}
