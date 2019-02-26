package com.example.market.controllers.fragmnet;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.market.R;
import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionDialog extends DialogFragment  {


    private MaterialButton mBtnTryAgain;
    private CallBacks mCallBacks;
    private boolean mIsDismissed=false;

    public ConnectionDialog() {
        // Required empty public constructor
    }


    public static ConnectionDialog newInstance() {

        Bundle args = new Bundle();

        ConnectionDialog fragment = new ConnectionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            mCallBacks= (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connecton_dialog, container, false);
        mBtnTryAgain = view.findViewById(R.id.tryAgain_btn_ConnectionD);

        mBtnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isConnected= NetworkConnection.checkConnection(getActivity());
                if (isConnected) {
                    mCallBacks.goPreviousFragment();
                    mIsDismissed = true;
                    dismiss();
                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT );
    }




    public interface CallBacks {
        void goPreviousFragment();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (!mIsDismissed) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}
