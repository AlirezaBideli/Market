package com.example.market.model;

import android.app.Notification;

public class NotificationEvent {

    private int mRequestCode;
    private Notification mNotification;

    public NotificationEvent(int requestCode, Notification notification) {
        mRequestCode = requestCode;
        mNotification = notification;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    public Notification getNotification() {
        return mNotification;
    }

    public void setNotification(Notification notification) {
        mNotification = notification;
    }
}
