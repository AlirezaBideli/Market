package com.example.market.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.market.prefs.AlarmManagerPrefs;
import com.example.market.prefs.NotifyHourPrefs;
import com.example.market.services.NotifyNewestService;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Received Broadcast Intent: " + intent);

        int hour = NotifyHourPrefs.getInstance().retriveHour();
        Boolean isAlarmOn = AlarmManagerPrefs.isAlarmOn(context);
        if (isAlarmOn)
            NotifyNewestService.setServiceAlarm(context, isAlarmOn, hour);

    }
}

