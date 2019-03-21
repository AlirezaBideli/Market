package com.example.market.controllers.fragment;


import android.content.Context;
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
import com.example.market.model.CustomerLab;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.OrderCalllBack;
import com.example.market.model.OrderLab;
import com.example.market.model.Product;
import com.example.market.model.RegisterCallBack;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.example.market.utils.PriceUtils;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends ParentFragment implements View.OnClickListener {


    public static final int ITEM_COUNT = 5;
    private static final boolean IS_NEW_LIST = false;
    private static int mTotalFinalPrice = 0;
    //CallBack
    private RegisterCallBack mRegisterCallBack;
    private OrderCalllBack mOrderCalllBack;
    //Widgets Variables
    private RecyclerView mRecyOrdered;
    private TextView mTxtTotalPriceSum;
    private Button mBtnOrder;
    private TextView mTxtNotFound;
    //SimpleVariables
    private List<Product> mOrderedProducts;
    private List<Order> mOrders;
    private int[] mTotalPrices;
    private int[] mCalculatedTotalPrices;
    private OrderedProductAdapter mAdapter;
    private Call<List<Product>> mOrdersCall;
    private LoadingCallBack mLoadingCallBack;
    private OrderLab mOrderLab;
    private int[] mProductCounts;
    private List<Integer> mSpinnerItems;
    private List<Integer> mOrderCounts;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableInit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
        if (context instanceof RegisterCallBack)
            mRegisterCallBack = (RegisterCallBack) context;
        if (context instanceof OrderCalllBack)
            mOrderCalllBack = (OrderCalllBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoadingCallBack = null;
        mRegisterCallBack = null;
        mOrderCalllBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_shoping_cart, container, false);
        findViewByIds(view);
        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderedProducts();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTotalFinalPrice = 0;
        if (mOrdersCall != null)
            mOrdersCall.cancel();
    }


    private void setUpRecyclerView() {

        mAdapter = new OrderedProductAdapter(mOrderedProducts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //Reverse recyclerView because the first orders placed at the end
        mRecyOrdered.setHasFixedSize(true);
        mRecyOrdered.setLayoutManager(layoutManager);
        mRecyOrdered.setAdapter(mAdapter);
    }

    private void getOrderedProducts() {
        mLoadingCallBack.showLoading();
        String ids = mOrderLab.getOrderIds();
        mOrdersCall = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getOrders(ids);

        if (ids.isEmpty()) {
            mTxtNotFound.setVisibility(View.VISIBLE);
            mLoadingCallBack.hideLoading();
            mBtnOrder.setEnabled(false);
        } else
            mOrdersCall.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                    if (getActivity() != null) {
                        if (response.isSuccessful()) {
                            mOrderedProducts = response.body();

                            if (mOrderedProducts.size() > 0) {
                                Collections.reverse(mOrderedProducts);
                                setUpRecyclerView();
                                setTotalPrices();
                                //fill TxtTotalSumPrice according to mBoughProductList prices
                                setTxtTotalSumPrice();
                                checkOrderedProductsSize();
                                mLoadingCallBack.hideLoading();
                            } else {
                                mTxtNotFound.setVisibility(View.VISIBLE);
                                mLoadingCallBack.hideLoading();
                            }
                        } else {
                            NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    if (getActivity() != null)
                        NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                }
            });


    }

    @Override
    protected void findViewByIds(View view) {
        mRecyOrdered = view.findViewById(R.id.recy_bought_products);
        mTxtTotalPriceSum = view.findViewById(R.id.total_price_txt);
        mBtnOrder = view.findViewById(R.id.comfirm_order_btn);
        mTxtNotFound = view.findViewById(R.id.txt_not_found);
    }


    @Override
    protected void variableInit() {
        mOrderLab = OrderLab.getInstance(getActivity());
        mOrders = mOrderLab.getOrders();
        mOrderedProducts = new ArrayList<>();
        mOrderCounts = mOrderLab.getOrderCounts();

    }

    @Override
    protected void setListeners() {

        mBtnOrder.setOnClickListener(this);
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
            Double p = (Double.parseDouble(mOrderedProducts.get(i).getPrice()));
            price = p.intValue();
            mTotalPrices[i] = price;
            mCalculatedTotalPrices[i] = price;
        }
    }

    private void setUniqueCalculatedTotalPrice(int productCount, int productPosition) {
        int index = productPosition;
        mCalculatedTotalPrices[index] = mTotalPrices[index] * productCount;
    }


    private void checkOrderedProductsSize() {
        if (mOrderedProducts.size() == 0)
            mBtnOrder.setEnabled(false);
        else
            mBtnOrder.setEnabled(true);
    }

    private int getCorrectPrice(String rawPrice) {
        Double p = (Double.parseDouble(rawPrice));
        return p.intValue();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comfirm_order_btn:

                CustomerLab customerLab = CustomerLab.getInstance(getActivity());
                if (customerLab.isUserRegistered())
                    mOrderCalllBack.showOrderPage();
                else
                    mRegisterCallBack.showRegisterPage();
                break;
        }
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
                    int orderPosition = getAdapterPosition();
                    Long productId = mOrders.get(orderPosition).get_id();
                    int count = mOrders.get(orderPosition).getCount();
                    Order order = new Order();
                    order.set_id(productId);
                    order.setCount(count);

                    removeProduct(order, orderPosition);
                    setTxtTotalSumPrice();

                }
            });
        }

        private void removeProduct(Order order, int orderPosition) {
            Product currentProduct = mOrderedProducts.get(orderPosition);
            mOrderedProducts.remove(currentProduct);
            mOrderLab.deleteOrderedProduct(order);
            mAdapter.setProducts(mOrderedProducts);
            mAdapter.notifyItemRemoved(orderPosition);
            mCalculatedTotalPrices[orderPosition] = 0;
            mTotalPrices[orderPosition] = 0;
            checkOrderedProductsSize();
        }

        private void setUpSpinner(final Product product, Spinner spnCount, int itemPosition) {
            mSpinnerItems = getSpinnerItems();
            int productCount = mOrderCounts.get(itemPosition);
            ArrayAdapter countAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mSpinnerItems);
            countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCount.setAdapter(countAdapter);
            //ProductCount starts from 1 while  spnCount Items starts from 0
            spnCount.setSelection(--productCount);
            setSpinnerListener(product, spnCount, itemPosition);

        }

        private void setSpinnerListener(final Product product, Spinner spnCount, final int position) {
            spnCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    setUniqueCalculatedTotalPrice(mSpinnerItems.get(i), position);
                    setTxtTotalSumPrice();
                    String rawPrice = product.getPrice();
                    int productCount = ++i;//ProductCount starts from 1 while  spnCount Items starts from 0
                    int price = getCorrectPrice(rawPrice);
                    String totalPrice = (price * productCount) + "";
                    String finalPrice = PriceUtils.getCurrencyFormat(totalPrice, getActivity());
                    mTxtFinalPrice.setText(finalPrice);

                    Order order = new Order();
                    order.set_id((long) product.getId());
                    order.setCount(productCount);
                    mOrderLab.insertToShoppingCart(order);
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
            int productCount = mOrderCounts.get(position);
            int price = getCorrectPrice(rawPrice);
            String rawFinalPrice = (price * productCount) + "";
            String formattedFinalInt = PriceUtils.getCurrencyFormat(rawFinalPrice, getActivity());

            mTxtTitle.setText(title);
            mTxtTotalPrice.setText(formattedTotalPrice);
            mTxtFinalPrice.setText(formattedFinalInt);
            setUpSpinner(product, mSpnCount, position);


            String imageUrl = null;
            if (product.getImages() != null)
                imageUrl = product.getImages().get(0).getSrc();

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
