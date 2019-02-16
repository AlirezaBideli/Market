package com.example.market.controllers.fragmnet;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.market.R;
import com.example.market.interfaces.FragmentStart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ParentFragment extends Fragment {


    protected abstract void  findViewByIds(View view);
    protected abstract void variableInit();
    protected abstract void setListeners();


}
