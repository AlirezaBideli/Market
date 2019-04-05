package com.example.market.controllers.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.market.R;
import com.example.market.prefs.NotifyHourPrefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


public class SelectHourDialog extends DialogFragment implements View.OnClickListener {


    private RadioButton mRd3Hour;
    private RadioButton mRd5Hour;
    private RadioButton mRd8Hour;
    private RadioButton mRd12Hour;
    private int sHour;
    private NotifyHourPrefs mNotifyHourPrefs;


    public SelectHourDialog() {
        // Required empty public constructor
    }

    public static SelectHourDialog newInstance() {

        Bundle args = new Bundle();

        SelectHourDialog fragment = new SelectHourDialog();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotifyHourPrefs = NotifyHourPrefs.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_hour_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRd3Hour = view.findViewById(R.id.rd_hours_3);
        mRd5Hour = view.findViewById(R.id.rd_hours_5);
        mRd8Hour = view.findViewById(R.id.rd_hours_8);
        mRd12Hour = view.findViewById(R.id.rd_hours_12);

        mRd3Hour.setOnClickListener(this);
        mRd5Hour.setOnClickListener(this);
        mRd8Hour.setOnClickListener(this);
        mRd12Hour.setOnClickListener(this);

        initializeHourSelected();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rd_hours_3:
                sHour = Hour.HOUR_3;
                break;
            case R.id.rd_hours_5:
                sHour = Hour.HOUR_5;
                break;
            case R.id.rd_hours_8:
                sHour = Hour.HOUR_8;
                break;
            case R.id.rd_hours_12:
                sHour = Hour.HOUR_12;
                break;
        }

    }

    private void initializeHourSelected() {

        //initialize previous hour has selected by user before
        int hour = mNotifyHourPrefs.retriveHour();

        switch (hour) {
            case Hour.HOUR_3:
                setNotifyHour(mRd3Hour, Hour.HOUR_3);
                break;
            case Hour.HOUR_5:
                setNotifyHour(mRd5Hour, Hour.HOUR_5);
                break;
            case Hour.HOUR_8:
                setNotifyHour(mRd8Hour, Hour.HOUR_8);
                break;
            case Hour.HOUR_12:
                setNotifyHour(mRd12Hour, Hour.HOUR_12);
                break;
        }

    }

    private void setNotifyHour(RadioButton item, int hour) {
        item.setChecked(true);
        sHour = hour;
    }


    @Override
    public void onPause() {
        super.onPause();
        mNotifyHourPrefs.storeHour(sHour);
        sendResult();
   }


    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra(NotifyNewestProductsFragment.HOUR_KEY, sHour);
        Fragment fragment = getTargetFragment();
        if (fragment != null)
            fragment.onActivityResult(NotifyNewestProductsFragment.CUSTOM_HOUR_REQ_CODE,
                    Activity.RESULT_OK, intent);
    }
    private static class Hour {
        private static final int HOUR_3 = 3;
        private static final int HOUR_5 = 5;
        private static final int HOUR_8 = 8;
        private static final int HOUR_12 = 12;
    }
}
