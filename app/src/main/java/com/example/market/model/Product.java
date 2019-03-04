package com.example.market.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;


@Entity
public class Product {


    //DataBase ID
    @Id(autoincrement = true)
    private Long _id;

    //Product Id in Api
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private String mPrice;

    @Transient
    @SerializedName("images")
    private List<Image> mImages;
    @Transient
    @SerializedName("attributes")
    private List<Attributes> mAttributes;
    @Transient
    @SerializedName("average_rating")
    private String mAverage_rating;
    @Transient
    @SerializedName("rating_count")
    private String mRating_count;
    @Transient
    @SerializedName("weight")
    private String mWeight;
    @Transient
    @SerializedName("dimensions")
    private Dimensions mDimensions;
    @Transient
    @SerializedName("description")
    private String mDescription;

    @Expose()
    @SerializedName("firstImgUrl")
    private String mFirstImgUrl;
    @Expose()
    @SerializedName("productCount")
    private int mProductCount = 1;

    @Generated(hash = 1032023407)
    public Product(Long _id, int mId, String mName, String mPrice,
                   String mFirstImgUrl, int mProductCount) {
        this._id = _id;
        this.mId = mId;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mFirstImgUrl = mFirstImgUrl;
        this.mProductCount = mProductCount;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }

    public int getProductCount() {
        return mProductCount;
    }

    public void setProductCount(int productCount) {
        mProductCount = productCount;
    }

    public String getFirstImgUrl() {
        return mFirstImgUrl;
    }

    public void setFirstImgUrl(String firstImgUrl) {
        mFirstImgUrl = firstImgUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public List<Attributes> getAttributes() {
        return mAttributes;
    }

    public String getWeight() {
        return mWeight;
    }

    public Dimensions getDimensions() {
        return mDimensions;
    }

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

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getMId() {
        return this.mId;
    }

    public void setMId(int mId) {
        this.mId = mId;
    }

    public String getMName() {
        return this.mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getMPrice() {
        return this.mPrice;
    }

    public void setMPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getMFirstImgUrl() {
        return this.mFirstImgUrl;
    }

    public void setMFirstImgUrl(String mFirstImgUrl) {
        this.mFirstImgUrl = mFirstImgUrl;
    }

    public int getMProductCount() {
        return this.mProductCount;
    }

    public void setMProductCount(int mProductCount) {
        this.mProductCount = mProductCount;
    }


    public class Dimensions {
        @SerializedName("length")
        private String mLength;
        @SerializedName("width")
        private String mWidth;
        @SerializedName("height")
        private String mHeight;

        public String getLength() {
            return mLength;
        }

        public String getWidth() {
            return mWidth;
        }

        public String getHeight() {
            return mHeight;
        }
    }

    public class Attributes {
        @SerializedName("id")
        private int mId;
        @SerializedName("name")
        private String mName;
        @SerializedName("options")
        private List<String> mOptions;

        public int getId() {
            return mId;
        }


        public String getOptions() {
            StringBuilder result = new StringBuilder();
            int size = mOptions.size();
            for (byte i = 0; i < size; i++) {
                if (i != size - 1)
                    result.append(mOptions.get(i) + ",");
                else
                    result.append(mOptions.get(i));

            }
            return result.toString();
        }

        public String getName() {
            return mName;
        }
    }


}


