package com.example.market.controllers.fragmnet;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.controllers.activity.MarketActivity;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.network.Api;
import com.example.market.network.RetrofitClientInstance;
import com.example.market.utils.NetworkConnection;
import com.example.market.utils.PriceUtils;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends ParentFragment {


    List<String> mFeaturedProductImages = new ArrayList<>();
    //CallBacks
    private CallBacks mCallBacks;
    //Widgets Variables
    private RecyclerView mRecyNewest;
    private RecyclerView mRecyMostVisited;
    private RecyclerView mRectBestSellers;
    private ViewPager mPagerFeaturesProducts;
    private PageIndicatorView mPageIndicatorView;
    //Simple Variables
    private int mDeafaultCount = 20;
    private List<Product> mProducts;


    public static MarketFragment newInstance() {

        Bundle args = new Bundle();

        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MarketActivity)
            mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }




    private void setUpViewPager() {
        mFeaturedProductImages=ProductLab.getInstance().getFeaturedProductImg();
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                String src = mFeaturedProductImages.get(position);
                Picasso.get().load(src).placeholder(R.drawable.shop_placeholder).into(imageView);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return mFeaturedProductImages.size();
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
        mPagerFeaturesProducts.setAdapter(pagerAdapter);
        mPagerFeaturesProducts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_market, container, false);
        findViewByIds(view);
        variableInit();
        setListeners();
        setUpRecyclerView();
        setUpViewPager();
        return view;
    }

    private void setUpRecyclerView() {
        List<Product> products;
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);

        //Newest RecyclerView
        fillRecyclerView(layoutManager, ProductType.NEWEST, mRecyNewest);
        //Best Sellers
        fillRecyclerView(layoutManager2, ProductType.MOST_VISITED, mRecyMostVisited);
        //Most Visited RecyclerView
        fillRecyclerView(layoutManager3, ProductType.BESTS, mRectBestSellers);

    }

    private void fillRecyclerView(LinearLayoutManager layoutManager
            , ProductType type, RecyclerView recyclerView) {

        ProductLab productLab = ProductLab.getInstance();
        List<Product> products = getProducts(type, productLab);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);
    }

    private List<Product> getProducts(ProductType type, ProductLab productLab) {
        List<Product> products = new ArrayList<>();
        switch (type) {
            case NEWEST:
                products = productLab.getNewestProducts();
                break;
            case MOST_VISITED:
                products = productLab.getMVisitedProducts();
                break;
            case BESTS:
                products = productLab.getBProducts();
                break;
        }
        return products;
    }


    @Override
    protected void findViewByIds(View view) {

        mRecyNewest = view.findViewById(R.id.recy_newest_MarketA);
        mRecyMostVisited = view.findViewById(R.id.recy_mostVisited_MarketA);
        mRectBestSellers = view.findViewById(R.id.recy_bests_MarketA);
        mPageIndicatorView = view.findViewById(R.id.pageIndicatorView_MarketA);
        mPagerFeaturesProducts = view.findViewById(R.id.pager_featured_products);
    }

    @Override
    public void variableInit() {


    }

    @Override
    public void setListeners() {

    }


    private enum ProductType {
        NEWEST,
        MOST_VISITED,
        BESTS,
    }

    public interface CallBacks {
        void showProductDetails(int id);
    }

    private class ProductHolder extends RecyclerView.ViewHolder {


        private TextView mTxtName;
        private TextView mTxtPrice;
        private ImageView mImgProduct;

        public ProductHolder(@NonNull View itemView, final List<Product> products) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.name_txt_specialItem);
            mTxtPrice = itemView.findViewById(R.id.price_txt_specialItem);
            mImgProduct = itemView.findViewById(R.id.image_img_specialItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int id = products.get(position).getId();
                    mCallBacks.showProductDetails(id);
                }
            });
        }

        public void bind(Product product) {


            String rawPrice = product.getPrice();
            String formatedPrice = PriceUtils.getCurrencyFormat(rawPrice, getActivity());
            mTxtPrice.setText(formatedPrice);
            mTxtName.setText(product.getName());


            if (product.getImages() == null)
                mImgProduct.setImageResource(R.drawable.shop_placeholder);
            else {
                String src = product.getImages().get(0).getSrc();
                Picasso.get()
                        .load(src)
                        .placeholder(R.drawable.shop_placeholder)
                        .into(mImgProduct);
            }
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {


        List<Product> mProductList;

        public ProductAdapter(List<Product> products) {
            mProductList = products;
        }

        public List<Product> getProducts() {
            return mProductList;
        }

        public void setProducts(List<Product> products) {
            mProductList = products;
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.special_product_item, parent, false);
            return new ProductHolder(view, mProductList);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {

            holder.bind(mProductList.get(position));
        }

        @Override
        public int getItemCount() {
            if (mProductList.size() < mDeafaultCount)
                return mProductList.size();

            return mDeafaultCount;
        }


    }


}
