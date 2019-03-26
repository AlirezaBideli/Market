package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Category;
import com.example.market.model.Image;
import com.example.market.model.repositories.ProductLab;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryListFragment extends ParentFragment {

    //Position of category tab
    private static final String ARG_TAB_POSITION = "tab_position";
    private static final String TAG = "CategoryList";
    private ProductLab mProductLab;
    private List<Category> mSubCategories;
    private RecyclerView mRecyclerView;
    private CallBacks mCallBacks;
    private CategoryAdapter mCategoryAdapter;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    public static CategoryListFragment newInstance(int tabPosition) {

        Bundle args = new Bundle();
        args.putInt(ARG_TAB_POSITION, tabPosition);
        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBacks)
            mCallBacks = (CallBacks) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();

        int tabPostion = getArguments().getInt(ARG_TAB_POSITION);
        mSubCategories = mProductLab.getUniqueSubCategory(tabPostion);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void findViewByIds(View view) {
        mRecyclerView = view.findViewById(R.id.subCategory_recy_categoryF);
    }

    @Override
    public void variableInit() {
        mProductLab = ProductLab.getInstance(getActivity());

    }

    @Override
    public void setListeners() {

    }


    private void setUpRecyclerView() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mCategoryAdapter = new CategoryAdapter(mSubCategories);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCategoryAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    public interface CallBacks {
        void gotToProductList(int subCatId);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTxtCategory;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_img_Producttem);
            mTxtCategory = itemView.findViewById(R.id.name_txt_Productitem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category category = mCategoryAdapter.getCategories()
                            .get(getAdapterPosition());
                    mCallBacks.gotToProductList(category.getId());

                }
            });
        }

        public void bind(Category category) {
            mTxtCategory.setText(category.getName());
            Image image = category.getImage();


            if (image != null) {
                Picasso.get().load(category.getImage().getSrc())
                        .placeholder(R.drawable.shop_placeholder)
                        .into(mImageView);
            } else
                mImageView.setImageResource(R.drawable.shop_placeholder);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> mCategories;

        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;
        }

        public List<Category> getCategories() {
            return mCategories;
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
