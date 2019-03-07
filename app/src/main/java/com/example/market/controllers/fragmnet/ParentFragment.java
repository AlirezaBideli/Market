package com.example.market.controllers.fragmnet;


import android.view.View;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ParentFragment extends Fragment {


    protected abstract void  findViewByIds(View view);
    protected abstract void variableInit();
    protected abstract void setListeners();


}
