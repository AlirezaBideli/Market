package com.example.market.controllers.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.model.LoadingCallBack;
import com.example.market.model.Review;
import com.example.market.model.ReviewJsonBody;
import com.example.market.model.repositories.CustomerLab;
import com.example.market.model.repositories.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReviewFragment extends Fragment implements View.OnClickListener {


    public static final int DEFAULT_RATING = 3;
    private MaterialCardView mBtnSendReview;
    private EditText mEdtReviewText;
    private Call<Review> mCallReview;
    private LoadingCallBack mLoadingCallBack;
    private RatingBar mRatingReview;

    public CreateReviewFragment() {
        // Required empty public constructor
    }

    public static CreateReviewFragment newInstance() {

        Bundle args = new Bundle();

        CreateReviewFragment fragment = new CreateReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingCallBack)
            mLoadingCallBack = (LoadingCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoadingCallBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEdtReviewText = view.findViewById(R.id.edt_review);
        mBtnSendReview = view.findViewById(R.id.btn_send_review);
        mRatingReview = view.findViewById(R.id.rating_review);

        mBtnSendReview.setOnClickListener(this);

        mRatingReview.setRating(DEFAULT_RATING);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_review:
                sendReview();
                break;
        }
    }

    private void sendReview() {

        String reviewText = mEdtReviewText.getText().toString();
        if (!reviewText.isEmpty()) {
            CustomerLab customerLab = CustomerLab.getInstance(getActivity());
            ProductLab productLab=ProductLab.getInstance(getActivity());
            String reviewer = customerLab.getCustomerFullName();
            String reviewer_email = customerLab.getCustomerEmail();
            float rating = mRatingReview.getRating();
            int productId=productLab.getCurrentProduct().getId();
            mLoadingCallBack.showLoading();
            ReviewJsonBody jsonBody = new ReviewJsonBody(productId , reviewText,
                    reviewer, reviewer_email, rating);
            mCallReview = RetrofitClientInstance.getRetrofitInstance()
                    .create(Api.class)
                    .sendProductReview(jsonBody);
            mCallReview.enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Call<Review> call, Response<Review> response) {
                    if (getActivity() != null) {
                        checkReviewApiResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<Review> call, Throwable t) {
                    if (getActivity() != null)
                        NetworkConnection.warnConnection(getActivity(), getFragmentManager());
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_review_text)
                    , Toast.LENGTH_SHORT).show();
        }

    }

    private void checkReviewApiResponse(Response<Review> response) {
        if (response.isSuccessful()) {
            Review review = response.body();
            if (review != null) {
                String message = getString(R.string.send_review_message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        } else {
            String message = getString(R.string.unsuccessful_send_review_message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            NetworkConnection.warnConnection(getActivity(), getFragmentManager());
        }
        mLoadingCallBack.hideLoading();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCallReview != null)
            mCallReview.cancel();
    }
}
