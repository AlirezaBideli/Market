package com.example.market.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("images")
    private List<Image> mImages;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("average_rating")
    private String mAverage_rating;
    @SerializedName("rating_count")
    private String mRating_count;

    public String getAverage_rating() {
        return mAverage_rating;
    }

    public String getRating_count() {
        return mRating_count;
    }

    public String getPrice() {

        return mPrice;
    }

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
