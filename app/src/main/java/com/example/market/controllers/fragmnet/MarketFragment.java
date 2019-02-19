package com.example.market.controllers.fragmnet;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.squareup.picasso.Picasso;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends ParentFragment {


    //CallBacks
    private CallBacks mCallBacks;
    //Widgets Variables
    private RecyclerView mRecyNewest;
    private RecyclerView mRecyMostVisited;
    private RecyclerView mRectBestSellers;
    //Simple Variables
    private ProductAdapter mRecyAdapter;
    private int mDeafaultCount = 20;
    private List<Product> mProducts;

    public static MarketFragment newInstance() {

        Bundle args = new Bundle();

        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MarketActivity)
            mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_market, container, false);
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
        fillRecyclerView(layoutManager, ProductType.NEWEST, mRecyNewest);
        //Best Sellers
        fillRecyclerView(layoutManager3, ProductType.MOST_VISITED, mRectBestSellers);
        //Most Visited RecyclerView
        fillRecyclerView(layoutManager2, ProductType.BESTS, mRecyMostVisited);

    }

    private void fillRecyclerView(LinearLayoutManager layoutManager2
            , ProductType type, RecyclerView recyMostVisited) {

        ProductLab productLab = ProductLab.getInstance();
        List<Product> products = new ArrayList<>();
        products = getProducts(type, productLab, products);
        recyMostVisited.setLayoutManager(layoutManager2);
        recyMostVisited.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        recyMostVisited.setAdapter(mRecyAdapter);
    }

    private List<Product> getProducts(ProductType type, ProductLab productLab
            , List<Product> products) {
        switch (type) {
            case NEWEST:
                products = productLab.getNewestProducts();
                break;
            case MOST_VISITED:
                products = productLab.getMVisitedProducts();
            case BESTS:
                products = productLab.getBProducts();
                break;
        }
        return products;
    }


    @Override
    protected void findViewByIds(View view) {

        mRecyNewest = view.findViewById(R.id.recy_newest_MarketA);
        mRecyMostVisited = view.findViewById(R.id.recy_mostVisited_MarketA);
        mRectBestSellers = view.findViewById(R.id.recy_bests_MarketA);
    }

    @Override
    public void variableInit() {

    }

    @Override
    public void setListeners() {

    }


    private enum ProductType {
        NEWEST,
        MOST_VISITED,
        BESTS,
    }


    public interface CallBacks {
        void showProductDetails(int id);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    int id =mProducts.get(position).getId();
                    mCallBacks.showProductDetails(id);
                }
            });
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



        public ProductAdapter(List<Product> products) {
            mProducts = products;
        }

        public List<Product> getProducts() {
            return mProducts;
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
