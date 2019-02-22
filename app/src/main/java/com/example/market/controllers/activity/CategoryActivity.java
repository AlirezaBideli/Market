package com.example.market.controllers.activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.example.market.R;
import com.example.market.controllers.fragmnet.CategoryListFragment;
import com.example.market.controllers.fragmnet.DetailFragment;
import com.example.market.controllers.fragmnet.ParentCatFragment;
import com.example.market.controllers.fragmnet.ProductFragment;
import com.example.market.controllers.fragmnet.ProductListFragment;
import com.example.market.interfaces.ActivityStart;
import com.example.market.model.Product;
import com.example.market.model.ProductLab;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CategoryActivity extends AppCompatActivity implements ActivityStart
        , CategoryListFragment.CallBacks, ProductListFragment.CallBacks, ProductFragment.CallBacks  {

    //Argument Tags
    public static final String TAG = "CategoryActivity";
    //widgets Variables
    private Toolbar mToolbar;
    //Widgets Variables
    private FrameLayout mLoadingCover;
    private ProductLab mProductLab;
    private boolean mIsDownloadable = true;
    //AsyncTasks
    private Runnable mCategoriesRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewByIds();
        variableInit();


    }




    @Override
    public void findViewByIds() {


        mLoadingCover = findViewById(R.id.cover_CategoryA);
        mToolbar = findViewById(R.id.toolbar_CategoryA);
    }

    @Override
    public void variableInit() {
        FragmentManager fragmentManager=getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container_CategoryA,ParentCatFragment.newInstance())
                .commit();
        setUpNavigation();

    }

    private void setUpNavigation() {
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListeners() {


    }


    @Override
    public void gotToProductList(int subCatId) {

        Fragment fragment = ProductListFragment.newInstance(subCatId);
        changePage(fragment);
    }

    private void changePage( Fragment fragment) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.container_CategoryA, fragment)
                            .commit();
    }

    @Override
    public void showProductDetails(int id) {


        Fragment fragment = ProductFragment.newInstance(id);
        changePage(fragment);

    }

    @Override
    public void showDetails(Product product) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_CategoryA, DetailFragment.newInstance(product))
                .commit();


    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment currentFragmnet = fragments.get(fragments.size() - 1);
        if (currentFragmnet instanceof ParentCatFragment)
            super.onBackPressed();
        else
            fragmentManager.beginTransaction()
                    .remove(currentFragmnet)
                    .commit();
    }


}
