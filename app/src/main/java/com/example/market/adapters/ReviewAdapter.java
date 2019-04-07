package com.example.market.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.market.App;
import com.example.market.R;
import com.example.market.model.Review;
import com.example.market.utils.HTMLTags;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {


    private final Activity mContext;
    private List<Review> mReviewList;


    public ReviewAdapter(List<Review> reviewList, Activity activity) {
        mReviewList = reviewList;
        mContext=activity;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.review_item,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.bind(review);
    }


    @Override
    public int getItemCount() {
        return mReviewList.size();
    }



    class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView mTvReviewer;
        private TextView mTvReview;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            mTvReviewer=itemView.findViewById(R.id.tv_reviewer);
            mTvReview=itemView.findViewById(R.id.tv_review);
        }

        public void bind(Review review) {
            if (review != null) {
                String reviewText = review.getReview();
                String formattedReviewText= HTMLTags.removeHTMLTags(reviewText);
                String reviewer = review.getReviewer();
                mTvReview.setText(formattedReviewText);
                mTvReviewer.setText(reviewer);
            }
        }


    }
}

