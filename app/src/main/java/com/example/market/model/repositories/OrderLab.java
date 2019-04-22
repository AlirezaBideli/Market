package com.example.market.model.repositories;

import android.content.Context;

import com.example.market.App;
import com.example.market.model.Order;
import com.example.market.model.OrderDao;

import java.util.ArrayList;
import java.util.List;

public class OrderLab {

    private static final String ORDER_PREFS = "orders";
    private static final String ID_KEY = "idKey";
    private static final String COUNT_KEY = "countKey";
    private static final byte ID_FEATURE = 0;
    private static final byte COUNT_FEATURE = 1;
    private static final int NOT_FOUND = -1;
    private static OrderLab ourInstance;
    private OrderDao mOrderDao;
    private Context mContext;
    private long mTotalPrice;

    private OrderLab(Context context) {
        mContext = context;
        mOrderDao = App.getApp().
                getDaoSession().
                getOrderDao();
    }

    public static OrderLab getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new OrderLab(context);
        return ourInstance;
    }

    public long getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        mTotalPrice = totalPrice;
    }

    //Products Shopping Cart Methods
    public void insertToShoppingCart(Order order) {

        mOrderDao.insertOrReplace(order);
    }

    public void deleteOrderedProduct(Order order) {
        mOrderDao.delete(order);
    }

    public boolean checkProductExist(Order order) {
        Long productId = order.get_id();
        List<Order> result = mOrderDao.queryBuilder().where(OrderDao.Properties._id.eq(productId)).list();
        return result != null && result.size() != 0;
    }

    public List<Order> getOrders() {
        return mOrderDao.loadAll();
    }

    public String getOrderIds() {
        List<Order> orders = getOrders();
        int size = orders.size();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < size; i++) {
            Long id = orders.get(i).get_id();
            ids.append(id).append(",");
        }

        return ids.toString();

    }

    public List<Integer> getOrderCounts() {
        List<Order> orders = getOrders();
        int size = orders.size();
        List<Integer> counts = new ArrayList<>();
        int count;
        for (int i = 0; i < size; i++) {
            count = orders.get(i).getCount();
            counts.add(count);
        }

        return counts;

    }

    public void deleteAllOrders() {
        mOrderDao.deleteAll();
    }

}
