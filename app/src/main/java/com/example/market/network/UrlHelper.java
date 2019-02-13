package com.example.market.network;

import android.net.Uri;

public class UrlHelper {

    //MAIN VARIABLES

    public static final  String MAIN_URL="https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
    private static final String CONSUMER_KEY_K="consumer_key";
    private static final String CONSUMER_KEY_V="ck_ce093b419f9fece59083828900a012e6ca8b029f";
    private static final String CONSUMER_SECRET_K="consumer_secret";
    private static final String CONSUMER_SECRET_V="cs_5a11293a4206538727858774b9d452eadbf72703";
    private static final String CATEGORY_CONDITION_K="parent";
    private static final String CATEGORY_CONDITION_V="0";



    //URLs


      public static final String CATEGORIES_URL= Uri.parse(MAIN_URL)
        .buildUpon()
        .appendEncodedPath("products/categories")
        .appendQueryParameter(CONSUMER_KEY_K,CONSUMER_KEY_V)
        .appendQueryParameter(CONSUMER_SECRET_K,CONSUMER_SECRET_V)
        .appendQueryParameter(CATEGORY_CONDITION_K,CATEGORY_CONDITION_V)
        .build().toString();

    public static final String ALL_SUB_CATEGORIES_URL = Uri.parse(MAIN_URL)
            .buildUpon()
            .appendEncodedPath("products/categories")
            .appendQueryParameter(CONSUMER_KEY_K,CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K,CONSUMER_SECRET_V)
            .build().toString()+"&"+ CATEGORY_CONDITION_K+"!=0";


    public static final String PRODUCTS_URL= Uri.parse(MAIN_URL)
            .buildUpon()
            .appendEncodedPath("products")
            .appendQueryParameter(CONSUMER_KEY_K,CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K,CONSUMER_SECRET_V)
            .build().toString();


    private static String getUniqueSubCatUrl(int parenId)
    {
           return Uri.parse(MAIN_URL) .buildUpon()
            .appendEncodedPath("products/categories")
            .appendQueryParameter(CONSUMER_KEY_K,CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K,CONSUMER_SECRET_V)
            .appendQueryParameter(CATEGORY_CONDITION_K,parenId+"")
            .build().toString();
    }





}
