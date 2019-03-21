package com.example.market.model;

import com.google.gson.annotations.SerializedName;

public class Billing {


    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("address_1")
    private String mAddress_1;
    @SerializedName("city")
    private String mCity;
    @SerializedName("postcode")
    private String mPostCode;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("phone")
    private String mPhone;


    public Billing(int customerId,String firstName, String lastName, String address_1,
                   String city, String postCode, String country,String phone) {
        mFirstName = firstName;
        mLastName = lastName;
        mAddress_1 = address_1;
        mCity = city;
        mPostCode = postCode;
        mCountry = country;
        mPhone=phone;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getAddress_1() {
        return mAddress_1;
    }

    public void setAddress_1(String address_1) {
        mAddress_1 = address_1;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getPostCode() {
        return mPostCode;
    }

    public void setPostCode(String postCode) {
        mPostCode = postCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }


}
