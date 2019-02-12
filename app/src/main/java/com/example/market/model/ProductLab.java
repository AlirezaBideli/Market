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
    private List<Catagory> mCatagories;
    private List<Catagory> mSubCategories;
    private List<String> mCatagoryTitles;
    private ProductLab() {

        mCatagoryTitles = new ArrayList<>();
        mCatagories = new ArrayList<>();

    }

    public static ProductLab getInstance() {
        return ourInstance;
    }



    public AsyncTask getCategoryTask() {
        return mCategoryTask;
    }

    public List<String> getCatagoryTitles() {
        return mCatagoryTitles;
    }

    public List<Catagory> getCategories() {


        return mCatagories;
    }

    public List<Catagory> getSubCategories() {

        return mSubCategories;
    }


    private class CatagoryTask extends AsyncTask<Void, Void, List<Catagory>> {

        @Override
        protected List<Catagory> doInBackground(Void... voids) {

            List<Catagory> result = new ArrayList<>();
            try {
                result = new WooCommerce().getCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Catagory> catagories) {
            super.onPostExecute(catagories);
            //Because the first Element is UnCategorized in APi
            //the index 0 deleted

            for (int i = 1; i < catagories.size(); i++)
                mCatagoryTitles.add(catagories.get(i).getName());

            mCatagories = catagories;

        }
    }

}
