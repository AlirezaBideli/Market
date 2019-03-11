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
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProductFragment extends ParentFragment {


    //CallBacks
    LoadingCallBack mLoadingCallBack;
    private RecyclerView mRecySearch;
    private List<Product> mProducts;
    private DetailCallBack mDetailCallBack;
    private SearchView mSearchView;
    private TextView mTxtNotFound;
    private FragmentManager mFragmentManager;

    public SearchProductFragment() {
        // Required empty public constructor
    }

    public static SearchProductFragment newInstance() {

        Bundle args = new Bundle();

        SearchProductFragment fragment = new SearchProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);
        findViewByIds(view);
        setListeners();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailCallBack)
            mDetailCallBack = (DetailCallBack) context;

        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;


    }



    @Override
    public void onDetach() {
        super.onDetach();
        mDetailCallBack = null;
        mLoadingCallBack = null;
    }

    @Override
    protected void findViewByIds(View view) {
        mRecySearch = view.findViewById(R.id.recy_search);
        mSearchView = view.findViewById(R.id.product_searchView);
        mTxtNotFound = view.findViewById(R.id.txt_not_found);
    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mLoadingCallBack.showLoading();
                RetrofitClientInstance.getRetrofitInstance()
                        .create(Api.class).getResultProducts(query)
                        .enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                if (response.isSuccessful()) {
                                    if (getActivity() != null) {
                                        mProducts = response.body();

                                        if (getActivity()!=null) {
                                            if (mProducts != null && mProducts.size() > 0) {
                                                setUpRecyclerView(mProducts);
                                                mTxtNotFound.setVisibility(View.INVISIBLE);
                                            } else
                                                mTxtNotFound.setVisibility(View.VISIBLE);
                                            mLoadingCallBack.hideLoading();
                                        }

                                    } else if (getActivity() != null)
                                        NetworkConnection.warnConnection(getActivity(), mFragmentManager);
                                }

                            }

                            @Override
                            public void onFailure(Call<List<Product>> call, Throwable t) {

                                if (getActivity() != null)
                                    NetworkConnection.warnConnection(getActivity(), mFragmentManager);
                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });


    }

    private void setUpRecyclerView(List<Product> searchResults) {
        SearchAdapter searchAdapter = new SearchAdapter(searchResults);
        mRecySearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecySearch.setHasFixedSize(true);
        mRecySearch.setAdapter(searchAdapter);
    }


    private class SearchHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private ImageView mImgProduct;

        public SearchHolder(@NonNull View itemView) {
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

    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {


        private List<Product> mProducts;

        public SearchAdapter(List<Product> products) {
            mProducts = products;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.product_item, parent, false);
            return new SearchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }


}
