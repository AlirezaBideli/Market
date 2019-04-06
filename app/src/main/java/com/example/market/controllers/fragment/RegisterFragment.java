package com.example.market.controllers.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.model.Customer;
import com.example.market.model.repositories.CustomerLab;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.OrderCallBack;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends ParentFragment implements View.OnClickListener {


    private static final int EDITTEXT_COUNT = 5;
    private static final String TAG = "RegisterFragmentTag";
    private TextInputEditText mEdtFirstName, mEdtLastName, mEdtUserName, mEdtPassword, mEdtEmail;
    private MaterialButton mBtnSubmit;
    private Call<Customer> mCallCustomer;
    private LoadingCallBack mLoadingCallBack;
    private OrderCallBack mOrderCallBack;
    private CustomerLab mCustomerLab;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
        if (context instanceof OrderCallBack)
            mOrderCallBack = (OrderCallBack) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoadingCallBack = null;
        mOrderCallBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findViewByIds(view);
        setListeners();
        return view;
    }

    @Override
    protected void findViewByIds(View view) {

        mEdtFirstName = view.findViewById(R.id.edt_first_name);
        mEdtLastName = view.findViewById(R.id.edt_last_name);
        mEdtUserName = view.findViewById(R.id.edt_userName);
        mEdtPassword = view.findViewById(R.id.edt_password);
        mEdtEmail = view.findViewById(R.id.edt_email);
        mBtnSubmit = view.findViewById(R.id.register_btn);

    }

    @Override
    protected void variableInit() {

    }

    @Override
    protected void setListeners() {
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                submitCustomer();

                break;
        }
    }

    private void submitCustomer() {
        mCustomerLab = CustomerLab.getInstance(getActivity());
        boolean isValid = warnUserForInputs();
        if (isValid) {
            String firstName = mEdtFirstName.getText().toString();
            String lastName = mEdtLastName.getText().toString();
            String userName = mEdtUserName.getText().toString();
            String password = mEdtPassword.getText().toString();
            String email = mEdtEmail.getText().toString();
            Customer customer = new Customer(Customer.DEFAULT_CUSTOMER_ID,
                    firstName, lastName, userName
                    , password, email);
            mCustomerLab.storeCustomer(customer);
            createCustomer(firstName, lastName, userName, email);
            Log.i(TAG, email + "");


        } else {
            Toast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void createCustomer(String firstName, String lastName, String userName, String email) {

        mLoadingCallBack.showLoading();
        mCallCustomer = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class).createCustomer(firstName, lastName, userName, email);
        mCallCustomer.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    int id = response.body().get_id();
                    String customerName=response.body().getUserName();
                    mCustomerLab.storeCustomerId(id);
                    Log.i(TAG, response.body().get_id() + "");
                    mLoadingCallBack.hideLoading();
                    mOrderCallBack.showOrderPage();

                } else if (getActivity() != null) {
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                    Log.i(TAG, response.errorBody() + "");

                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                Log.i(TAG, t.getMessage());

            }
        });
    }

    private boolean warnUserForInputs() {

        boolean result = true;
        String error;
        TextInputEditText[] editTextList = {mEdtFirstName, mEdtLastName,
                mEdtUserName, mEdtPassword, mEdtEmail};
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

    @Override
    public void onPause() {
        super.onPause();
        if (mCallCustomer != null)
            mCallCustomer.cancel();
    }


    public interface CallBacks {
        void changeCustomerNameHeader(String CustomerName);
    }
}
