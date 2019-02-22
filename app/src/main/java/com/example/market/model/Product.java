package com.example.market.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product  implements Serializable{

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
    @SerializedName("weight")
    private String mWeight;
    @SerializedName("dimensions")
    private Dimenstions mDimensions;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("attributes")
    private List<Attributes> mAttributes;

    public String getDescription() {
        return mDescription;
    }

    public List<Attributes> getAttributes() {
        return mAttributes;
    }

    public String getWeight() {
        return mWeight;
    }

    public Dimenstions getDimensions() {
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

    public class Dimenstions {
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


