package com.example.market.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.interfaces.ActivityStart;
import com.example.market.interfaces.NetworkControll;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import com.example.market.utils.ActivityHeper;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MarketActivity extends AppCompatActivity implements ActivityStart
        , NetworkControll, NavigationView.OnNavigationItemSelectedListener {


    //Widgets Variables
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    //Recycler Views
    private RecyclerView mRecyNewest;
    private RecyclerView mRecyMostVisited;
    private RecyclerView mRectBestSellers;
    private ProductAdapter mRecyAdapter;

    private int mDeafaultCount = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        findViewByIds();
        variableInit();
        setListeners();
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        List<Product> products;
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(MarketActivity.this,
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(MarketActivity.this,
                        LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 =
                new LinearLayoutManager(MarketActivity.this,
                        LinearLayoutManager.HORIZONTAL, false);


        //Best Sellers
        products=ProductLab.getInstance().getBProducts();
        mRectBestSellers.setLayoutManager(layoutManager3);
        mRectBestSellers.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRectBestSellers.setAdapter(mRecyAdapter);
        //Most Visited RecyclerView
        products = ProductLab.getInstance().getMVisitedProducts();
        mRecyMostVisited.setLayoutManager(layoutManager2);
        mRecyMostVisited.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRecyMostVisited.setAdapter(mRecyAdapter);
        //Newest RecyclerView
        products = ProductLab.getInstance().getNewestProducts();
        mRecyNewest.setLayoutManager(layoutManager);
        mRecyNewest.setHasFixedSize(true);
        mRecyAdapter = new ProductAdapter(products);
        mRecyNewest.setAdapter(mRecyAdapter);
    }

    @Override
    public void findViewByIds() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mRecyNewest = findViewById(R.id.recy_newest_MarketA);
        mRecyMostVisited = findViewById(R.id.recy_mostVisited_MarketA);
        mRectBestSellers=findViewById(R.id.recy_bests_MarketA);
    }

    @Override
    public void variableInit() {

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MarketActivity.this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void checkConnection() {

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {

            case R.id.categories_list:
                goToCategories();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goToCategories() {
        Intent intent = ActivityHeper.Intent_CategoryA(MarketActivity.this);
        startActivity(intent);
    }


    private class ProductHolder extends RecyclerView.ViewHolder {


        private TextView mTxtName;
        private TextView mTxtPrice;
        private ImageView mImgProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.name_txt_specialItem);
            mTxtPrice = itemView.findViewById(R.id.price_txt_specialItem);
            mImgProduct = itemView.findViewById(R.id.image_img_specialItem);
        }

        public void bind(Product product) {

            mTxtName.setText(product.getName());
            mTxtPrice.setText(product.getPrice());

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

        private List<Product> mProducts;

        public ProductAdapter(List<Product> products) {
            mProducts = products;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(MarketActivity.this)
                    .inflate(R.layout.special_product_item, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

            holder.bind(mProducts.get(position));
        }

        @Override
        public int getItemCount() {
            if (mProducts.size() < mDeafaultCount)
                return mProducts.size();

            return mDeafaultCount;
        }
    }
}
