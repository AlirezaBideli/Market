package com.example.market.controllers.fragmnet;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.utils.ActivityHeper;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends ParentFragment {




    //RecyclerViews
    private RecyclerView mRecyNewest;
    private RecyclerView mRecyMostVisited;
    private RecyclerView mRectBestSellers;
    private ProductAdapter mRecyAdapter;
    private int mDeafaultCount = 20;

    public static MarketFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view =inflater.inflate(R.layout.fragment_market,container,false);
        findViewByIds(view);
        variableInit();
        setListeners();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        List<Product> products;
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);


        //Newest RecyclerView
        products = ProductLab.getInstance().getNewestProducts();
        mRecyNewest.setLayoutManager(layoutManager);
        mRecyNewest.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRecyNewest.setAdapter(mRecyAdapter);
        //Best Sellers
        products= ProductLab.getInstance().getBProducts();
        mRectBestSellers.setLayoutManager(layoutManager3);
        mRectBestSellers.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRectBestSellers.setAdapter(mRecyAdapter);
        //Most Visited RecyclerView
        products = ProductLab.getInstance().getMVisitedProducts();
        mRecyMostVisited.setLayoutManager(layoutManager2);
        mRecyMostVisited.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRecyMostVisited.setAdapter(mRecyAdapter);

    }



    @Override
    protected void findViewByIds(View view) {

        mRecyNewest = view.findViewById(R.id.recy_newest_MarketA);
        mRecyMostVisited = view.findViewById(R.id.recy_mostVisited_MarketA);
        mRectBestSellers=view.findViewById(R.id.recy_bests_MarketA);
    }

    @Override
    public void variableInit() {

    }

    @Override
    public void setListeners() {

    }


    private class ProductHolder extends RecyclerView.ViewHolder {


        private TextView mTxtName;
        private TextView mTxtPrice;
        private ImageView mImgProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.name_txt_specialItem);
            mTxtPrice = itemView.findViewById(R.id.price_txt_specialItem);
            mImgProduct = itemView.findViewById(R.id.image_img_specialItem);
        }

        public void bind(Product product) {

            mTxtName.setText(product.getName());
            mTxtPrice.setText(product.getPrice());

            if (product.getImages() == null)
                mImgProduct.setImageResource(R.drawable.shop_placeholder);
            else {
                String src = product.getImages().get(0).getSrc();
                Picasso.get()
                        .load(src)
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

            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.special_product_item, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {

            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            if (mProducts.size() < mDeafaultCount)
                return mProducts.size();

            return mDeafaultCount;
        }
    }


}
