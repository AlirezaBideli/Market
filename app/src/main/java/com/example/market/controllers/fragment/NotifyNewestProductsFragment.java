package com.example.market.controllers.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.prefs.AlarmManagerPrefs;
import com.example.market.prefs.NotifyHourPrefs;
import com.example.market.services.NotifyNewestService;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyNewestProductsFragment extends Fragment implements View.OnClickListener {

    public static final String HOUR_KEY = "hour_key";
    public static final int CUSTOM_HOUR_REQ_CODE = 0;
    public static final int DEFAULT_HOUR_REQ_CODE = 1;
    public static final int EMPTY = 0;
    private static final int CHIP_COUNT = 4;
    private static final String TAG_SELECT_HOUR = "tag_select_hour";
    private static final String TAG_SELECT_CUSTOM_HOUR = "tag_select_custom_hour";
    private static final int DEFAULT_HOUR = 12;
    private ConstraintLayout mNotifyItem;
    private ConstraintLayout mSelectHourItem;
    private LinearLayout mCustomHourItem;
    private TextView mTvHour;
    private RecyclerView mSettingRecy;
    private Switch mSwhEnable;
    private String mHourText;


    public NotifyNewestProductsFragment() {
        // Required empty public constructor
    }

    public static NotifyNewestProductsFragment newInstance() {

        Bundle args = new Bundle();

        NotifyNewestProductsFragment fragment = new NotifyNewestProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify_newest_products, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNotifyItem = view.findViewById(R.id.item_notification);
        mSelectHourItem = view.findViewById(R.id.item_selectHour);
        mCustomHourItem = view.findViewById(R.id.item_custom_hour);
        mSwhEnable = view.findViewById(R.id.swh_enable);
        mTvHour = view.findViewById(R.id.tv_notify_hour);

        mNotifyItem.setOnClickListener(this);
        mSelectHourItem.setOnClickListener(this);
        mCustomHourItem.setOnClickListener(this);
        mSwhEnable.setOnClickListener(this);

        initializeSwitchEnabled();

    }

    private void initializeTvHour() {
        NotifyHourPrefs np = NotifyHourPrefs.getInstance();
        int hour = np.retriveHour();
        if (hour == EMPTY) {
            mHourText = getString(R.string.default_notify_hour);
            mTvHour.setText(mHourText);
            np.storeHour(DEFAULT_HOUR);
        } else {
            mHourText = formatHourText(hour);
            mTvHour.setText(mHourText);
        }
    }


    private void enableNotifyByItem() {

        boolean isChecked = !mSwhEnable.isChecked();
        mSwhEnable.setChecked(isChecked);
        hideItems(isChecked);

    }

    private void enableNotifyBySwitch() {

        boolean isChecked = mSwhEnable.isChecked();
        AlarmManagerPrefs.setAlarmOn(getActivity(),isChecked);
        hideItems(isChecked);

    }

    private void initializeSwitchEnabled() {
        boolean isEnabled = AlarmManagerPrefs.isAlarmOn(getActivity());
        if (isEnabled)
            initializeTvHour();
        mSwhEnable.setChecked(isEnabled);
        hideItems(isEnabled);
    }

    private void hideItems(boolean isEnabled) {
        if (!isEnabled) {
            mCustomHourItem.setVisibility(View.GONE);
            mSelectHourItem.setVisibility(View.GONE);
        } else {
            mCustomHourItem.setVisibility(View.VISIBLE);
            mSelectHourItem.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.item_notification:
                enableNotifyByItem();
                schedulAlarmOn();
                break;
            case R.id.swh_enable:
                enableNotifyBySwitch();
                schedulAlarmOn();
                break;
            case R.id.item_selectHour:
                showSelectDialog();
                break;
            case R.id.item_custom_hour:
                showSelectCustomHour();
                break;
        }
    }

    private void showSelectCustomHour() {
        SelectCustomHourDialog dialog = SelectCustomHourDialog.newInstance();
        dialog.setTargetFragment(NotifyNewestProductsFragment.this, CUSTOM_HOUR_REQ_CODE);
        dialog.show(getFragmentManager(), TAG_SELECT_CUSTOM_HOUR);
    }

    private void showSelectDialog() {
        SelectHourDialog dialog = SelectHourDialog.newInstance();
        dialog.setTargetFragment(NotifyNewestProductsFragment.this, DEFAULT_HOUR_REQ_CODE);
        dialog.show(getFragmentManager(), TAG_SELECT_HOUR);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CUSTOM_HOUR_REQ_CODE
                    ^ requestCode == DEFAULT_HOUR_REQ_CODE) {
                int notifyHour = data.getIntExtra(HOUR_KEY, DEFAULT_HOUR);
                mHourText = formatHourText(notifyHour);
                mTvHour.setText(mHourText);
            }


        }
    }


    private void schedulAlarmOn() {
        int hour = NotifyHourPrefs.getInstance().retriveHour();
        boolean isChecked = mSwhEnable.isChecked();

        NotifyNewestService.setServiceAlarm(getActivity(), isChecked,hour);
        AlarmManagerPrefs.setAlarmOn(getActivity(), isChecked);


    }

    private String formatHourText(int hour) {
        String formatedTvHourText = getString(R.string.notify_hour_text_part_1) + " " + hour + " " +
                getString(R.string.notify_hour_text_part_2);
        return formatedTvHourText;
    }
}
