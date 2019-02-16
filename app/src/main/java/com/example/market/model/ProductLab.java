package com.example.market.model;

import android.os.AsyncTask;

import com.example.market.network.WooCommerce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductLab {
    public static final byte mCatagoryNum = 0;
    public static final byte mSubCatagoryNum = 1;
    private static final ProductLab ourInstance = new ProductLab();
    private final AsyncTask mCategoryTask = new CatagoryTask().execute();
    private AsyncTask mProductTask;
    private List<Category> mCatagories;
    private List<Category> mSubCategories;
    private List<Product> mProducts;
    private List<String> mCatagoryTitles;

    private ProductLab() {

        mCatagoryTitles = new ArrayList<>();
        mCatagories = new ArrayList<>();

    }

    public static ProductLab getInstance() {
        return ourInstance;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public AsyncTask getCategoryTask() {
        return mCategoryTask;
    }

    public AsyncTask getProductsTask(int subCatId) {


        mProductTask = new ProductTask().execute(subCatId);
        return mProductTask;
    }

    public List<String> getCatagoryTitles() {


        return mCatagoryTitles;
    }

    public int getParentId(int catIndex) {
        //because the first category name is Uncategorized
        return mCatagories.get(catIndex).getId();
    }

    public List<Category> getUniqueSubCategory(int parentId) {
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

    private class ProductTask extends AsyncTask<Integer, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Integer... integers) {
            int subCatId = integers[0];
            List<Product> products = new ArrayList<>();
            try {
                products = WooCommerce.getProducts(subCatId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }


        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
            mProducts = products;

        }
    }

    private class CatagoryTask extends AsyncTask<Void, List<Category>, List<Category>> {

        @Override
        protected List<Category> doInBackground(Void... voids) {

            List<Category> catagories = new ArrayList<>();
            List<Category> sub_catagories = new ArrayList<>();

            try {
                sub_catagories = WooCommerce.getSubCategories();
                publishProgress(sub_catagories);
                catagories = WooCommerce.getCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return catagories;
        }

        @Override
        protected void onProgressUpdate(List<Category>... values) {
            super.onProgressUpdate(values);
            mSubCategories = values[0];
        }

        @Override
        protected void onPostExecute(List<Category> catagories) {
            super.onPostExecute(catagories);

            String name;
            for (int i = 0; i < catagories.size(); i++) {
                name = catagories.get(i).getName();
                mCatagoryTitles.add(name);
            }
            mCatagories = catagories;


        }
    }

}
