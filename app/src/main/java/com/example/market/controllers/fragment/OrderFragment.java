package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.model.Billing;
import com.example.market.model.Customer;
import com.example.market.model.repositories.CustomerLab;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Order;
import com.example.market.model.OrderJsonBody;
import com.example.market.model.repositories.OrderLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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
            mEdtCity, mEdtAddress, mEdtPostCode;

    private MaterialCardView mBtnOrder;
    private Call<Customer> mCallSendOrder;
    private LoadingCallBack mLoadingCallBack;
    private CallBacks mCallBacks;

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
        setListeners();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
        if (context instanceof CallBacks)
            mCallBacks= (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoadingCallBack = null;
        mCallBacks=null;
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

    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {
        mBtnOrder.setOnClickListener(this);
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
        CustomerLab customerLab=CustomerLab.getInstance(getActivity());
        int customer_id=customerLab.getCustomerId();
        Billing billing = new Billing(customer_id,firstName, lastName, address, city, postCode, country,phone);
        ArrayList<Order> orders = (ArrayList<Order>) orderLab.getOrders();

        mLoadingCallBack.showLoading();
        OrderJsonBody orderJsonBody=new OrderJsonBody(billing,orders,customer_id);
        mCallSendOrder = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class).sendOrder(orderJsonBody);

        mCallSendOrder.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {

                if (response.isSuccessful()) {
                    if (getActivity() != null) {
                        mLoadingCallBack.hideLoading();
                        String message=getString(R.string.order_sended_text_1)+
                                response.body().get_id()+
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
    }


    public interface CallBacks
    {
        void goToShoppingCart();
    }
}
