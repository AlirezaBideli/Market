package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.DetailCallBack;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Product;
import com.example.market.model.repositories.ProductLab;
import com.example.market.model.SortType;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends ParentFragment implements View.OnClickListener {


    //Arguments Tag
    private static final String ARG_SUBCAT_ID = "subCatId";
    private static final String STATE_PAGE = "page";
    private static final String TAG = "ProductListFragment";
    private static final String SORT_DIALOG_TAG = "SortDialogTag";
    //Widgets variables
    public RecyclerView mRecyProducts;
    //CallBack
    private DetailCallBack mDetailCallBack;
    private LoadingCallBack mLoadingCallBack;
    private TextView mTxtNotFound;
    private MaterialCardView mBtnSort;
    private MaterialCardView  mBtnFilter;
    //Simple Variables
    private List<Product> mProducts;
    private ProductLab mProductLab;
    private ProductAdapter mProductAdapter;
    private int mPage = 1;
    private Call<List<Product>> mProductCall;
    private int mSubcatId;
    private CallBacks mCallBack;

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


    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailCallBack) {
            mDetailCallBack = (DetailCallBack) context;
        }

        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;

        if (context instanceof CallBacks)
            mCallBack = (CallBacks) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDetailCallBack = null;
        mLoadingCallBack = null;
        mCallBack = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();

        return view;
    }

    @Override
    protected void findViewByIds(View view) {

        mRecyProducts = view.findViewById(R.id.recy_product_list);
        mTxtNotFound = view.findViewById(R.id.notFound_txt_productListF);
        mBtnSort = view.findViewById(R.id.sort_btn);
        mBtnFilter = view.findViewById(R.id.filter_btn);
    }

    @Override
    protected void variableInit() {
        mProducts = new ArrayList<>();
        mProductLab = ProductLab.getInstance(getActivity());
        SortDialogFragment.misFirstTime = true;
        SortDialogFragment.mRadioCheckedPosition = 0;

        mSubcatId = getArguments().getInt(ARG_SUBCAT_ID);
        getProducts(mSubcatId, SortType.NEWEST);


    }




    public void refreshList(SortType sortType) {
        getProducts(mSubcatId, sortType);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProductCall != null)
            mProductCall.cancel();
    }

    private void getProducts(int subCatId, SortType sortType) {

        if (mLoadingCallBack != null) {
            mLoadingCallBack.showLoading();
            mProductCall = getProductListCall(subCatId, sortType);
            mProductCall.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> result = response.body();
                        if (getActivity() != null && result != null && result.size() > 0) {
                            mProducts = result;
                            mProductLab.setProducts(mProducts);
                            setUpRecyclerView();
                            mLoadingCallBack.hideLoading();
                        } else if (getActivity() != null && result != null) {
                            checkCount();
                            mLoadingCallBack.hideLoading();

                        }
                    } else
                        NetworkConnection.warnConnection
                                (getActivity(), getFragmentManager());
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    if (getActivity() != null)
                        NetworkConnection.warnConnection
                                (getActivity(), getFragmentManager());
                }
            });
        }
    }

    @Override
    protected void setListeners() {
        mBtnSort.setOnClickListener(this);
        mBtnFilter.setOnClickListener(this);
    }

    private void checkCount() {
        if (mProducts.size() == 0) {
            mTxtNotFound.setVisibility(View.VISIBLE);
            mBtnFilter.setEnabled(false);
            mBtnSort.setEnabled(false);
        }
        else
            mTxtNotFound.setVisibility(View.INVISIBLE);
    }

    private void setUpRecyclerView() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mProductAdapter = new ProductAdapter(mProducts);
        mRecyProducts.setLayoutManager(layoutManager);
        mRecyProducts.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        mRecyProducts.addItemDecoration(dividerItemDecoration);
        mRecyProducts.setAdapter(mProductAdapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_btn:
                FragmentManager fragmentManager = getFragmentManager();
                SortDialogFragment sortDialogFragment = SortDialogFragment.newInstance();
                sortDialogFragment.show(fragmentManager, SORT_DIALOG_TAG);
                break;

            case R.id.filter_btn:
                mCallBack.showFilterPage();
                break;
        }
    }

    private Call getProductListCall(int subCatId, SortType sortType) {
        Call productCall = null;
        Api api = RetrofitClientInstance.getRetrofitInstance().create(Api.class);
        switch (sortType) {
            case NEWEST:
                productCall = api.getCatProducts(subCatId, OrderBy.DATE.getName(), Order.DeCENDING.getName());
                break;
            case BEST_SELLERS:
                productCall = api.getCatProducts(subCatId, OrderBy.POPULARITY.getName(), Order.DeCENDING.getName());
                break;
            case PRICE_ASC:
                productCall = api.getCatProducts(subCatId, OrderBy.PRICE.getName(), Order.ASCENDING.getName());
                break;
            case PRICE_DESC:
                productCall = api.getCatProducts(subCatId, OrderBy.PRICE.getName(), Order.DeCENDING.getName());
                break;
        }
        return productCall;
    }

    private enum OrderBy {
        DATE("date"),
        POPULARITY("popularity"),
        PRICE("price");

        private String name;

        OrderBy(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    private enum Order {
        ASCENDING("asc"),
        DeCENDING("desc");


        private String name;

        Order(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface CallBacks {
        void showFilterPage();
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
                    mDetailCallBack.showProductDetails(id);
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
