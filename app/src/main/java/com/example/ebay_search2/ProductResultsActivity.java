package com.example.ebay_search2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import com.example.ebay_search2.ui.productResults.ProductResultsFragment;

public class ProductResultsActivity extends AppCompatActivity {

    private static String TAG = "ProductResultsActivity";
    private static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_results_acitivity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.toolbar_product_results);

            // Remove content insets
            Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);

            // Set up back button click listener
            ImageView backButton = actionBar.getCustomView().findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

//        ProductResultsFragment productResultsFragment = new ProductResultsFragment();
        // Retrieve parameters from Intent
//        Intent intent = getIntent();
//        Log.d(TAG, intent.toString());
//        if (intent != null && intent.hasExtra("result")) {
//            Log.d(TAG, "onCreate: "+intent.getStringExtra("result"));
//            result = intent.getStringExtra("result");
//            Log.d(TAG, result);

            // Use the parameterValue as needed

//
//            Bundle bundle = new Bundle();
//            bundle.putString("result", parameterValue);
//            productResultsFragment.setArguments(bundle);

            // Add or replace Fragment A
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .replace(R.id.product_result_fragment, ProductResultsFragment.class, bundle)
//                    .commit();
//        }


    }


}