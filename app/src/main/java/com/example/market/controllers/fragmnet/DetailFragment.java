package com.example.market.controllers.fragmnet;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends ParentFragment {


    private static final String ARG_PRODUCT = "product";
    String[] mHeaders;

    private RecyclerView mRecyDetail;
    private DetailAdapter mDetailAdapter;
    private int mMainHeader=0;
    private int mOtherHeader=1;
    private int mViewType;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT,product);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        findViewByIds(view);
        mHeaders = new String[]{getString(R.string.header_main_detail)
                , getString(R.string.header_other_detail)};
        setUpRecyclerView();
        return view;
    }


    @Override
    protected void findViewByIds(View view) {
        mRecyDetail = view.findViewById(R.id.detailList_recy_detailF);
    }

    private void setUpRecyclerView() {
        Product product = (Product) getArguments().getSerializable(ARG_PRODUCT);
        List<Product.Attributes> attributes = product.getAttributes();

        mDetailAdapter = new DetailAdapter(attributes,product);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyDetail.setLayoutManager(layoutManager);
        mRecyDetail.setHasFixedSize(true);
        mRecyDetail.setAdapter(mDetailAdapter);

    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {

    }

    private class DetailHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle, mTxtValue;
        private TextView mTxtMainHeader, mTxtOtherHeader, mTxtWeight, mTxtDimens;

        public DetailHolder(@NonNull View itemView) {
            super(itemView);
            if (mViewType == mMainHeader) {
                mTxtMainHeader = itemView.findViewById(R.id.mainHeader_detaillItem);
                mTxtWeight = itemView.findViewById(R.id.weight_detailItem);
                mTxtDimens = itemView.findViewById(R.id.dimensions_detailItem);
                mTxtOtherHeader =itemView.findViewById(R.id.otherHeader_detailFragmnet);

            } else {
                mTxtTitle = itemView.findViewById(R.id.tittle_detailItem);
                mTxtValue = itemView.findViewById(R.id.weight_detailItem);
            }

        }

        public void bind(Product.Attributes attributes, Product product) {

            if (mViewType == mMainHeader) {

                mTxtMainHeader.setText(mHeaders[0]);
                mTxtOtherHeader.setText(mHeaders[1]);
                mTxtWeight.setText(product.getWeight());
                String length = product.getDimensions().getLength();
                String width = product.getDimensions().getWidth();
                String height = product.getDimensions().getHeight();
                String dimensions="";
                if (!length.equals(""))
                    dimensions = length + "*" + width + "*" + height;
                mTxtDimens.setText(dimensions);

            } else {
                mTxtTitle.setText(attributes.getName());
                mTxtValue.setText(attributes.getOptions());
            }
        }
    }

    private class DetailAdapter extends RecyclerView.Adapter<DetailHolder> {

        private Product mProduct;
        private List<Product.Attributes> mAttributes;

        private DetailAdapter(List<Product.Attributes> details, Product product) {
            mAttributes = details;
            mProduct = product;
        }

        @NonNull
        @Override
        public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == mMainHeader)
                view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.detail_heder_item, parent, false);
            else
                view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.detail_item, parent, false);
            return new DetailHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailHolder holder, int position) {


            holder.bind(mAttributes.get(position), mProduct);
        }

        @Override
        public int getItemCount() {
            return mAttributes.size();
        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                mViewType = mMainHeader;
                return mMainHeader;
            }
            else {
                mViewType=mOtherHeader;
                return mOtherHeader;
            }

        }

        public void setAttributes(List<Product.Attributes> attributes) {
            mAttributes = attributes;
        }
    }
}
