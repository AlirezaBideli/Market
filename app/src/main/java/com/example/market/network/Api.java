package com.example.market.network;

import com.example.market.model.Category;
import com.example.market.model.Coupon;
import com.example.market.model.Customer;
import com.example.market.model.OrderJsonBody;
import com.example.market.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    static final String PAGE_CONDITION = "per_page=30";


    @GET("products/categories?" + AUTHENTICATION + "&" + CATEGORY_CONDITION + "=0")
    Call<List<Category>> getCategories(@Query("page") int page);

    @GET("products/categories?" + AUTHENTICATION + "&" + CATEGORY_CONDITION + "!=0")
    Call<List<Category>> getSubCategories(@Query("page") int page);

    @GET("products?" + AUTHENTICATION + "&" + PAGE_CONDITION)
    Call<List<Product>> getCatProducts(@Query("category") int category
            , @Query("orderby") String orderby, @Query("order") String order);

    @GET("products/{id}?" + AUTHENTICATION)
    Call<Product> getProduct(@Path("id") int id);

    @GET("products?" + AUTHENTICATION + "&" + PAGE_CONDITION)
    Call<List<Product>> getNewestProduct();

    @GET("products" + AUTHENTICATION)
    Call<List<Product>> getProducts(@Query("orderby") String orderby, @Query("order") String order);

    @GET("products?" + AUTHENTICATION + "&" + TYPE_CONDITION_K + "=popularity" + "&" + PAGE_CONDITION)
    Call<List<Product>> getMVisitedProducts();

    @GET("products?" + AUTHENTICATION + "&" + TYPE_CONDITION_K + "=rating" + "&" + PAGE_CONDITION)
    Call<List<Product>> getBestProducts();

    @GET("products?" + AUTHENTICATION + "&" + "featured=true")
    Call<List<Product>> getFeaturedProducts();

    @GET("products?" + AUTHENTICATION)
    Call<List<Product>> getResultProducts(@Query("search") String search);

    @GET("products?" + AUTHENTICATION)
    Call<List<Product>> getOrders(@Query("include") String include);


    @POST("customers?" + AUTHENTICATION)
    @FormUrlEncoded
    Call<Customer> createCustomer(@Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("username") String username,
                                  @Field("email") String email);

    @POST("orders?" + AUTHENTICATION)
    Call<Customer> sendOrder(@Body OrderJsonBody orderjsonBody);

    @GET("products/attributes/{id}/terms?" + AUTHENTICATION)
    Call<List<Product.Terms>> getAttributeTerms(@Path("id") int id);


    @GET("coupons?"+AUTHENTICATION)
    Call<List<Coupon>> getAllCoupons();


}
