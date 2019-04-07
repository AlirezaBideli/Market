package com.example.market.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Product {


    //Product Id in Api
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("images")
    private List<Image> mImages;
    @SerializedName("attributes")
    private List<Attributes> mAttributes;
    @SerializedName("average_rating")
    private String mAverage_rating;
    @SerializedName("rating_count")
    private String mRating_count;
    @SerializedName("weight")
    private String mWeight;
    @SerializedName("dimensions")
    private Dimensions mDimensions;
    @SerializedName("description")
    private String mDescription;

    @Expose()
    @SerializedName("firstImgUrl")
    private String mFirstImgUrl;



    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public void setImages(List<Image> images) {
        mImages = images;
    }

    public List<Attributes> getAttributes() {
        return mAttributes;
    }

    public void setAttributes(List<Attributes> attributes) {
        mAttributes = attributes;
    }

    public String getAverage_rating() {
        return mAverage_rating;
    }

    public void setAverage_rating(String average_rating) {
        mAverage_rating = average_rating;
    }

    public String getRating_count() {
        return mRating_count;
    }

    public void setRating_count(String rating_count) {
        mRating_count = rating_count;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
        mWeight = weight;
    }

    public Dimensions getDimensions() {
        return mDimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        mDimensions = dimensions;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getFirstImgUrl() {
        return mFirstImgUrl;
    }

    public void setFirstImgUrl(String firstImgUrl) {
        mFirstImgUrl = firstImgUrl;
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

    public class Terms {
        @SerializedName("name")
        private String mName;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }
    }


}


