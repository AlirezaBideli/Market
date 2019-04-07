package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.adapters.ReviewAdapter;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Review;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewListFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PRODUCT_ID = "arg_product_id";
    private Call<List<Review>> mCallReview;
    private TextView mTvNoReview;
    private FloatingActionButton mFabCreate;
    private RecyclerView mRecyReview;
    private CallBacks mCallBacks;
    private LoadingCallBack mLoadingCallBack;

    public ReviewListFragment() {
        // Required empty public constructor
    }


    public static ReviewListFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBacks)
            mCallBacks = (CallBacks) context;
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
        mLoadingCallBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFabCreate = view.findViewById(R.id.fab_create);
        mRecyReview = view.findViewById(R.id.recy_review);
        mTvNoReview=view.findViewById(R.id.tv_no_review);
        mFabCreate.setOnClickListener(this);
        getReviewsFromApi();
    }

    private void setUpRecyclerView(List<Review> reviewList) {

        mTvNoReview.setVisibility(View.INVISIBLE);
        ReviewAdapter adapter = new ReviewAdapter(reviewList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyReview.setLayoutManager(layoutManager);
        mRecyReview.setHasFixedSize(true);
        mRecyReview.setAdapter(adapter);


    }

    private void getReviewsFromApi() {
        mLoadingCallBack.showLoading();
        int productId = getArguments().getInt(ARG_PRODUCT_ID);
        mCallReview = RetrofitClientInstance.getRetrofitInstance()
                .create(Api.class)
                .getReviews(productId);

        mCallReview.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                mLoadingCallBack.hideLoading();
                if (response.isSuccessful()) {

                    List<Review> reviewList = response.body();
                    if (reviewList == null ^ reviewList.size()==0) {
                        mRecyReview.setVisibility(View.INVISIBLE);
                        mTvNoReview.setVisibility(View.VISIBLE);
                    }

                    else
                        setUpRecyclerView(reviewList);


                } else {
                    if (getActivity() != null)
                        NetworkConnection.warnConnection(getActivity(), getFragmentManager());

                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                mLoadingCallBack.hideLoading();
                if (getActivity() != null)
                    NetworkConnection.warnConnection(getActivity(), getFragmentManager());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCallReview != null)
            mCallReview.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create:
                mCallBacks.showCreateReviewFragment();
                break;
        }
    }

    public interface CallBacks {
        void showCreateReviewFragment();
    }
}
