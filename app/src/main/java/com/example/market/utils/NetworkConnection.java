package com.example.market.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.market.controllers.fragmnet.ConnectionDialog;

import androidx.fragment.app.FragmentManager;

public class NetworkConnection {

    private static final String DIALOG_TAG = "dialog_tag";

    public static void warnConnection(Context context, FragmentManager fragmentManager) {
        boolean isConnected = checkConnection(context);

        if (!isConnected) {
            ConnectionDialog connectionDialog = ConnectionDialog.newInstance();
            connectionDialog.show(fragmentManager,DIALOG_TAG);
        }

    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
