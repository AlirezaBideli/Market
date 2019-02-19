package com.example.market.network;

import android.util.Log;

import com.example.market.model.Category;
import com.example.market.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WooCommerce {

    private static final String TAG = "NetworkClass";

    public static byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {


            InputStream inputStream = httpURLConnection.getInputStream();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage() + " with: " + urlSpec);
            }


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readSize = 0;

            while ((readSize = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readSize);
            }

            outputStream.close();
            return outputStream.toByteArray();

        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: ", e);
        } finally {
            httpURLConnection.disconnect();
        }
        return null;
    }

    public static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public static List<Category> getCategories() throws IOException {
        List<Category> catagories = new ArrayList<>();
        String url = UrlHelper.CATEGORIES_URL;
        String result = getUrlString(url);
        try {
            JSONArray jsonBody = new JSONArray(result);


            Gson gson = new Gson();

            catagories = Arrays.asList(gson.fromJson(jsonBody.toString(), Category[].class));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return catagories;
    }

    public static List<Category> getSubCategories() throws IOException {
        List<Category> catagories = new ArrayList<>();
        String url = UrlHelper.ALL_SUB_CATEGORIES_URL;
        String result = getUrlString(url);
        try {
            JSONArray jsonBody = new JSONArray(result);


            Gson gson = new Gson();

            catagories = Arrays.asList(gson.fromJson(jsonBody.toString(), Category[].class));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return catagories;
    }

    public static List<Product> getCatProducts(int subCatId) throws IOException {
        List<Product> productList = new ArrayList<>();
        String url = UrlHelper.getCatProductsURL(subCatId);
        String result = getUrlString(url);
        try {
            JSONArray jsonBody = new JSONArray(result);
            Gson gson = new Gson();
            productList = Arrays.asList(gson.fromJson(jsonBody.toString(), Product[].class));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static List<Product> getNewstProducts() throws IOException {
        List<Product> newestProducts = new ArrayList<>();
        String url = UrlHelper.NEWEST_PRODUCTS_URL;
        String result = getUrlString(url);
        try {
            JSONArray jsonBody = new JSONArray(result);
            Gson gson = new Gson();
            newestProducts = Arrays.asList(gson.fromJson(jsonBody.toString(), Product[].class));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newestProducts;
    }

    public static Product getUniqueProducts(int id) throws IOException {

        Product product=null;
        String url=UrlHelper.getProductURL(id);
        String result=getUrlString(url);
        try {
            JSONObject jsonBody=new JSONObject(result);
            Gson gson=new Gson();
            product=gson.fromJson(jsonBody.toString(),Product.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return product;
    }


}
