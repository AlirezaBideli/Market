package com.example.market.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.market.App;
import com.example.market.R;
import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.model.NotificationEvent;
import com.example.market.model.Product;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.prefs.AlarmManagerPrefs;
import com.example.market.prefs.NotificationPrefs;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Services {

    public static final boolean IS_BY_NOTIFICATION = true;
    private static final String ACTION_SHOW_NOTIFICATION = "com.example.photogallery.SHOW_NOTIFICATION";
    private static final String EXTRA_REQUEST_CODE = "REQUEST_CODE";
    private static final String EXTRA_NOTIFICATION = "NOTIFICATION";


    public static void fetchNewestProductAndNotify(final String TAG) {
        RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getNewestProduct()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            Product product = response.body().get(0);
                            int newProductId = product.getId();
                            Log.i(TAG, newProductId + " ");

                            pollServerAndSendNotification(TAG, newProductId);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {


                    }
                });


    }

    public static void pollServerAndSendNotification(String TAG, int newProductId) {
        Context context = App.getmContext();
        int lastId = Integer.parseInt(AlarmManagerPrefs.getLastId(context));

        if (newProductId == lastId)
            //old result
            Log.d(TAG, "old result");
        else {
            //new result
            Log.d(TAG, "new result");
            Log.d(TAG, "last id: " + lastId);
            Log.d(TAG, "id: " + newProductId);
            sendNotification(newProductId);
            AlarmManagerPrefs.setLastId(context, newProductId + "");
        }



    }

    private static void sendNotification(int newProductId) {
        Context context = App.getmContext();
        NotificationPrefs notificationPrefs = NotificationPrefs.getInstance();
        int newReqCode = notificationPrefs.getRequestCode() + 1;

        Intent i = ActivityHeper.Intent_CategoryA(context, IS_BY_NOTIFICATION, newProductId);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        String channelId = context.getString(R.string.channel_id);
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(context.getString(R.string.new_product_picture))
                .setContentText(context.getString(R.string.new_product_text))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(EXTRA_REQUEST_CODE, newReqCode);
        intent.putExtra(EXTRA_NOTIFICATION, notification);
        CategoryActivity.mCalledByNotification = true;
        EventBus.getDefault().post(new NotificationEvent(newReqCode, notification));
        notificationPrefs.storeRequestCode(newReqCode);
    }

}
