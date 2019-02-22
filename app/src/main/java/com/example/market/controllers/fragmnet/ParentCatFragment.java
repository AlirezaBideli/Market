package com.example.market.controllers.fragmnet;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.market.R;
import com.example.market.model.Category;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentCatFragment extends ParentFragment {

    public static final String TAG = "CategoryActivity";
    private int mCatPage = 1;
    private int mSubCatPage = 1;
    private TabLayout mTabLayout;
    private ViewPager mCategoryPager;
    private List<String> mCategoryTitles;
    private ProgressDialog mProgressDialog;
    private int mTabPosition;
    private List<Category> mCategories;
    private List<Category> mSubCategories;

    public static ParentCatFragment newInstance() {

        Bundle args = new Bundle();

        ParentCatFragment fragment = new ParentCatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_cat, container, false);
        findViewByIds(view);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.show();


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        variableInit();
    }

    @Override
    protected void findViewByIds(View view) {


        mTabLayout = view.findViewById(R.id.category_tab_parentCatF);
        mCategoryPager = view.findViewById(R.id.pager_category_ParentCategoryF);
    }

    @Override
    public void variableInit() {
        mCategories = new ArrayList<>();
        mCategoryTitles = new ArrayList<>();
        mSubCategories=new ArrayList<>();
        getCategories(mCatPage);


    }

    private void getCategories(int page) {
        RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getCategories(page)
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if (response.isSuccessful()) {
                            List<Category> result = response.body();
                            if (result != null && result.size() > 0) {
                                mCategories.addAll(result);
                                getCategories(++mCatPage);
                            } else if (mCategories != null) {
                                ProductLab.getInstance().setCatagories(mCategories);
                                getSubCategory(mSubCatPage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

                    }
                });
    }

    private void getSubCategory(int page) {
        RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getSubCategories(mSubCatPage).
                enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if (response.isSuccessful()) {
                            List<Category> result = response.body();
                            if (result!=null && result.size() > 0) {
                                mSubCategories.addAll(result);
                                getSubCategory(++mSubCatPage);
                            }
                            else if (mSubCategories!=null ){
                                ProductLab.getInstance().setSubCategories(mSubCategories);
                                setPagerWithTabLayout(mCategories);
                                mProgressDialog.cancel();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

                    }
                });
    }

    private void setPagerWithTabLayout(List<Category> categories) {
        for (int i = 0; i < categories.size(); i++) {
            mCategoryTitles.add(categories.get(i).getName());
        }
        mTabLayout.setVisibility(View.VISIBLE);
        mCategoryPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                mTabPosition = position;
                return CategoryListFragment.newInstance(position);
            }

            @Override
            public int getCount() {

                return mCategoryTitles.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mCategoryTitles.get(position);
            }


        });
        mTabLayout.setupWithViewPager(mCategoryPager);
    }


    @Override
    public void setListeners() {


    }



}
