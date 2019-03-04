package com.example.market.model;

import com.example.market.App;

import java.util.ArrayList;
import java.util.List;

public class ProductLab {
    public static final byte mCatagoryNum = 0;
    public static final byte mSubCatagoryNum = 1;
    private static final ProductLab ourInstance = new ProductLab();
    //Lists
    private List<Category> mCatagories;
    private List<Category> mSubCategories;
    private List<Product> mProducts;
    private List<Product> mNewestProducts;
    private List<Product> mMVisitedProducts;
    private List<Product> mBProducts;
    private List<String> mCatagoryTitles;
    private ProductDao mProductDao = (new App()).getDaoSession().getProductDao();
    //Uniques
    private Product CurrentProduct;

    private ProductLab() {

        mNewestProducts = new ArrayList<>();
        mBProducts = new ArrayList<>();
        mMVisitedProducts = new ArrayList<>();
        mCatagoryTitles = new ArrayList<>();
        mCatagories = new ArrayList<>();


    }

    public static ProductLab getInstance() {
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

    public List<String> getCatagoryTitles() {


        return mCatagoryTitles;
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


    //Products Shopping Cart Methods

    public void insertShoppingCart(Product product) {
        if (product.getImages() != null) {
            String url=product.getImages().get(0).getSrc();
            product.setFirstImgUrl(url);
        }
        mProductDao.insertOrReplace(product);
    }

    public List<Product> getShopingCartPro() {

        return mProductDao.queryBuilder().list();
    }

    public boolean checkProductExist(int apiId) {

        Product result = mProductDao.queryBuilder().
                where(ProductDao.Properties.MId.eq(apiId))
                .unique();

        return result != null;

    }

    public void deleteOrderedProduct(Product product)
    {
        mProductDao.delete(product);
    }

    //Product enums
    public enum ProductType {

        NEWEST,
        MOST_VISITED,
        BEST_SELLERS
    }


}


