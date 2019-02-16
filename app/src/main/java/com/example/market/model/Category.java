package com.example.market.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    private int mId;
    @SerializedName("parent")
    private int mParent;
    @SerializedName("name")
    private String mName;
    @SerializedName("image")
    private Image mImage;

    public Image getImage() {
        return mImage;
    }



    public int getParent() {
        return mParent;
    }





    public int getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }



}


