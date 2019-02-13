package com.example.market.controllers.fragmnet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.interfaces.FragmentStart;
import com.example.market.model.Category;
import com.example.market.model.ProductLab;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements FragmentStart {

    //Position of category tab
    private static final String ARG_TAB_POSITION = "tab_position";
    private ProductLab mProductLab = ProductLab.getInstance();
    private List<Category> mSubCatagories;
    private RecyclerView mRecyclerView;

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

        setUpRecyclerView();
        return view;
    }

    @Override
    public void findViewByIds(View view) {
        mRecyclerView = view.findViewById(R.id.subCategory_recy_categoryF);
    }

    @Override
    public void variableInit() {
        int tabPostion = getArguments().getInt(ARG_TAB_POSITION);
        int parenId = mProductLab.getParentId(tabPostion);
        mSubCatagories = mProductLab.getUniqueSubCategory(parenId);

    }

    @Override
    public void setListeners() {

    }

    private void setUpRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(mSubCatagories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTxtCategory;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_img_Categoryitem);
            mTxtCategory = itemView.findViewById(R.id.name_txt_Categoryitem);
        }

        public void bind(Category category) {
            mTxtCategory.setText(category.getName());
            Picasso.get().load(category.getImage())
                    .placeholder(R.drawable.shop_placeholder)
                    .into(mImageView);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> mCategories;

        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;
        }

        public void setCategories(List<Category> categories) {
            mCategories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.category_item, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

            holder.bind(mCategories.get(position));
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }


}
