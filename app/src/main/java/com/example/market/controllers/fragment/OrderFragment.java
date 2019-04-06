package com.example.market.controllers.fragment;


import android.content.Context;
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
import com.example.market.model.Billing;
import com.example.market.model.Coupon;
import com.example.market.model.Customer;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.OrderJsonBody;
import com.example.market.model.repositories.CustomerLab;
import com.example.market.model.repositories.OrderLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.example.market.utils.PriceUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends ParentFragment implements View.OnClickListener {


    public static final int EDITTEXT_COUNT = 7;
    private TextInputEditText mEdtFirstName, mEdtLastName, mEdtPhone, mEdtCountry,
            mEdtCity, mEdtAddress, mEdtPostCode, mEdtCoupons;
    private TextInputLayout mInputCoupons;
    private TextView mTvSubmitCopouns;
    private TextView mTVFinalPrice;
    private Switch mSwhCoupons;
    private LottieAnimationView mLoading;
    private MaterialCardView mBtnOrder;
    private Call<Customer> mCallSendOrder;
    private LoadingCallBack mLoadingCallBack;
    private CallBacks mCallBacks;
    private Call<List<Coupon>> mCallCheckCoupon;
    private OrderLab mOrderLab;
    private long mTotalPrice;
    private Coupon mFoundedCoupon;

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
        return view;
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
        mEdtFirstName = view.findViewById(R.id.edt_first_name);
        mEdtLastName = view.findViewById(R.id.edt_last_name);
        mEdtPhone = view.findViewById(R.id.edt_phone);
        mEdtCountry = view.findViewById(R.id.edt_country);
        mEdtCity = view.findViewById(R.id.edt_city);
        mEdtPostCode = view.findViewById(R.id.edt_post_code);
        mEdtAddress = view.findViewById(R.id.edt_address);
        mBtnOrder = view.findViewById(R.id.billing_btn);
        mTvSubmitCopouns = view.findViewById(R.id.tv_submit_coumpons);
        mInputCoupons = view.findViewById(R.id.textInputLayout6);
        mEdtCoupons = view.findViewById(R.id.edt_coupons);
        mSwhCoupons = view.findViewById(R.id.swh_coupons);
        mTVFinalPrice = view.findViewById(R.id.tv_final_price);
        mLoading = view.findViewById(R.id.loading_orderF);

    }

    @Override
    protected void variableInit() {

        mOrderLab = OrderLab.getInstance(getActivity());
        String totalPrice = mOrderLab.getTotalPrice() + "";
        String formattedPrice = PriceUtils.getCurrencyFormat(totalPrice, getActivity());
        mTVFinalPrice.setText(formattedPrice);
    }

    @Override
    protected void setListeners() {
        mBtnOrder.setOnClickListener(this);

        mTvSubmitCopouns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String coupon = mEdtCoupons.getText().toString();
                if (!coupon.isEmpty()) {
                    mLoading.setVisibility(View.VISIBLE);
                    mTvSubmitCopouns.setVisibility(View.INVISIBLE);
                    getAllCoupons();
                }
                else
                {
                    String error=getString(R.string.coupons_edt_hint);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }


            }
        });
        mSwhCoupons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    mInputCoupons.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.INVISIBLE);
                    mTvSubmitCopouns.setVisibility(View.VISIBLE);
                } else {
                    mInputCoupons.setVisibility(View.GONE);
                    mTvSubmitCopouns.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);

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

                } else if (getActivity() != null) {
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                }
            }

            @Override
            public void onFailure(Call<List<Coupon>> call, Throwable t) {
                if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());

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

        mLoading.setVisibility(View.GONE);
        mTvSubmitCopouns.setVisibility(View.VISIBLE);


    }


    private void setDiscount(Coupon coupon) {
        mTotalPrice = mOrderLab.getTotalPrice();
        int couponAmount = coupon.getAmount();
        String finalTotalPrice = mTotalPrice - (mTotalPrice * couponAmount / 100) + "";
        String formattedPrice = PriceUtils.getCurrencyFormat(finalTotalPrice, getActivity());
        mTVFinalPrice.setText(formattedPrice);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.billing_btn:
                sendOrderToApi();
                break;
        }
    }

    private void sendOrderToApi() {
        boolean isValid = warnUserForInputs();
        if (isValid) {
            sendOrder();
        } else {
            Toast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean warnUserForInputs() {

        boolean result = true;
        String error;
        TextInputEditText[] editTextList = {mEdtFirstName, mEdtLastName,
                mEdtPhone, mEdtCountry, mEdtCity, mEdtPostCode, mEdtAddress};
        for (int i = 0; i < EDITTEXT_COUNT; i++) {
            TextInputEditText editText = editTextList[i];
            error = getString(R.string.unique_input_error, editText.getHint());
            if (editText.getText().toString().isEmpty()) {
                result = false;
                editText.setError(error, null);
            }
        }


        return result;
    }

    private void sendOrder() {
        String firstName = mEdtFirstName.getText().toString();
        String lastName = mEdtLastName.getText().toString();
        String phone = mEdtPhone.getText().toString();
        String country = mEdtCountry.getText().toString();
        String city = mEdtCity.getText().toString();
        String postCode = mEdtPostCode.getText().toString();
        String address = mEdtAddress.getText().toString();


        final OrderLab orderLab = OrderLab.getInstance(getActivity());
        CustomerLab customerLab = CustomerLab.getInstance(getActivity());
        int customer_id = customerLab.getCustomerId();
        Billing billing = new Billing(customer_id, firstName, lastName, address, city, postCode, country, phone);
        ArrayList<Order> orders = (ArrayList<Order>) orderLab.getOrders();

        mLoadingCallBack.showLoading();
        OrderJsonBody orderJsonBody = new OrderJsonBody(billing, orders, mFoundedCoupon, customer_id);
        getOrdersFromApi(orderLab, orderJsonBody);

    }

    private void getOrdersFromApi(final OrderLab orderLab, OrderJsonBody orderJsonBody) {
        mCallSendOrder = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class).sendOrder(orderJsonBody);

        mCallSendOrder.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {

                if (response.isSuccessful()) {
                    if (getActivity() != null) {
                        mLoadingCallBack.hideLoading();
                        String message = getString(R.string.order_sended_text_1) +
                                response.body().get_id() +
                                getString(R.string.order_sended_text_2);

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        orderLab.deleteAllOrders();
                        mCallBacks.goToShoppingCart();
                    }

                } else if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCallSendOrder != null)
            mCallSendOrder.cancel();
        if (mCallCheckCoupon != null)
            mCallCheckCoupon.cancel();
    }


    public interface CallBacks {
        void goToShoppingCart();
    }
}
