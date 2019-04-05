package com.example.market.utils;

import android.content.Context;
import android.content.Intent;

import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.controllers.activity.SplashActivity;

public class ActivityHeper {
    public static final String EXTRA_BY_NOTIFICATION = "com.example.market.utils.calledByNotification";
    public static final String EXTRA_PRODUCT_ID = "com.example.market.utils.ProductId";

    //This class is used for Activities Intent



    //This Activity be Called by 2 way First User Click on Category in drawer menu
    //Second By User Click on Newest product Notification
    public static  Intent Intent_CategoryA(Context context,boolean isByNotification,int productId)
    {
        Intent intent=new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_BY_NOTIFICATION,isByNotification);
        intent.putExtra(EXTRA_PRODUCT_ID,productId);
        return intent;
    }


    public static  Intent Intent_MarketA(Context context)
    {
        Intent intent=new Intent(context, MarketActivity.class);
        return intent;
    }
    public static  Intent Intent_SplashA(Context context)
    {
        Intent intent=new Intent(context, SplashActivity.class);
        return intent;
    }


}
