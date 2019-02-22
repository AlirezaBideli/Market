package com.example.market.controllers.fragmnet;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.market.R;
import com.example.market.controllers.activity.CategoryActivity;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.model.Product;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends ParentFragment implements View.OnClickListener {

    //Argument Tags
    private static final String ARG_ID = "productId";
    //CallBack
    private CallBacks mCallBacks;
    //Widgets Variables
    private TextView mTxtName, mTxtPrice, mTxtDesc;
    private MaterialButton mBtnDetail;
    private ViewPager mImgPager;
    //simple Variables
    private Product mProduct;
    private int mId;
    private ProgressDialog mProgressDialog;


    //Widgets Variables

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ((context instanceof CategoryActivity))
            mCallBacks = (CategoryActivity) context;
        else
            if ((context instanceof MarketActivity))
                mCallBacks = (MarketActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void findViewByIds(View view) {

        mTxtName = view.findViewById(R.id.name_txt_productF);
        mTxtPrice = view.findViewById(R.id.price_txt_productF);
        mTxtDesc = view.findViewById(R.id.desc_txt_productF);
        mImgPager = view.findViewById(R.id.image_pager_productF);
        mBtnDetail = view.findViewById(R.id.detail_btn_productF);
    }

    @Override
    protected void variableInit() {

        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.show();
        mId = getArguments().getInt(ARG_ID);
        RetrofitClientInstance.getRetrofitInstance().create(Api.class)
                .getProduct(mId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    mProduct= response.body();
                    if (mProduct != null) {
                        mTxtName.setText(mProduct.getName());
                        mTxtPrice.setText(mProduct.getPrice());
                        mTxtDesc.setText(mProduct.getDescription());
                        PagerAdapter pagerAdapter = new PagerAdapter() {
                            @NonNull
                            @Override
                            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                                ImageView imageView = new ImageView(getActivity());
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                String src = mProduct.getImages().get(position).getSrc();
                                Picasso.get().load(src).into(imageView);
                                container.addView(imageView);
                                return imageView;
                            }

                            @Override
                            public int getCount() {
                                if (mProduct.getImages() == null || mProduct.getImages() == null)
                                    return 0;
                                else return mProduct.getImages().size();
                            }

                            @Override
                            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                                return view == object;
                            }

                            @Override
                            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                                container.removeView((View) object);
                            }
                        };
                        mImgPager.setAdapter(pagerAdapter);
                    }
                    mProgressDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });

    }

    @Override
    protected void setListeners() {

        mBtnDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.detail_btn_productF:
                mCallBacks.showDetails(mProduct);
                break;
        }
    }

    public interface CallBacks {
        void showDetails(Product product);
    }
}
