package com.example.market.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("reviewer")
    private String mReviewer;
    @SerializedName("review")
    private String mReview;
    @SerializedName("rating")
    private int mRating;

    public String getReviewer() {
        return mReviewer;
    }

    public void setReviewer(String reviewer) {
        mReviewer = reviewer;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }


}
