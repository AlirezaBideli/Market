package com.example.market.controllers.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.prefs.NotifyHourPrefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


public class SelectCustomHourDialog extends DialogFragment {

    public static final int MIN_HOUR = 1;
    public static final int MAX_HOUR = 24;
    private static int sHour;
    private TextView mTvCustomHour;
    private NumberPicker mHourPicker;

    public SelectCustomHourDialog() {
        // Required empty public constructor
    }


    public static SelectCustomHourDialog newInstance() {

        Bundle args = new Bundle();

        SelectCustomHourDialog fragment = new SelectCustomHourDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_custom_hour_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvCustomHour = view.findViewById(R.id.tv_custom_hour);
        mHourPicker = view.findViewById(R.id.picker_hour);

        mHourPicker.setMinValue(MIN_HOUR);
        mHourPicker.setMaxValue(MAX_HOUR);
        mHourPicker.setWrapSelectorWheel(true);
        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                mTvCustomHour.setText(newValue + "");
                sHour = newValue;
            }
        });


    }

    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra(NotifyNewestProductsFragment.HOUR_KEY, sHour);
        Fragment fragment = getTargetFragment();
        if (fragment != null)
            fragment.onActivityResult(NotifyNewestProductsFragment.CUSTOM_HOUR_REQ_CODE,
                    Activity.RESULT_OK, intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        sendResult();
        NotifyHourPrefs.getInstance().storeHour(sHour);
    }
}
