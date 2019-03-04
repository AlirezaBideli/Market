package com.example.market.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {


    private static final String BASE_URL="https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
    private static Retrofit RetrofitInstance;

    public static Retrofit getRetrofitInstance() {

        if (RetrofitInstance==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            RetrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return RetrofitInstance;
    }
}
