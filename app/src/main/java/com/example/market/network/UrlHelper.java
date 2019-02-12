package com.example.market.network;

public class UrlHelper {

    //MAIN VARIABLES
    public static final  String MAIN_URL="https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
    private static final String CONSUMER_KEY="consumer_key=ck_ce093b419f9fece59083828900a012e6ca8b029f";
    private static final String CONSUMER_SECRET="consumer_secret=cs_5a11293a4206538727858774b9d452eadbf72703";


    //URLs
    public static final String CATAGORIES_URL=
            MAIN_URL+"products/categories?" +CONSUMER_KEY+"&"+CONSUMER_SECRET+"&parent=0";


    public static final String PRODUCTS_URL=
            MAIN_URL+"products?" + CONSUMER_KEY+"&"+CONSUMER_SECRET;





}
