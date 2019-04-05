package com.example.market.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.market.App;

public class NotifyHourPrefs {

    private static final String Key_HOUR = "key_hour";
    private SharedPreferences mHourPreferences;
    private static final NotifyHourPrefs ourInstance = new NotifyHourPrefs();

    public static NotifyHourPrefs getInstance() {
        return ourInstance;
    }

    private NotifyHourPrefs() {
        Context context= App.getmContext();
        mHourPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void storeHour(int hour)
    {
        mHourPreferences.edit()
                .putInt(Key_HOUR,hour)
                .apply();
    }

    public int retriveHour()
    {
       return mHourPreferences.
                getInt(Key_HOUR,0);

    }

}
