package com.example.market.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.market.model.ConnectionEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import androidx.fragment.app.FragmentManager;


public class ConnectionReciever extends BroadcastReceiver {


    private static final String TAG = "ConnectionReceiverTag";
    private String EXTRA_FRAGMENT_MANAGER = "com.example.market.recievers.fragmentManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {

            EventBus.getDefault().post(new ConnectionEvent());
            Log.i(TAG, "Connection Receiver Called");


        }
    }
}
