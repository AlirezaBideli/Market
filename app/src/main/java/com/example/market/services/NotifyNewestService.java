package com.example.market.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import com.example.market.prefs.AlarmManagerPrefs;
import com.example.market.utils.Services;

import java.util.concurrent.TimeUnit;


public class NotifyNewestService extends IntentService {


    private static final String TAG = "NotifyNewestServiceTag";

    public NotifyNewestService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NotifyNewestService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn, int interval) {

        long poll_interval = TimeUnit.HOURS.toMillis(interval);
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), poll_interval, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        AlarmManagerPrefs.setAlarmOn(context, isOn);
    }

    public static boolean isAlarmOn(Context context) {
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,
                0,
                i,
                PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected())
            return;

        Log.d(TAG, "received intent: " + intent);
        Services.fetchNewestProductAndNotify(TAG);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

}
