package com.example.market.controllers.fragmnet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends ParentFragment {


    private static final String ARG_SUBCAT_ID = "subCatId";
    //Widgets variables
    private RecyclerView mRecyProducts;
    private TextView mTxtNotFound;
    //Simple Variables
    private List<Product> mProducts;
    private ProductLab mProductLab;
    private ProductAdapter mProductAdapter;


    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance() {

        Bundle args = new Bundle();
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mTxtNotFound=view.findViewById(R.id.notFound_txt_productListF);
    }

    @Override
    protected void variableInit() {

        mProductLab = ProductLab.getInstance();


        mProducts = mProductLab.getProducts();
        checkCount();
        setUpRecyclerView();


    }

    @Override
    protected void setListeners() {

    }

    private void checkCount()
    {
        if (mProducts.size()==0)
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


    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private ImageView mImgProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.name_txt_Productitem);
            mImgProduct = itemView.findViewById(R.id.image_img_Producttem);
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
