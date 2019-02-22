package com.example.market.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {


    private static final String BASE_URL="https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
    private static Retrofit RetrofitInstance;

    public static Retrofit getRetrofitInstance() {
        if (RetrofitInstance==null) {
            RetrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RetrofitInstance;
    }
}
