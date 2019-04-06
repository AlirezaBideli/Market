package com.example.market.model;

import com.google.gson.annotations.SerializedName;

public class Coupon {


    @SerializedName("code")
    private String mCode;
    @SerializedName("amount")
    private int mAmount;



    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        this.mAmount = amount;
    }


}
