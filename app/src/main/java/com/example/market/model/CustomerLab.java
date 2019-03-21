package com.example.market.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CustomerLab {


    private static final String ID_KEY = "id_key";
    private static final String FIRST_NAME_KEY = "firstName_key";
    private static final String LAST_NAME_KEY = "lastName_key";
    private static final String USERNAME_KEY = "userName_key";
    private static final String PASSWORD = "password_key";
    private static final String EMAIL_KEY = "email_key";
    private static final String IS_USER_REGISTERED="isUserRegistered";
    private static CustomerLab ourInstance;
    private SharedPreferences mCustomerPrefs;

    private CustomerLab(Context context) {
        mCustomerPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CustomerLab getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new CustomerLab(context);
        return ourInstance;
    }

    public void storeCustomer(Customer customer) {
        int id = customer.get_id();
        String firstName=customer.getFirstName();
        String lastName=customer.getLastName();
        String userName=customer.getUserName();
        String password=customer.getPassword();
        String email=customer.getEmail();

        String[] keys={ID_KEY,FIRST_NAME_KEY,LAST_NAME_KEY,USERNAME_KEY,EMAIL_KEY,PASSWORD};
        String[] values={id+"",firstName,lastName,userName,email,password};
        int size=keys.length;
        for (byte i=0;i<size;i++)
            mCustomerPrefs.edit().putString(keys[i],values[i]).apply();

        mCustomerPrefs.edit().putBoolean(IS_USER_REGISTERED,true).apply();

    }

    public boolean isUserRegistered()
    {
        return mCustomerPrefs.getBoolean(IS_USER_REGISTERED,false);
    }

    public int getCustomerId()
    {
        return mCustomerPrefs.getInt(ID_KEY,0);
    }

    public void storeCustomerId(int id) {
        mCustomerPrefs.edit().putInt(ID_KEY,id).apply();
    }
}
