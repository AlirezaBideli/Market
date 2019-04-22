package com.example.market.controllers.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.example.market.R;
import com.example.market.adapters.BillingAdapter;
import com.example.market.model.Billing;
import com.example.market.model.Coupon;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.OrderJsonBody;
import com.example.market.model.repositories.BillingLab;
import com.example.market.model.repositories.CustomerLab;
import com.example.market.model.repositories.OrderLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.PriceUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends ParentFragment implements View.OnClickListener{


    public static final int SLEEP_TIME = 2000;
    private TextInputEditText mEdtCoupons;
    private TextInputLayout mInputCoupons;
    private TextView mTvSubmitCopouns;
    private TextView mTVFinalPrice;
    private Switch mSwhCoupons;
    private LottieAnimationView mCouponProcessingLoading;
    private MaterialCardView mBtnOrder;
    private RecyclerView mRecyBilling;
    private Call<Order> mCallSendOrder;
    private LoadingCallBack mLoadingCallBack;
    private CallBacks mCallBacks;
    private Call<List<Coupon>> mCallCheckCoupon;
    private OrderLab mOrderLab;
    private long mTotalPrice;
    private Coupon mFoundedCoupon;
    private LottieAnimationView mSelectAddressLoading;
    public static final int PLACE_PICKER_REQUEST = 1;
    private String TAG="OrderFragmentTag";


    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {

        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {


        List<Billing> billingsList = BillingLab.getInstance().getBillings();

        BillingAdapter adapter = new BillingAdapter(billingsList,
                getActivity(), mCallBacks, mRecyBilling);

        mRecyBilling.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyBilling.setHasFixedSize(true);
        mRecyBilling.setAdapter(adapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
        if (context instanceof CallBacks)
            mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoadingCallBack = null;
        mCallBacks = null;
    }

    @Override
    protected void findViewByIds(View view) {

        mBtnOrder = view.findViewById(R.id.billing_btn);
        mTvSubmitCopouns = view.findViewById(R.id.tv_submit_coumpons);
        mInputCoupons = view.findViewById(R.id.textInputLayout6);
        mEdtCoupons = view.findViewById(R.id.edt_coupons);
        mSwhCoupons = view.findViewById(R.id.swh_coupons);
        mTVFinalPrice = view.findViewById(R.id.tv_final_price);
        mCouponProcessingLoading = view.findViewById(R.id.loading_orderF);
        mRecyBilling = view.findViewById(R.id.recy_billing);
        mSelectAddressLoading=view.findViewById(R.id.loading_order);
    }

    @Override
    protected void variableInit() {

        mOrderLab = OrderLab.getInstance(getActivity());
        String totalPrice = mOrderLab.getTotalPrice() + "";
        String formattedPrice = PriceUtils.getCurrencyFormat(totalPrice, getActivity());
        mTVFinalPrice.setText(formattedPrice);

        BillingLab billingLab = BillingLab.getInstance();
        int size = billingLab.getBillings().size();


        if (size == 0) {
            mBtnOrder.setBackgroundColor(Color.GRAY);
            mBtnOrder.setEnabled(false);
        }


    }

    @Override
    protected void setListeners() {
        mBtnOrder.setOnClickListener(this);
        mTvSubmitCopouns.setOnClickListener(this);
        mSwhCoupons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    mInputCoupons.setVisibility(View.VISIBLE);
                    mCouponProcessingLoading.setVisibility(View.INVISIBLE);
                    mTvSubmitCopouns.setVisibility(View.VISIBLE);
                } else {
                    mInputCoupons.setVisibility(View.GONE);
                    mTvSubmitCopouns.setVisibility(View.GONE);
                    mCouponProcessingLoading.setVisibility(View.GONE);

                }

            }
        });
    }

    private void getAllCoupons() {

        mCallCheckCoupon = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getAllCoupons();

        mCallCheckCoupon.enqueue(new Callback<List<Coupon>>() {
            @Override
            public void onResponse(Call<List<Coupon>> call, Response<List<Coupon>> response) {
                if (response.isSuccessful()) {
                    List<Coupon> coupons = response.body();
                    checkCoupon(coupons);
                }
            }

            @Override
            public void onFailure(Call<List<Coupon>> call, Throwable t) {
            }
        });


    }

    private void checkCoupon(List<Coupon> coupons) {
        int couponsSize = coupons.size();
        String userCoupon = mEdtCoupons.getText().toString();
        String apiCouponCode;
        boolean found = false;

        for (int i = 0; i < couponsSize; i++) {
            apiCouponCode = coupons.get(i).getCode();
            if (apiCouponCode.equals(userCoupon)) {
                mFoundedCoupon = coupons.get(i);
                setDiscount(mFoundedCoupon);
                found = true;

                break;
            }
        }
        if (!found) {
            String message = getString(R.string.invalide_coupon_text);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        }

        mCouponProcessingLoading.setVisibility(View.GONE);
        mTvSubmitCopouns.setVisibility(View.VISIBLE);


    }


    private void setDiscount(Coupon coupon) {
        mTotalPrice = mOrderLab.getTotalPrice();
        int couponAmount = coupon.getAmount();
        String finalTotalPrice = mTotalPrice - (mTotalPrice * couponAmount / 100) + "";
        String formattedPrice = PriceUtils.getCurrencyFormat(finalTotalPrice, getActivity());
        mTVFinalPrice.setText(formattedPrice);

    }


    private void getOrdersFromApi(final OrderLab orderLab, OrderJsonBody orderJsonBody) {
        mCallSendOrder = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class).sendOrder(orderJsonBody);

        mCallSendOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                mLoadingCallBack.hideLoading();



                if (response.isSuccessful()) {
                    if (getActivity() != null) {

                        String message = getString(R.string.order_sended_text_1);

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        orderLab.deleteAllOrders();
                        mCallBacks.goToShoppingCart();
                    }

                }

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                mLoadingCallBack.hideLoading();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.billing_btn:
                sendOrderToApi();
                break;
            case R.id.tv_submit_coumpons:
                submitCompound();
                break;


        }
    }


    private void sendOrderToApi() {


        OrderLab orderLab = OrderLab.getInstance(getActivity());
        CustomerLab customerLab = CustomerLab.getInstance(getActivity());
        BillingLab billingLab = BillingLab.getInstance();
        int customer_id = customerLab.getCustomerId();

        ArrayList<Order> orders = (ArrayList<Order>) orderLab.getOrders();

        Billing selectedBilling = billingLab.getSelectedBilling();
        mLoadingCallBack.showLoading();


        OrderJsonBody orderJsonBody = new OrderJsonBody(selectedBilling,
                orders, mFoundedCoupon, customer_id);
        getOrdersFromApi(orderLab, orderJsonBody);


    }

    private void submitCompound() {
        String coupon = mEdtCoupons.getText().toString();
        if (!coupon.isEmpty()) {
            mCouponProcessingLoading.setVisibility(View.VISIBLE);
            mTvSubmitCopouns.setVisibility(View.INVISIBLE);
            getAllCoupons();
        } else {
            String error = getString(R.string.coupons_edt_hint);
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mCallSendOrder != null)
            mCallSendOrder.cancel();
        if (mCallCheckCoupon != null)
            mCallCheckCoupon.cancel();
    }

    public void updateAddressSelectionRecy() {

            mRecyBilling.getAdapter().notifyDataSetChanged();
            mRecyBilling.scrollToPosition(0);
    }


    public interface CallBacks {
        void goToShoppingCart();

        void goToBillingForm(boolean isEdit, long billingId);
    }


    public void updateAddressRecyelrView()
    {
        mSelectAddressLoading.setVisibility(View.VISIBLE);
        mRecyBilling.setVisibility(View.GONE);
        mRecyBilling.getAdapter().notifyDataSetChanged();
        mRecyBilling.getLayoutManager().scrollToPosition(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSelectAddressLoading.setVisibility(View.INVISIBLE);
        mRecyBilling.setVisibility(View.VISIBLE);



    }
}
