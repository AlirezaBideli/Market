package com.example.market.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ProductLab {
    public static final byte mCatagoryNum = 0;
    public static final byte mSubCatagoryNum = 1;
    private static ProductLab ourInstance;
    private final Context mContext;
    //Lists
    private List<Category> mCatagories;
    private List<Category> mSubCategories;
    private List<Product> mProducts;
    private List<Product> mNewestProducts;
    private List<Product> mMVisitedProducts;
    private List<Product> mBProducts;
    private List<String> mCategoryTitles;

    //Uniques
    private Product CurrentProduct;
    private List<String> mFeaturedProductImg = new ArrayList<>();

    private ProductLab(Context context) {
        mContext = context;
        mNewestProducts = new ArrayList<>();
        mBProducts = new ArrayList<>();
        mMVisitedProducts = new ArrayList<>();
        mCategoryTitles = new ArrayList<>();
        mCatagories = new ArrayList<>();
    }

    public static ProductLab getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ProductLab(context);
        return ourInstance;
    }

    public Product getCurrentProduct() {
        return CurrentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        CurrentProduct = currentProduct;
    }


    //Products List Methods
    public List<Category> getCatagories() {
        return mCatagories;
    }

    public void setCatagories(List<Category> catagories) {
        mCatagories = catagories;
    }

    public Product getUniqueProduct(int id) {
        return CurrentProduct;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public List<String> getCategoryTitles() {


        return mCategoryTitles;
    }

    public int getParentId(int catIndex) {
        //because the first category name is Uncategorized
        return mCatagories.get(catIndex).getId();
    }

    public List<Category> getUniqueSubCategory(int catIndex) {
        int parentId = getParentId(catIndex);
        List<Category> result = new ArrayList<>();
        for (int i = 0; i < mSubCategories.size(); i++) {
            Category subCategory = mSubCategories.get(i);
            if (subCategory.getParent() == parentId)
                result.add(subCategory);
        }
        return result;
    }

    public List<Category> getCategories() {


        return mCatagories;
    }

    public List<Category> getSubCategories() {


        return mSubCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        mSubCategories = subCategories;
    }

    public List<Product> getNewestProducts() {
        return mNewestProducts;
    }

    public void setNewestProducts(List<Product> newestProducts) {
        mNewestProducts = newestProducts;
    }

    public List<Product> getMVisitedProducts() {
        return mMVisitedProducts;
    }

    public void setMVisitedProducts(List<Product> MVisitedProducts) {
        mMVisitedProducts = MVisitedProducts;
    }

    public List<Product> getBProducts() {
        return mBProducts;
    }

    public void setBProducts(List<Product> BProducts) {
        mBProducts = BProducts;
    }

    public void setFeaturedProductsImags(List<String> featuredProducts) {
        mFeaturedProductImg = featuredProducts;
    }

    public List<String> getFeaturedProductImg() {
        return mFeaturedProductImg;
    }

    //Product enums
    public enum ProductType {

        NEWEST,
        MOST_VISITED,
        BEST_SELLERS
    }


}


