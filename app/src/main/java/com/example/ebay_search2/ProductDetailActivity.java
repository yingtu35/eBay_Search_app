package com.example.ebay_search2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ebay_search2.DetailPagerAdapter;
import com.example.ebay_search2.databinding.ActivityProductDetailBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Intent intent = getIntent();
//        String details = intent.getStringExtra("details");
        String allInfo = intent.getStringExtra("allInfo");
//        String googleImage = intent.getStringExtra("googleImage");

        DetailPagerAdapter DetailPagerAdapter = new DetailPagerAdapter(this, allInfo);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(DetailPagerAdapter);
        TabLayout tabs = binding.tabs;
//        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Add tabs with icons and labels
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.information_variant);
                            tab.setText(getString(R.string.tab_text_1));
                            break;
                        case 1:
                            tab.setIcon(R.drawable.truck_delivery);
                            tab.setText(getString(R.string.tab_text_2));
                            break;
                        // Add more cases for additional tabs
                        case 2:
                            tab.setIcon(R.drawable.google);
                            tab.setText(getString(R.string.tab_text_3));
                            break;
                        case 3:
                            tab.setIcon(R.drawable.equal);
                            tab.setText(getString(R.string.tab_text_4));
                            break;
                    }
                }
        ).attach();
    }
}