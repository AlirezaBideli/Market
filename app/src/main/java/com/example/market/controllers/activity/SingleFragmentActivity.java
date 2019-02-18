package com.example.market.controllers.activity;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {


    public abstract Fragment createFragment();

    public abstract int getContainerId();
    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(getContainerId()) == null) {
            fragmentManager.beginTransaction()
                    .add(getContainerId(), createFragment())
                    .commit();
        }
    }
}
