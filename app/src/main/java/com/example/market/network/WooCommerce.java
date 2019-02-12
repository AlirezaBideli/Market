package com.example.market.network;

import android.util.Log;

import com.example.market.model.Catagory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WooCommerce {

    private static final String TAG = "NetworkClass";

    public byte[] getUrlBytes(String urlSpec) throws IOException {

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

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Catagory> getCategories() throws IOException {
        List<Catagory> catagories = new ArrayList<>();
        String url = UrlHelper.CATAGORIES_URL;
        String result = getUrlString(url);
        try {
            JSONArray jsonBody = new JSONArray(result);


            Gson gson = new Gson();

            catagories = Arrays.asList(gson.fromJson(jsonBody.toString(), Catagory[].class));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return catagories;
    }


}
