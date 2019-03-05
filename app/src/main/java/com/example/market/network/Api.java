package com.example.market.network;

import com.example.market.model.Category;
import com.example.market.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

     static final String MAIN_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/";
     static final String CONSUMER_KEY = "consumer_key=ck_ce093b419f9fece59083828900a012e6ca8b029f";
     static final String CONSUMER_SECRET = "consumer_secret=cs_5a11293a4206538727858774b9d452eadbf72703";
     static final String CATEGORY_CONDITION = "parent";
     static final String PRODUCT_CATEGORY_K = "category";
     static final String TYPE_CONDITION_K="orderby";




    @GET("products/categories?"+CONSUMER_KEY+"&"+CONSUMER_SECRET+"&"+CATEGORY_CONDITION+"=0")
    Call<List<Category>> getCategories(@Query("page") int page);
    @GET("products/categories?"+CONSUMER_KEY+"&"+CONSUMER_SECRET+"&"+CATEGORY_CONDITION+"!=0")
    Call<List<Category>> getSubCategories(@Query("page") int page);
    @GET("products?"+CONSUMER_KEY+"&"+CONSUMER_SECRET)
    Call<List<Product>>getCatProducts(@Query ("category")int category,@Query("page") int page );

    @GET("products/{id}?"+CONSUMER_KEY+"&"+CONSUMER_SECRET)
    Call<Product> getProduct(@Path("id") int id);
    @GET("products?"+CONSUMER_KEY+"&"+CONSUMER_SECRET)
    Call<List<Product>> getNewestProduct(@Query("page") int page );
    @GET("products?"+CONSUMER_KEY+"&"+CONSUMER_SECRET+"&"+TYPE_CONDITION_K+"=popularity")
    Call<List<Product>> getMVisitedProducts(@Query("page") int page);
    @GET("products?"+CONSUMER_KEY+"&"+CONSUMER_SECRET+"&"+TYPE_CONDITION_K+"=rating")
    Call<List<Product>> getBestProducts(@Query("page") int page);
    @GET("products?"+CONSUMER_KEY+"&"+CONSUMER_SECRET+"&"+"featured=true")
    Call<List<Product>> getFeaturedProducts();



}
