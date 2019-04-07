package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.repositories.OrderLab;
import com.example.market.model.Product;
import com.example.market.model.repositories.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.example.market.utils.PriceUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends ParentFragment implements View.OnClickListener {


    public static final int DEFAULT_COUNT = 1;
    //Argument Tags
    private static final String ARG_ID = "productId";
    //simple Variables
    private static final int DEFAULT_CHAR_COUNT = 300;
    private static final int WRAP_CONTENT_SIZE = -1;
    public static final String INVALID_PRICE = "0";
    //DetailCallBack
    private LoadingCallBack mLoadingCallBack;
    //CallBack
    private CallBacks mCallBacks;
    //Widgets Variables
    private TextView mTxtName, mTxtPrice, mTxtDesc;
    private MaterialButton mBtnDetail;
    private TextView mBtnRemain;
    private ViewPager mImgPager;
    private MaterialCardView mReviewLayout;
    private PageIndicatorView mIndicatorView;
    private MaterialButton mBtnReview;
    private View mSeperator;
    private Product mProduct;
    private int mId;
    private Call<Product> mCallProduct;
    private boolean mIsDescCompleted;


    //Widgets Variables
    private MaterialButton mBtnAddCart;


    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CallBacks)
            mCallBacks= (CallBacks) context;
        if (context instanceof  LoadingCallBack)
            mLoadingCallBack= (LoadingCallBack) context;


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
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();
        return view;
    }


    @Override
    protected void findViewByIds(View view) {

        mTxtName = view.findViewById(R.id.name_txt_productF);
        mTxtPrice = view.findViewById(R.id.price_txt_productF);
        mTxtDesc = view.findViewById(R.id.desc_txt_productF);
        mImgPager = view.findViewById(R.id.image_pager_productF);
        mBtnDetail = view.findViewById(R.id.detail_btn_productF);
        mBtnRemain = view.findViewById(R.id.text_remaining_ProductF);
        mReviewLayout = view.findViewById(R.id.review_layout_ProductF);
        mIndicatorView = view.findViewById(R.id.pageIndicatorView_ProductF);
        mSeperator = view.findViewById(R.id.separator_ProductF);
        mBtnAddCart = view.findViewById(R.id.btn_add_cart);
        mBtnReview=view.findViewById(R.id.btn_review);
    }

    @Override
    protected void variableInit() {
        getProducts();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCallProduct != null)
            mCallProduct.cancel();
    }

    private void getProducts() {
        if (mLoadingCallBack != null) {
            mId = getArguments().getInt(ARG_ID);
            Log.i(this.getClass().getSimpleName(),"Product Id: "+mId);
            mLoadingCallBack.showLoading();

            mCallProduct = RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                    .getProduct(mId);
            mCallProduct.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        if (mLoadingCallBack != null) {
                            mProduct = response.body();
                            if (mProduct != null) {
                                fillPage(mProduct);
                            }
                            mLoadingCallBack.hideLoading();
                        }
                    } else {
                        if (getActivity() != null)
                            NetworkConnection.warnConnection(getActivity(), getFragmentManager());

                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    if (getActivity() != null)
                        NetworkConnection.warnConnection(getActivity(), getFragmentManager());

                }
            });
        }
    }


    private void fillPage(Product product) {
        ProductLab.getInstance(getActivity()).setCurrentProduct(product);

        String productName = product.getName();
        String productDescription = product.getDescription();
        String productPrice = product.getPrice();
        String formatedPrice = PriceUtils.getCurrencyFormat(productPrice, getActivity());
        mTxtName.setText(productName);
        mTxtPrice.setText(formatedPrice);
        checkProductDesc(productDescription);
        mTxtDesc.setText(productDescription);

        if(productPrice.isEmpty())
            mBtnAddCart.setEnabled(false);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                String src = mProduct.getImages().get(position).getSrc();
                Picasso.get().load(src).into(imageView);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                if (mProduct.getImages() == null)
                    return 0;
                else return mProduct.getImages().size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
        mImgPager.setAdapter(pagerAdapter);


        //Sync PageIndicatorView with ViewPager
        mImgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setListeners() {

        mBtnDetail.setOnClickListener(this);
        mBtnRemain.setOnClickListener(this);
        mBtnAddCart.setOnClickListener(this);
        mBtnReview.setOnClickListener(this);
    }


    private void addToShoppingCart() {
        OrderLab orderLab = OrderLab.getInstance(getActivity());
        Order order = new Order();
        order.set_id((long) mProduct.getId());
        order.setCount(DEFAULT_COUNT);
        boolean isExsisted = orderLab.checkProductExist(order);
        if (isExsisted)
            Toast.makeText(getActivity(), R.string.shopping_cart_warning, Toast.LENGTH_SHORT).show();
        else {
            orderLab.insertToShoppingCart(order);
            mCallBacks.showShoppingCart();
        }
    }

    private void handleDescription() {
        ViewGroup.LayoutParams layoutParams = mTxtDesc.getLayoutParams();

        String buttonText;
        if (!mIsDescCompleted) {
            buttonText = getString(R.string.close);
            layoutParams.height = WRAP_CONTENT_SIZE;
            mTxtDesc.setLayoutParams(layoutParams);
            mBtnRemain.setText(buttonText);
            mIsDescCompleted = true;
        } else {

            buttonText = getString(R.string.text_remaining);
            int height = (int) getResources().getDimension(R.dimen.product_view);
            layoutParams.height = height;
            mTxtDesc.setLayoutParams(layoutParams);
            mBtnRemain.setText(buttonText);
            mIsDescCompleted = false;
        }
    }

    private void checkProductDesc(String review) {
        if (review.isEmpty())
            mReviewLayout.setVisibility(View.INVISIBLE);
        else if (review.length() <= DEFAULT_CHAR_COUNT) {
            ViewGroup.LayoutParams layoutParams = mTxtDesc.getLayoutParams();
            layoutParams.height = WRAP_CONTENT_SIZE;
            mBtnRemain.setVisibility(View.INVISIBLE);
            mSeperator.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.detail_btn_productF:
                mCallBacks.showDetails();
                break;
            case R.id.btn_add_cart:
                addToShoppingCart();
                break;
            case R.id.text_remaining_ProductF:
                handleDescription();
                break;
            case R.id.btn_review:
                int productId=mProduct.getId();
                mCallBacks.showReviews(productId);
                break;

        }
    }

    public interface CallBacks {
        void showDetails();

        void showShoppingCart();

        void showReviews(int productId);
    }
}
