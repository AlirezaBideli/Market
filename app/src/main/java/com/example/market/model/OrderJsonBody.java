package com.example.market.model;

import java.util.ArrayList;
import java.util.List;

public class OrderJsonBody {

    private Billing billing;
    private List<Order> line_items;
    private List<Coupon> coupon_lines;
    private int customer_id;

    public OrderJsonBody(Billing billing, List<Order> line_items, Coupon coupon, int customer_id) {
        this.billing = billing;
        this.line_items = line_items;
        this.customer_id = customer_id;
        this.coupon_lines = new ArrayList<>();
        this.coupon_lines.add(coupon);
    }


}
