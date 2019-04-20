package com.example.market.model.repositories;

import android.content.Context;

import com.example.market.App;
import com.example.market.model.Billing;
import com.example.market.model.BillingDao;


import java.util.ArrayList;
import java.util.List;

public class BillingLab {

    private BillingDao mBillingDao;
    private static final BillingLab ourInstance = new BillingLab();

    public static BillingLab getInstance() {
        return ourInstance;
    }

    private BillingLab() {
        mBillingDao = App.getApp().getDaoSession().getBillingDao();
    }

    public void addBilling(Billing billing) {

        mBillingDao.insert(billing);

    }

    public void updateBilling(Billing billing) {

        mBillingDao.update(billing);

    }


    public void deleteBilling(Billing billing) {
        mBillingDao.delete(billing);
    }

    public List<Billing> getBillings() {
        Context context = App.getmContext();
        int customerId = CustomerLab.getInstance(context).getCustomerId();
        List<Billing> billingList = mBillingDao.queryBuilder()
                .where(BillingDao.Properties.MCustomerId.eq(customerId))
                .list();
        List<Billing> result = new ArrayList<>();
        int selctedBillingIndex=0;
        for (int i=0;i<billingList.size();i++) {
            if (billingList.get(i).getIsSelected())
            {
                selctedBillingIndex=i;
                break;
            }
        }
        //first billing is selected billing
        Billing firstBilling=billingList.get(selctedBillingIndex);

        result.add(firstBilling);
        result.addAll(billingList);
        result.remove(selctedBillingIndex+1);

        return result;
    }

    public Billing getSelectedBilling() {
        return mBillingDao.queryBuilder()
                .where(BillingDao.Properties.IsSelected.eq(true))
                .unique();
    }


    public Billing getUniqueBilling(long billingId) {
        return mBillingDao.queryBuilder().where(BillingDao.Properties._ID.eq(billingId)).unique();
    }

    public List<Billing> setNotSelectedOtherBillings(Billing exceptionBilling) {


        long exceptionBillingId = exceptionBilling.get_id();

        //get All Billing except Billing selected by User
        List<Billing> otherBillings = mBillingDao.
                queryBuilder()
                .where(BillingDao.Properties._ID.notEq(exceptionBillingId))
                .list();

        //Set them not selected  an Update in db
        for (Billing i : otherBillings)
            i.setIsSelected(false);
        mBillingDao.updateInTx(otherBillings);

        List<Billing> sortedList = new ArrayList<>();
        sortedList.add(exceptionBilling);
        sortedList.addAll(otherBillings);

        return sortedList;


    }
}
