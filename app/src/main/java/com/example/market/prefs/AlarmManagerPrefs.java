package com.example.market.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlarmManagerPrefs {
    private static final String PREF_LAST_ID = "lastId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";


    public static void setLastId(Context context, String lastId) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences
                .edit()
                .putString(PREF_LAST_ID, lastId)
                .apply();
    }

    public static String getLastId(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(PREF_LAST_ID, "0");
    }

    public static void setAlarmOn(Context context, boolean isAlarmOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isAlarmOn)
                .apply();
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }
}
