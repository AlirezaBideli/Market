package com.example.market.model;

public class ReviewJsonBody {
    private int product_id;
    private String review;
    private String reviewer;
    private String reviewer_email;
    private float rating;

    public ReviewJsonBody(int product_id, String review, String reviewer, String reviewer_email, float rating) {
        this.product_id = product_id;
        this.review = review;
        this.reviewer = reviewer;
        this.reviewer_email = reviewer_email;
        this.rating = rating;
    }
}
