package com.example.market.controllers.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends ParentFragment implements View.OnClickListener {

    private static final byte ATTRIBUTE_NAME = 0;
    private static final byte ATTRIBUTE_TERM = 1;
    private RecyclerView mRecyAttrName, mRecyAttrTerm;
    private List<Product.Attributes> mAttributes;
    private Call<List<Product.Terms>> mTermsCall;
    private AttributeNameAdapter mNameADapter;
    private List<Product.Terms> mTerms;
    private AttributeTermAdapter mTermADapter;
    private FrameLayout mLoadingBar;
    private MaterialCardView mBtnFilter;
    private CallBacks mCallBacks;

    private ArrayList<String> mFilterNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> mFilterValues = new ArrayList<>();
    private Product.Attributes mCurrentAttribute;
    private View mPreviousClickedItem;
    private TextView mPreviousAttrNameClicked;

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {

        Bundle args = new Bundle();

        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        findViewByIds(view);
        setListeners();
        setUpNameRecyclerView();
        return view;

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
    protected void findViewByIds(View view) {

        mRecyAttrName = view.findViewById(R.id.recy_attribute_name);
        mRecyAttrTerm = view.findViewById(R.id.recy_attribute_terms);
        mLoadingBar = view.findViewById(R.id.loading_filter);
        mBtnFilter = view.findViewById(R.id.filter_btn);
    }

    private void setUpNameRecyclerView() {
        ProductLab productLab = ProductLab.getInstance(getActivity());
        mAttributes = productLab.getMostCompletedAttribute();
        mNameADapter = new AttributeNameAdapter(mAttributes);
        mRecyAttrName.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyAttrName.setAdapter(mNameADapter);
    }


    private void downLoadTerms(Product.Attributes attributes) {
        mLoadingBar.setVisibility(View.VISIBLE);

        int id = attributes.getId();
        mTermsCall = RetrofitClientInstance.
                getRetrofitInstance().
                create(Api.class).getAttributeTerms(id);
        mTermsCall.enqueue(new Callback<List<Product.Terms>>() {
            @Override
            public void onResponse(Call<List<Product.Terms>> call, Response<List<Product.Terms>> response) {
                if (response.isSuccessful()) {

                    setUpTermRecyclerView(response);

                } else if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());

            }

            @Override
            public void onFailure(Call<List<Product.Terms>> call, Throwable t) {

                if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
            }
        });


    }

    private void setUpTermRecyclerView(Response<List<Product.Terms>> response) {
        mLoadingBar.setVisibility(View.INVISIBLE);
        mTerms = response.body();
        mTermADapter = new AttributeTermAdapter(mTerms);
        mRecyAttrTerm.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyAttrTerm.setAdapter(mTermADapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && mTermsCall != null)
            mTermsCall.cancel();
    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {
        mBtnFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_btn:
                ProductLab productLab = ProductLab.getInstance(getActivity());
                List<Product> products = productLab.getProducts();

                mCallBacks.showProductList(this);
                break;
        }
    }


    public interface CallBacks {
        void showProductList(FilterFragment filterFragment);
    }

    private class AttributesNameHolder extends RecyclerView.ViewHolder {


        private TextView mTxtAttributeName;


        public AttributesNameHolder(final View itemView) {
            super(itemView);
            mTxtAttributeName = itemView.findViewById(R.id.txt_attribute_name);
            final int defaultBackGroundColor = getResources().getColor(R.color.gray_800);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int itemPosition = getAdapterPosition();
                    mCurrentAttribute = mAttributes.get(itemPosition);
                    downLoadTerms(mCurrentAttribute);

                    itemView.setBackgroundColor(Color.WHITE);
                    mTxtAttributeName.setTextColor(Color.BLACK);
                    if (mPreviousClickedItem != null) {
                        mPreviousClickedItem.setBackgroundColor(defaultBackGroundColor);
                        mPreviousAttrNameClicked.setTextColor(Color.WHITE);
                    }
                    mPreviousClickedItem = itemView;
                    mPreviousAttrNameClicked = mTxtAttributeName;

                }
            });
        }


        public void bind(Product.Attributes attribute) {
            mTxtAttributeName.setText(attribute.getName());
        }
    }

    private class AttributeNameAdapter extends RecyclerView.Adapter<AttributesNameHolder> {

        private List<Product.Attributes> mAttributes;

        private AttributeNameAdapter(List<Product.Attributes> attributes) {
            mAttributes = attributes;
        }

        @NonNull
        @Override
        public AttributesNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.attribute_name_item, parent, false);
            return new AttributesNameHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AttributesNameHolder holder, int position) {
            holder.bind(mAttributes.get(position));
        }

        @Override
        public int getItemCount() {
            return mAttributes.size();
        }

        public void setAttributes(List<Product.Attributes> attributes) {
            mAttributes = attributes;
        }
    }

    private class AttributesTermHolder extends RecyclerView.ViewHolder {

        private ArrayList<String> mAttributeValues = new ArrayList<>();
        private CheckBox mTxtAttributeTerm;


        public AttributesTermHolder(final View itemView) {
            super(itemView);
            mTxtAttributeTerm = itemView.findViewById(R.id.chk_attribute_term);
            mTxtAttributeTerm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String filterName = mCurrentAttribute.getName();
                    if (!mFilterNames.contains(filterName))
                        mFilterNames.add(filterName);

                    String filterValue = mTerms.get(getAdapterPosition()).getName();
                    if (!mAttributeValues.contains(filterValue))
                        mAttributeValues.add(filterValue);

                    if (!mFilterValues.containsAll(mAttributeValues))
                        mFilterValues.add(mAttributeValues);
                    mTxtAttributeTerm.setTextColor(Color.GRAY);

                }
            });

        }


        public void bind(Product.Terms term) {
            mTxtAttributeTerm.setText(term.getName());
        }
    }

    private class AttributeTermAdapter extends RecyclerView.Adapter<AttributesTermHolder> {

        private List<Product.Terms> mTerms;

        private AttributeTermAdapter(List<Product.Terms> terms) {
            mTerms = terms;
        }

        @NonNull
        @Override
        public AttributesTermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.attribute_term_item, parent, false);
            return new AttributesTermHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AttributesTermHolder holder, int position) {
            holder.bind(mTerms.get(position));
        }

        @Override
        public int getItemCount() {
            return mTerms.size();
        }

        public void setAttributes(List<Product.Terms> terms) {
            mTerms = terms;
        }
    }


}
