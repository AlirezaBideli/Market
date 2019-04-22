package com.example.market.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.greenrobot.greendao.annotation.Entity;

public class Customer {

    public static final int DEFAULT_CUSTOMER_ID = 0;
    @SerializedName("id")
    private int _id;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("username")
    private String mUserName;
    private String mPassword;
    @SerializedName("email")
    private String mEmail;


    public Customer(int _id, String firstName, String lastName, String userName
            , String password, String email) {
        this._id = _id;
        mFirstName = firstName;
        mLastName = lastName;
        mUserName = userName;
        mEmail = email;
        mPassword = password;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }




}
