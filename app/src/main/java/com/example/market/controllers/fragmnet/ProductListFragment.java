package com.example.market.controllers.fragmnet;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.interfaces.LoadingCallBack;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends ParentFragment {


    //Arguments Tag
    private static final String ARG_SUBCAT_ID = "subCatId";
    private static final String STATE_PAGE = "page";
    private static final String TAG = "ProductListFragment";
    //CallBack
    private CallBacks mCallBacks;
    private LoadingCallBack mLoadingCallBack;
    //Widgets variables
    private RecyclerView mRecyProducts;
    private TextView mTxtNotFound;
    //Simple Variables
    private List<Product> mProducts;
    private ProductLab mProductLab;
    private ProductAdapter mProductAdapter;
    private int mPage = 1;
    private Call<List<Product>> mProductCall;

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(int subCatId) {

        Bundle args = new Bundle();
        args.putInt(ARG_SUBCAT_ID, subCatId);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryActivity) {
            mCallBacks = (CallBacks) context;
            mLoadingCallBack = (LoadingCallBack) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
        mLoadingCallBack = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        findViewByIds(view);
        variableInit();

        return view;
    }

    @Override
    protected void findViewByIds(View view) {

        mRecyProducts = view.findViewById(R.id.recy_product_list);
        mTxtNotFound = view.findViewById(R.id.notFound_txt_productListF);
    }

    @Override
    protected void variableInit() {
        mProducts = new ArrayList<>();
        mProductLab = ProductLab.getInstance();


    }


    @Override
    public void onResume() {
        super.onResume();
        int subCatId = getArguments().getInt(ARG_SUBCAT_ID);
        getProducts(subCatId);
        Log.i(TAG,"on Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mProductCall.cancel();
        Log.i(TAG,"on Pause");
    }

    private void getProducts(int subCatId) {

        if (mLoadingCallBack != null) {
            mLoadingCallBack.showLoading();

            mProductCall = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                    .getCatProducts(subCatId, mPage);
            mProductCall.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> result = response.body();
                        if (result != null && result.size() > 0) {
                            mProducts.addAll(result);
                            getProducts(++mPage);

                        } else if (mProducts != null && mLoadingCallBack != null) {
                            checkCount();
                            setUpRecyclerView();
                            mLoadingCallBack.hideLoading();

                        }
                    } else
                        NetworkConnection.warnConnection
                                (getActivity(), getFragmentManager());
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    if (getActivity()!=null)
                    NetworkConnection.warnConnection
                            (getActivity(), getFragmentManager());
                }
            });
        }
    }

    @Override
    protected void setListeners() {

    }

    private void checkCount() {
        if (mProducts.size() == 0)
            mTxtNotFound.setVisibility(View.VISIBLE);
        else
            mTxtNotFound.setVisibility(View.INVISIBLE);
    }

    private void setUpRecyclerView() {

        mProductAdapter = new ProductAdapter(mProducts);
        mRecyProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyProducts.setHasFixedSize(true);
        mRecyProducts.setAdapter(mProductAdapter);


    }


    public interface CallBacks {
        void showProductDetails(int id);
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private ImageView mImgProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.name_txt_Productitem);
            mImgProduct = itemView.findViewById(R.id.image_img_Producttem);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = mProducts.get(getAdapterPosition());
                    final int id = product.getId();
                    mCallBacks.showProductDetails(id);
                }
            });
        }

        public void bind(Product product) {
            mTxtName.setText(product.getName());
            if (product.getImages() == null)
                mImgProduct.setImageResource(R.drawable.shop_placeholder);
            else {
                String src = product.getImages().get(0).getSrc();
                Picasso.get().
                        load(src)
                        .placeholder(R.drawable.shop_placeholder)
                        .into(mImgProduct);
            }

        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {


        private List<Product> mProducts;

        public ProductAdapter(List<Product> products) {
            mProducts = products;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.product_item, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }
}
