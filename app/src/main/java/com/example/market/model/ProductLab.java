package com.example.market.model;

import android.os.AsyncTask;

import com.example.market.network.WooCommerce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductLab {
    public static final byte mCatagoryNum = 0;
    public static final byte mSubCatagoryNum = 1;
    private static final ProductLab ourInstance = new ProductLab();
    private final AsyncTask mCategoryTask = new CatagoryTask().execute();


    //AsyncTasks
    private AsyncTask mCatProductTask;
    private AsyncTask mProductsTask;
    private AsyncTask mProductTask;
    //Lists
    private List<Category> mCatagories;
    private List<Category> mSubCategories;
    private List<Product> mProducts;
    private List<Product> mNewestProducts;//newest products list
    private List<Product> mMVisitedProducts;//most visited products list
    private List<Product> mBProducts;//best products list
    private List<String> mCatagoryTitles;

    //Uniques
    private Product mProduct;

    private ProductLab() {

        mCatagoryTitles = new ArrayList<>();
        mCatagories = new ArrayList<>();

    }

    public static ProductLab getInstance() {
        return ourInstance;
    }

    public AsyncTask getProductTask(int id) {
        mProductTask=new ProductTask().execute(id);
        return mProductTask;
    }

    public Product getUniqueProduct(int id) {
        return mProduct;
    }

    public AsyncTask getProductsTask() {
        mProductsTask = new ProductsTask().execute();
        return mProductsTask;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public AsyncTask getCategoryTask() {
        return mCategoryTask;
    }

    public AsyncTask getCatProductsTask(int subCatId) {


        mCatProductTask = new CatProductsTask().execute(subCatId);
        return mCatProductTask;
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

    public List<Product> getNewestProducts() {
        return mNewestProducts;
    }

    public List<Product> getMVisitedProducts() {
        mMVisitedProducts = new ArrayList<>();
        mMVisitedProducts.addAll(mNewestProducts);

        Collections.sort(mMVisitedProducts, new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                int count1 = Integer.parseInt(product.getRating_count());
                int count2 = Integer.parseInt(t1.getRating_count());
                if (count1 > count2)
                    return 1;
                else
                    return 0;

            }
        });

        return mMVisitedProducts;
    }

    public List<Product> getBProducts() {
        mBProducts = new ArrayList<>();
        mBProducts.addAll(mNewestProducts);
        Collections.sort(mBProducts, new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                float count1 = Float.parseFloat(product.getAverage_rating());
                float count2 = Float.parseFloat(t1.getAverage_rating());
                if (count1 > count2)
                    return 1;
                else
                    return 0;

            }
        });
        return mBProducts;
    }

    public enum ProductType {

        NEWEST,
        MOST_VISITED,
        BEST_SELLERS
    }


    private class ProductsTask extends AsyncTask<Void, Void, Void> {

        private List<Product> newestProducts;//newest products list
        private List<Product> mVisitedProducts;//most visited products list
        private List<Product> bProducts;//best products list

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                newestProducts = WooCommerce.getNewstProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mNewestProducts = newestProducts;
        }

    }

    private class ProductTask extends AsyncTask<Integer, Void, Product> {

        @Override
        protected Product doInBackground(Integer... integers) {
            int id = integers[0];
            Product product = null;
            try {
                product = WooCommerce.getUniqueProducts(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return product;
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            mProduct = product;
        }
    }

    private class CatProductsTask extends AsyncTask<Integer, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Integer... integers) {
            int subCatId = integers[0];
            List<Product> products = new ArrayList<>();
            try {
                products = WooCommerce.getCatProducts(subCatId);
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


