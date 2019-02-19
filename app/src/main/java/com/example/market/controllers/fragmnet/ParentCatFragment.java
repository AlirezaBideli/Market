package com.example.market.controllers.fragmnet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.market.R;
import com.example.market.model.Category;
import com.example.market.model.ProductLab;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ParentCatFragment extends ParentFragment {

    public static final String TAG = "CategoryActivity";

    private TabLayout mTabLayout;
    private ViewPager mCategoryPager;
    private List<String> mCategoryTitles;


    public static ParentCatFragment newInstance() {

        Bundle args = new Bundle();

        ParentCatFragment fragment = new ParentCatFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_parent_cat,container,false);
       findViewByIds(view);
       variableInit();
        return view;
    }


    @Override
    protected void findViewByIds(View view) {


        mTabLayout = view.findViewById(R.id.category_tab_parentCatF);
        mCategoryPager = view.findViewById(R.id.pager_category_ParentCategoryF);
    }


    @Override
    public void onStart() {
        super.onStart();
        addTabs();


            mCategoryPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
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
    public void variableInit() {

        ProductLab productLab = ProductLab.getInstance();
        List<Category> catagories = productLab.getCategories();
        mCategoryTitles = productLab.getCatagoryTitles();


    }

    @Override
    public void setListeners() {





    }

    private void addTabs()
    {
        int size=mCategoryTitles.size();
        for (int i=0;i<size;i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(mCategoryTitles.get(i));
            mTabLayout.addTab(tab);
        }
    }









}
