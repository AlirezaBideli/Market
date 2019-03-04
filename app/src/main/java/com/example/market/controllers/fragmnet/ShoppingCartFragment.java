package com.example.market.controllers.fragmnet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.utils.PriceUtils;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends ParentFragment {


    public static final int TOTAL_PRICES_INNER_SIZE = 2;
    public static final int ITEM_COUNT = 5;
    private static int mTotalFinalPrice = 0;
    //Widgets Variables
    private RecyclerView mRecyOrdered;
    private TextView mTxtTotalPriceSum;
    private Button mBtnFinishPurchasing;
    //SimpleVariables
    private List<Product> mOrderedProducts = new ArrayList<>();
    private int[] mTotalPrices;
    private int[] mCalculatedTotalPrices;
    private List<Integer> mProductCounts;
    private OrderedProductAdapter mAdapter;


    public ShoppingCartFragment() {
        // Required empty public constructor
    }


    public static ShoppingCartFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingCartFragment fragment = new ShoppingCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_shoping_cart, container, false);
        findViewByIds(view);
        setUpRecyclerView();

        setTotalPrices();
        //fill TxtTotalSumPrice according to mBoughProductList prices
        setTxtTotalSumPrice();
        checkOrderedProductsSize();
        return view;
    }


    private void setUpRecyclerView() {

        mOrderedProducts = ProductLab.getInstance().getShopingCartPro();
        mAdapter = new OrderedProductAdapter(mOrderedProducts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyOrdered.setHasFixedSize(true);
        mRecyOrdered.setLayoutManager(layoutManager);
        mRecyOrdered.setAdapter(mAdapter);
    }

    @Override
    protected void findViewByIds(View view) {
        mRecyOrdered = view.findViewById(R.id.recy_bought_products);
        mTxtTotalPriceSum = view.findViewById(R.id.total_price_txt);
        mBtnFinishPurchasing = view.findViewById(R.id.finish_purchasing_btn);
    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {

    }

    private List<Integer> getSpinnerItems() {
        List<Integer> result = new ArrayList<>(5);
        for (int i = 1; i <= ITEM_COUNT; i++)
            result.add(i);
        return result;
    }

    private void setTxtTotalSumPrice() {
        int result = 0;
        int size = mCalculatedTotalPrices.length;
        for (int i = 0; i < size; i++) {
            String price = mCalculatedTotalPrices[i] + "";
            result += Integer.parseInt(price);
        }
        String totalPrice = PriceUtils.getCurrencyFormat(result + "", getActivity());
        mTxtTotalPriceSum.setText(totalPrice);
    }

    private void setTotalPrices() {
        int price;
        int totalPricesSize = mOrderedProducts.size();
        mTotalPrices = new int[totalPricesSize];
        mCalculatedTotalPrices = new int[totalPricesSize];
        for (int i = 0; i < totalPricesSize; i++) {
            price = Integer.parseInt(mOrderedProducts.get(i).getPrice());
            mTotalPrices[i] = price;
            mCalculatedTotalPrices[i] = price;
        }
    }

    private void setUniqueCalculatedTotalPrice(int productCount, int productPosition) {
        int index = productPosition;
        mCalculatedTotalPrices[index] = mTotalPrices[index] * productCount;
    }

    @Override
    public void onPause() {
        super.onPause();
        mTotalFinalPrice = 0;
    }

    private void checkOrderedProductsSize() {
        if (mOrderedProducts.size() == 0)
            mBtnFinishPurchasing.setEnabled(false);
        else
            mBtnFinishPurchasing.setEnabled(true);
    }

    private class OrderedProductHolder extends RecyclerView.ViewHolder {


        private ImageView mImgProduct;
        private TextView mTxtTitle;
        private TextView mTxtTotalPrice, mTxtFinalPrice;
        private Spinner mSpnCount;
        private MaterialButton mBtnDelete;

        public OrderedProductHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.title_txt);
            mTxtTotalPrice = itemView.findViewById(R.id.total_price_txt);
            mTxtFinalPrice = itemView.findViewById(R.id.final_price_txt);
            mImgProduct = itemView.findViewById(R.id.product_img);
            mSpnCount = itemView.findViewById(R.id.count_spn);
            mBtnDelete = itemView.findViewById(R.id.delete_btn);


            mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeProduct();
                    setTxtTotalSumPrice();

                }
            });
        }

        private void removeProduct() {
            int currentIndex = getAdapterPosition();
            Product currentProduct = mOrderedProducts.get(currentIndex);
            mOrderedProducts.remove(currentProduct);
            ProductLab.getInstance().deleteOrderedProduct(currentProduct);
            mAdapter.setProducts(mOrderedProducts);
            mAdapter.notifyItemChanged(currentIndex);

            mCalculatedTotalPrices[currentIndex] = 0;
            mTotalPrices[currentIndex] = 0;
            checkOrderedProductsSize();
        }

        private void setUpSpinner(final Product product, Spinner spnCount, final int position) {
            mProductCounts = getSpinnerItems();
            int productCount = product.getProductCount();
            ArrayAdapter countAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mProductCounts);
            countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCount.setAdapter(countAdapter);
            //ProductCount starts from 1 while  spnCount Items starts from 0
            spnCount.setSelection(--productCount);
            spnCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    setUniqueCalculatedTotalPrice(mProductCounts.get(i), position);
                    setTxtTotalSumPrice();

                    String rawPrice = product.getPrice();
                    int productCount = ++i;//ProductCount starts from 1 while  spnCount Items starts from 0

                    String totalPrice = (Integer.parseInt(rawPrice) * productCount) + "";
                    String finalPrice = PriceUtils.getCurrencyFormat(totalPrice, getActivity());

                    mTxtFinalPrice.setText(finalPrice);
                    product.setProductCount(productCount);//ProductCount starts from 1 while  spnCount Items starts from 0
                    ProductLab.getInstance().insertShoppingCart(product);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        public void bind(Product product, int position) {

            String title = product.getName();
            String rawPrice = product.getPrice();
            String formattedTotalPrice = PriceUtils.getCurrencyFormat(rawPrice, getActivity());
            int productCount = product.getProductCount();
            String rawFinalPrice = (Integer.parseInt(rawPrice) * productCount)+"";
            String formattedFinalInt = PriceUtils.getCurrencyFormat(rawFinalPrice, getActivity());

            mTxtTitle.setText(title);
            mTxtTotalPrice.setText(formattedTotalPrice);
            mTxtFinalPrice.setText(formattedFinalInt);
            setUpSpinner(product, mSpnCount, position);
            String imageUrl = product.getFirstImgUrl();

            if (imageUrl != null) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.shop_placeholder)
                        .into(mImgProduct);
            }
        }
    }


    private class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductHolder> {


        private List<Product> mProducts;

        public OrderedProductAdapter(List<Product> products) {
            mProducts = products;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public OrderedProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_cart_item, parent, false);
            return new OrderedProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderedProductHolder holder, int position) {
            holder.bind(mProducts.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }
}
