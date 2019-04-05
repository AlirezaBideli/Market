package com.example.market;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.example.market.model.DaoMaster;
import com.example.market.model.DaoSession;
import com.example.market.model.NotificationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.app.NotificationManagerCompat;

public class App extends Application {

    public static final boolean isBusEvent = true;

    private static Context mContext;
    private static App app;
    private DaoSession mDaoSession;

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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


    @Subscribe(priority = 1, threadMode = ThreadMode.POSTING)
    public void onMessageEvent(NotificationEvent notificationEvent) {
        Log.i("EventBus", "showNotificationEventBus");
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(notificationEvent.getRequestCode(), notificationEvent.getNotification());
    }
}
