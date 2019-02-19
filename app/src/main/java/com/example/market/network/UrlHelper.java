package com.example.market.network;

import android.net.Uri;

public class UrlHelper {

    //MAIN VARIABLES

    public static final String MAIN_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
    private static final String CONSUMER_KEY_K = "consumer_key";
    private static final String CONSUMER_KEY_V = "ck_ce093b419f9fece59083828900a012e6ca8b029f";
    private static final String CONSUMER_SECRET_K = "consumer_secret";
    private static final String CONSUMER_SECRET_V = "cs_5a11293a4206538727858774b9d452eadbf72703";
    private static final String CATEGORY_CONDITION_K = "parent";
    private static final String CATEGORY_CONDITION_V = "0";
    private static final String PRODUCT_CATEGORY_K = "category";
    private static final String COUNT_CONDITION_K="per_page";
    private static final String COUNT_CONDITION_V="30";





    //URLs


    //because the default order is descending
    //and default order_by is according to Date
    //we don't append order and order_by QueryParameters
    public static final String NEWEST_PRODUCTS_URL = Uri.parse(MAIN_URL)
            .buildUpon()
            .appendEncodedPath("products")
            .appendQueryParameter(CONSUMER_KEY_K, CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K, CONSUMER_SECRET_V)
            .appendQueryParameter(COUNT_CONDITION_K, COUNT_CONDITION_V)
            .build().toString();


    public static final String CATEGORIES_URL = Uri.parse(MAIN_URL)
            .buildUpon()
            .appendEncodedPath("products/categories")
            .appendQueryParameter(CONSUMER_KEY_K, CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K, CONSUMER_SECRET_V)
            .appendQueryParameter(CATEGORY_CONDITION_K, CATEGORY_CONDITION_V)
            .appendQueryParameter(COUNT_CONDITION_K, COUNT_CONDITION_V)
            .build().toString();

    public static final String ALL_SUB_CATEGORIES_URL = Uri.parse(MAIN_URL)
            .buildUpon()
            .appendEncodedPath("products/categories")
            .appendQueryParameter(CONSUMER_KEY_K, CONSUMER_KEY_V)
            .appendQueryParameter(CONSUMER_SECRET_K, CONSUMER_SECRET_V)
            .appendQueryParameter(COUNT_CONDITION_K, COUNT_CONDITION_V)
            .build().toString() + "&" + CATEGORY_CONDITION_K + "!=0";

    public static String getCatProductsURL(int subCatId) {
        return Uri.parse(MAIN_URL).buildUpon()
                .appendEncodedPath("products")
                .appendQueryParameter(CONSUMER_KEY_K, CONSUMER_KEY_V)
                .appendQueryParameter(CONSUMER_SECRET_K, CONSUMER_SECRET_V)
                .appendQueryParameter(PRODUCT_CATEGORY_K, subCatId + "")
                .build().toString();
    }


    public static String getProductURL(int id) {
        return Uri.parse(MAIN_URL).buildUpon()
                .appendEncodedPath("products/"+id)
                .appendQueryParameter(CONSUMER_KEY_K, CONSUMER_KEY_V)
                .appendQueryParameter(CONSUMER_SECRET_K, CONSUMER_SECRET_V)
                .build().toString();
    }



}
