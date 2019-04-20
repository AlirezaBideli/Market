package com.example.market.controllers.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.market.R;
import com.example.market.model.Billing;
import com.example.market.model.repositories.BillingLab;
import com.example.market.utils.ActivityHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingFragment extends Fragment implements View.OnClickListener {


    public static final String EXTRA_LOCATION = "com.example.market.controllers.fragment.location";
    public static final String EXTRA_ADDRESS = "com.example.market.controllers.fragment.address";
    private static final int REQ_MAPS = 0;
    private static final String ARG_FILL = "arg_fill";
    private static final String ARG_ID = "arg_id";
    private static final String TAG = "BillingFragmentTag";

    private CallBacks mCallBacks;
    private TextInputEditText mEdtFirstName, mEdtLastName, mEdtPhone,
            mEdtCity, mEdtState, mEdtAddress, mEdtPostCode;
    private MaterialCardView mBtnSetBilling;

    private FrameLayout mItemSelectAddress;
    private String mAddress_2;
    private String mLocationLat="0";
    private String mLocationLng="0";
    private boolean mIsFormOpenedForEditing;
    private long mBillingId;


    public static BillingFragment newInstance(boolean fillForm, long billingId) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_FILL, fillForm);
        args.putLong(ARG_ID, billingId);
        BillingFragment fragment = new BillingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BillingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CallBacks)
            mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallBacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_billing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEdtFirstName = view.findViewById(R.id.edt_first_name);
        mEdtLastName = view.findViewById(R.id.edt_last_name);
        mEdtPhone = view.findViewById(R.id.edt_phone);
        mEdtCity = view.findViewById(R.id.edt_city);
        mEdtPostCode = view.findViewById(R.id.edt_post_code);
        mEdtAddress = view.findViewById(R.id.edt_address);
        mEdtState = view.findViewById(R.id.edt_state);
        mItemSelectAddress = view.findViewById(R.id.item_select_address);
        mBtnSetBilling = view.findViewById(R.id.btn_billing);
        mItemSelectAddress.setOnClickListener(this);
        mBtnSetBilling.setOnClickListener(this);
        fillFrom();

    }


    private boolean warnUserForInputs() {

        boolean result = true;
        String error;
        TextInputEditText[] editTextList = {mEdtFirstName, mEdtLastName,
                mEdtPhone, mEdtCity, mEdtState, mEdtPostCode, mEdtAddress};
        for (int i = 0; i < editTextList.length; i++) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;


        if (requestCode == REQ_MAPS) {
            LatLng userLocation = data.getParcelableExtra(EXTRA_LOCATION);
            mLocationLat = userLocation.latitude + "";
            mLocationLng = userLocation.longitude + "";
            Log.i(TAG,"latitude= "+mLocationLat+" & longitude= "+mLocationLng);
            mAddress_2 = data.getStringExtra(EXTRA_ADDRESS);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.item_select_address:
                showGoogleMap();
                break;
            case R.id.btn_billing:
                createBillingAndExit();
                break;
        }
    }

    private void createBillingAndExit() {

        boolean isValid = warnUserForInputs();

        if (isValid) {
            BillingLab billingLab = BillingLab.getInstance();

            Billing billing = getNewBilling();

            List<Billing> previousBillings = billingLab.getBillings();
            if (previousBillings.size() == 0)
                billing.setIsSelected(true);

            if (mIsFormOpenedForEditing) {
                billing.set_id(mBillingId);
                billing.setmLatitude(mLocationLat);
                billing.setmLongitude(mLocationLng);
                billingLab.updateBilling(billing);
            } else {
                billingLab.addBilling(billing);
            }


        }


        //Refresh OrderFragment
        mCallBacks.refreshOrderFragment();
        //Exit
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private Billing getNewBilling() {
        String firstName = mEdtFirstName.getText().toString();
        String lastName = mEdtLastName.getText().toString();
        String phone = mEdtPhone.getText().toString();
        String state = mEdtState.getText().toString();
        String city = mEdtCity.getText().toString();
        String postalCode = mEdtPostCode.getText().toString();
        String address_1 = mEdtAddress.getText().toString();
        boolean isSelected = false;

        Billing billing = new Billing(firstName, lastName, address_1
                , mAddress_2, city, postalCode, phone, state, isSelected,
                mLocationLat, mLocationLng);
        return billing;
    }

    private void showGoogleMap() {
        double lattitude=Double.parseDouble(mLocationLat);
        double longitude=Double.parseDouble(mLocationLng);
        LatLng latLng=new LatLng(lattitude,longitude);
        Intent intent = ActivityHelper.Intent_MapsActivity(getActivity(),latLng);
        startActivityForResult(intent, REQ_MAPS);

    }

    private void fillFrom() {
        mIsFormOpenedForEditing = getArguments().getBoolean(ARG_FILL);
        mBillingId = getArguments().getLong(ARG_ID);
        if (mIsFormOpenedForEditing) {
            BillingLab billingLab = BillingLab.getInstance();
            Billing billing = billingLab.getUniqueBilling(mBillingId);

            String firstName = billing.getMFirstName();
            String lastName = billing.getMLastName();
            String phone = billing.getMPhone();
            String state = billing.getMState();
            String city = billing.getMCity();
            String postalCode = billing.getMPostCode();
            String address_1 = billing.getMAddress_1();

            mLocationLng=billing.getmLongitude();
            mLocationLat=billing.getmLatitude();

            mEdtFirstName.setText(firstName);
            mEdtLastName.setText(lastName);
            mEdtAddress.setText(address_1);
            mEdtPhone.setText(phone);
            mEdtPostCode.setText(postalCode);
            mEdtState.setText(state);
            mEdtCity.setText(city);
        }


    }

    public interface CallBacks {
        void refreshOrderFragment();
    }


}
