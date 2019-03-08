package com.example.market.controllers.fragmnet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.market.R;
import com.example.market.model.SortType;

import androidx.fragment.app.DialogFragment;


public class SortDialogFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener {


    public static int mRadioCheckedPosition;
    public static boolean misFirstTime;
    private RadioButton mRadioBestSellers, mRadioPriceDec, mRadioPriceAsc, mRadioNewest;
    private RadioGroup mSortRadioGroup;
    private CallBacks mCallBacks;


    public SortDialogFragment() {
        // Required empty public constructor
    }

    public static SortDialogFragment newInstance() {

        Bundle args = new Bundle();

        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBacks)
            mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT
                        , WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort_dialog, container, false);
        findViewbyId(view);
        checkLastCheckedRadio();
        mSortRadioGroup.setOnCheckedChangeListener(this);

        return view;
    }


    private void checkLastCheckedRadio() {
        switch (mRadioCheckedPosition) {
            case 3:
                mRadioNewest.setChecked(true);
                break;
            case 2:
                mRadioPriceAsc.setChecked(true);
                break;
            case 1:
                mRadioPriceDec.setChecked(true);
                break;
            case 0:
                mRadioBestSellers.setChecked(true);
                break;
        }
        misFirstTime = false;
    }

    private void findViewbyId(View view) {
        mSortRadioGroup = view.findViewById(R.id.sort_radioGroup);
        mRadioPriceAsc = view.findViewById(R.id.radio_price_asc);
        mRadioPriceDec = view.findViewById(R.id.radio_price_desc);
        mRadioNewest = view.findViewById(R.id.radio_newest);
        mRadioBestSellers = view.findViewById(R.id.radio_best_sellers);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (!misFirstTime) {
            switch (i) {
                case R.id.radio_best_sellers:
                    mRadioCheckedPosition = RadioPosition.BEST_SELLERS.getValue();
                    mRadioBestSellers.setChecked(true);
                    mCallBacks.sort(SortType.BEST_SELLERS);
                    dismiss();
                    break;
                case R.id.radio_newest:
                    mRadioCheckedPosition = RadioPosition.NEWEST.getValue();
                    mRadioNewest.setChecked(true);
                    mCallBacks.sort(SortType.NEWEST);
                    dismiss();
                    break;
                case R.id.radio_price_asc:
                    mRadioCheckedPosition = RadioPosition.PRICE_ASC.getValue();
                    mRadioPriceAsc.setChecked(true);
                    mCallBacks.sort(SortType.PRICE_ASC);
                    dismiss();
                    break;
                case R.id.radio_price_desc:
                    mRadioCheckedPosition = RadioPosition.PRICE_DESC.getValue();
                    mRadioPriceDec.setChecked(true);
                    mCallBacks.sort(SortType.PRICE_DESC);
                    dismiss();
                    break;

            }

        }



    }


    private enum RadioPosition {
        BEST_SELLERS(0),
        PRICE_DESC(1),
        PRICE_ASC(2),
        NEWEST(3);

        private int value;

        RadioPosition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface CallBacks {
        public void sort(SortType sortType);
    }
}
