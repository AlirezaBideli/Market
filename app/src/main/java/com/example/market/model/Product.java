package com.example.market.model;

import android.net.Uri;

public class Product {

    private int mId;
    private Uri mImgPath;
    private String mName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Uri getImgPath() {
        return mImgPath;
    }

    public void setImgPath(Uri imgPath) {
        mImgPath = imgPath;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
