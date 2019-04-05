package com.example.market.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.market.App;

public class NotificationPrefs {

    private static final String KEY_REQUEST_CODE = "request_code";
    private static final NotificationPrefs ourInstance = new NotificationPrefs();
    private SharedPreferences mSharedPreferences;

    private NotificationPrefs() {
        Context context= App.getmContext();
        mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static NotificationPrefs getInstance() {
        return ourInstance;
    }

    public void storeRequestCode(int requestCode) {
        mSharedPreferences.edit()
                .putInt(KEY_REQUEST_CODE, requestCode)
                .apply();
    }
    public int getRequestCode()
    {
        return  mSharedPreferences.getInt(KEY_REQUEST_CODE,0);
    }
}
