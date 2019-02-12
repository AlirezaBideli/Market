package com.example.market.controllers.fragmnet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.market.R;
import com.example.market.interfaces.FragmentStart;
import com.example.market.model.Catagory;
import com.example.market.model.ProductLab;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements FragmentStart {

    //Position of category tab
    private static final String ARG_TAB_POSITION = "tab_position";
    private ProductLab mProductLab = ProductLab.getInstance();
    private List<Catagory> mSubCatagories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(int tabPosition) {

        Bundle args = new Bundle();
        args.putInt(ARG_TAB_POSITION, tabPosition);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();
        return view;
    }

    @Override
    public void findViewByIds(View view) {

    }

    @Override
    public void variableInit() {

        int tabPostion = getArguments().getInt(ARG_TAB_POSITION);
        int parentId =searchParentId(tabPostion);
        mSubCatagories=mProductLab.getSubCategories();

    }

    @Override
    public void setListeners() {

    }


    private int searchParentId(int tabPositioin) {
        List<Catagory> catagories = mProductLab.getCategories();
        return  catagories.get(tabPositioin).getId();
    }
}
