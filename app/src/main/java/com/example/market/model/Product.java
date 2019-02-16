package com.example.market.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("images")
    private List<Image> mImages;

    public List<Image> getImages() {
        return mImages;
    }
    public int getId() {
        return mId;
    }
    public String getName() {
        return mName;
    }
}
