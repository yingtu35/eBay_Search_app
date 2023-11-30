package com.example.ebay_search2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ebay_search2.ui.productDetails.ImagesTab;
import com.example.ebay_search2.ui.productDetails.ProductDetailTab;
import com.example.ebay_search2.ui.productDetails.ShippingTab;
import com.example.ebay_search2.ui.productDetails.SimilarProductsTab;

import org.json.JSONObject;


public class DetailPagerAdapter extends FragmentStateAdapter {

    private JSONObject allInfo;
    private static final int NUM_PAGES = 4;
    public DetailPagerAdapter(FragmentActivity fa, JSONObject allInfo) {
        super(fa);
        this.allInfo = allInfo;

    }
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductDetailTab(this.allInfo);
            case 1:
                return new ShippingTab(this.allInfo);
            case 2:
                return new ImagesTab(this.allInfo);
            case 3:
                return new SimilarProductsTab(this.allInfo);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}