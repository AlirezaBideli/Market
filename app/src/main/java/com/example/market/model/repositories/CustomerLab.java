package com.example.market.model.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.market.model.Customer;

public class CustomerLab {


    private static final String ID_KEY = "id_key";
    private static final String FIRST_NAME_KEY = "firstName_key";
    private static final String LAST_NAME_KEY = "lastName_key";
    private static final String IS_USER_REGISTERED="isUserRegistered";
    private static final String EMAIL_KEY = "email_key";
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
        String email=customer.getEmail();

        String[] keys={ID_KEY,FIRST_NAME_KEY,LAST_NAME_KEY,EMAIL_KEY};
        String[] values={id+"",firstName,lastName,email};
        int size=keys.length;
        for (byte i=0;i<size;i++)
            mCustomerPrefs.edit().putString(keys[i],values[i]).apply();

        mCustomerPrefs.edit().putBoolean(IS_USER_REGISTERED,true).apply();

    }

    public boolean isUserRegistered()
    {
        return mCustomerPrefs.getBoolean(IS_USER_REGISTERED,false);
    }

    public  String getCustomerFullName()
    {
        String firstName=mCustomerPrefs.getString(FIRST_NAME_KEY,"");
        String lastName=mCustomerPrefs.getString(LAST_NAME_KEY,"");
        String fullName=firstName+" "+lastName;
        return fullName;
    }
    public String getCustomerEmail()
    {
        return  mCustomerPrefs.getString(EMAIL_KEY,"");
    }
    public int getCustomerId()
    {
        return mCustomerPrefs.getInt(ID_KEY,0);
    }

    public void storeCustomerId(int id) {
        mCustomerPrefs.edit().putInt(ID_KEY,id).apply();
    }
}
