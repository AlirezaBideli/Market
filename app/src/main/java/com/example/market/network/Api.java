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
    static final String TYPE_CONDITION_K = "orderby";
    static final String AUTHENTICATION = CONSUMER_KEY + "&" + CONSUMER_SECRET;
    static final String PAGE_CONDITION = "";



    @GET("products/categories?" + AUTHENTICATION+ "&" + CATEGORY_CONDITION + "=0")
    Call<List<Category>> getCategories(@Query("page") int page);

    @GET("products/categories?" + AUTHENTICATION+ "&" + CATEGORY_CONDITION + "!=0")
    Call<List<Category>> getSubCategories(@Query("page") int page);

    @GET("products?" + AUTHENTICATION+"&"+PAGE_CONDITION)
    Call<List<Product>> getCatProducts(@Query("category") int category
            ,@Query("orderby")String orderby,@Query("order")String order);

    @GET("products/{id}?" + AUTHENTICATION)
    Call<Product> getProduct(@Path("id") int id);

    @GET("products?" + AUTHENTICATION+"&"+PAGE_CONDITION)
    Call<List<Product>> getNewestProduct();

    @GET("products"+AUTHENTICATION)
    Call<List<Product>> getProducts(@Query("orderby")String orderby,@Query("order")String order);

    @GET("products?" + AUTHENTICATION+ "&" + TYPE_CONDITION_K + "=popularity"+"&"+PAGE_CONDITION)
    Call<List<Product>> getMVisitedProducts();

    @GET("products?" + AUTHENTICATION+"&"+ TYPE_CONDITION_K + "=rating"+"&"+PAGE_CONDITION)
    Call<List<Product>> getBestProducts();

    @GET("products?" + AUTHENTICATION+ "&"+"featured=true")
    Call<List<Product>> getFeaturedProducts();

    @GET("products?" +AUTHENTICATION)
    Call<List<Product>> getResultProducts(@Query("search") String search);


}
