package com.example.market.utils;

import android.content.Context;
import android.content.Intent;

import com.example.market.controllers.activity.CategoryActivity;

public class ActivityHeper {

    //This class is used for Activities Intent

    public static  Intent Intent_CategoryA(Context context)
    {
        Intent intent=new Intent(context, CategoryActivity.class);
        return intent;
    }
}
