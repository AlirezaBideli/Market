package com.example.market.controllers.activity;

import android.util.Log;

import com.example.market.controllers.fragment.ConnectionDialog;
import com.example.market.model.ConnectionEvent;
import com.example.market.model.NotificationEvent;
import com.example.market.utils.NetworkConnection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {


    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String DIALOG_TAG = "dialog_tag";

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.POSTING)
    public void onConnectionChanged(ConnectionEvent connectionEvent) {
        Log.i(TAG, "onConnectionChanged Called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        NetworkConnection.warnConnection(this,fragmentManager);

    }

    @Subscribe(priority = 1, threadMode = ThreadMode.POSTING)
    public void onMessageEvent(NotificationEvent notificationEvent) {
        Log.i("EventBus", "showNotificationEventBus");
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(notificationEvent.getRequestCode(), notificationEvent.getNotification());
        Log.i(TAG,"onMessageEvent Called");

    }
}
