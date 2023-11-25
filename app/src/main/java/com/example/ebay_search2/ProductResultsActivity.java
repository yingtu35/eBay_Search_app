package com.example.ebay_search2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ebay_search2.ui.productResults.ProductResultsFragment;

public class ProductResultsActivity extends AppCompatActivity {

    private static String TAG = "ProductResultsActivity";
    private static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_results_acitivity);

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