package com.example.market.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.fragment.OrderFragment;
import com.example.market.model.Billing;
import com.example.market.model.repositories.BillingLab;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingHolder> {


    private static final int ADD_LAYOUT = 1;
    private static final int ADDRESS_LAYOUT = 0;
    private static final String TAG = "BillingAdapter";
    private final RecyclerView mRecyclerView;

    private OrderFragment.CallBacks mOrderCallBacks;
    private Callbacks mCallBacks;
    private List<Billing> mBillingList;
    private Context mContext;
    private int mSize;

    public BillingAdapter(List<Billing> mBillingList, Context context,
                          OrderFragment.CallBacks callBacks,RecyclerView recyclerView) {

        this.mBillingList = mBillingList;
        this.mSize = mBillingList.size() + 1;
        this.mContext = context;
        this.mOrderCallBacks = callBacks;
        this.mRecyclerView=recyclerView;
        this.mCallBacks= (Callbacks) context;

    }

    @NonNull
    @Override
    public BillingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view;
        if (viewType == ADDRESS_LAYOUT)
            view = LayoutInflater.from(mContext).inflate(R.layout.billing_item, parent,
                    false);
        else
            view = LayoutInflater.from(mContext).inflate(R.layout.add_item, parent,
                    false);

        return new BillingHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingHolder holder, int position) {


        if (getItemViewType(position) == ADDRESS_LAYOUT) {
            Billing billing = mBillingList.get(position);
            holder.bind(billing,getItemViewType(position));
        } else //Add Layout don't need billing object
            holder.bind(null,getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        //(+1)extra item is for add layout
        return mSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mSize - 1) {
            Log.i(TAG,"Add Layout");
            return ADD_LAYOUT;
        }
        else {
            Log.i(TAG,"Address Layout");
            return ADDRESS_LAYOUT;
        }

    }

    class BillingHolder extends RecyclerView.ViewHolder {

        private TextView mTvState, mTvCity, mTvAddress, mTvPhoneCall;
        private ImageView mBtnAdd;
        private RadioButton mRdSelectAddress;
        private ImageView mBtnDelete, mBtnEdit;
        private long DEFAULT_BILLING_ID = 0;


        public BillingHolder(@NonNull View itemView,int viewType) {
            super(itemView);


            if (viewType == ADDRESS_LAYOUT) {
                mTvState = itemView.findViewById(R.id.tv_state);
                mTvAddress = itemView.findViewById(R.id.tv_address);
                mTvPhoneCall = itemView.findViewById(R.id.tv_phon_call);
                mTvCity = itemView.findViewById(R.id.tv_city);
                mRdSelectAddress = itemView.findViewById(R.id.rdb_select_address);
                mBtnEdit = itemView.findViewById(R.id.btn_edit);
                mBtnDelete = itemView.findViewById(R.id.btn_delete);
            } else
                mBtnAdd = itemView.findViewById(R.id.img_add);


        }

        public void bind(final Billing billing,int viewType) {



            if (viewType == ADD_LAYOUT) {
                mBtnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOrderCallBacks.goToBillingForm(false, DEFAULT_BILLING_ID);
                    }
                });
                return;
            }

            if (billing == null)
                return;

            if (viewType == ADDRESS_LAYOUT) {
                final long billingId = billing.get_id();
                String state = billing.getMState();
                String city = billing.getMCity();
                String phone = billing.getMPhone();
                String address = billing.getMAddress_1();
                String addressName = billing.getFullName();
                boolean isSelected = billing.getIsSelected();
                mTvState.setText(state);
                mTvCity.setText(city);
                mTvAddress.setText(address);
                mTvPhoneCall.setText(phone);
                mRdSelectAddress.setText(addressName);

                if (isSelected)
                    mRdSelectAddress.setChecked(true);
                else
                    mRdSelectAddress.setChecked(false);

                mBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mOrderCallBacks.goToBillingForm(true, billingId);
                    }
                });

                mBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BillingLab billingLab = BillingLab.getInstance();

                        billingLab.deleteBilling(billing);
                        mBillingList.remove(billing);
                        mSize--;
                        notifyItemRemoved(getAdapterPosition());
                    }
                });

                mRdSelectAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            BillingLab billingLab = BillingLab.getInstance();
                            billing.setIsSelected(isChecked);
                            billingLab.updateBilling(billing);
                            mBillingList = billingLab.setNotSelectedOtherBillings(billing);

                            mCallBacks.updateAddressSelectionRecy();
                        }
                    }
                });

            }
        }
    }

    public interface Callbacks
    {
        void updateAddressSelectionRecy();
    }

}
