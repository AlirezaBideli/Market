package com.example.market.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Order {
    @Id
    @SerializedName("product_id")
    private Long _id;
    @SerializedName("quantity")
    private int mCount;

    @Generated(hash = 1477403839)
    public Order(Long _id, int mCount) {
        this._id = _id;
        this.mCount = mCount;
    }
    @Generated(hash = 1105174599)
    public Order() {
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getMCount() {
        return this.mCount;
    }

    public void setMCount(int mCount) {
        this.mCount = mCount;
    }

}
