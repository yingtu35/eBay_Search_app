package com.example.ebay_search2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebay_search2.ui.Product;
import com.example.ebay_search2.ui.WishlistManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static final String URL = "http://10.0.2.2:3000";
    private static final String TAG = "SplashActivity";
    private static WishlistManager wishlistManager = WishlistManager.getInstance();
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the wishlist from the server
        initializeWishList();

        // Using a handler to delay loading the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Finish this activity so the user can't return to it
                finish();
            }
        }, 0);

    }

    private void initializeWishList() {
        ApiCall.getWishlist(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d(TAG, response);
                try {
                    if (response != null) {
                        // parse the response
                        JSONArray items = new JSONArray(response);
                        Log.d(TAG, "wishListItems: " + items);
                        addItemsInWishList(items);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    private void addItemsInWishList(JSONArray items) throws JSONException {
        Log.d(TAG, "addItemsInWishList: " + items.length());
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String itemId = item.getString("_id");
            String title = item.has("Title") ? item.getString("Title") : "unknown";
            String price = item.has("Price") ? item.getString("Price") : "unknown";
            String shipping = item.has("Shipping") ? item.getString("Shipping") : "unknown";
            String zip = item.has("Zip") ? item.getString("Zip") : "unknown";
            String condition = item.has("Condition") ? item.getString("Condition") : "unknown";
            String image = item.has("Image") ? item.getString("Image") : "";
            String url = item.has("Url") ? item.getString("Url") : "";
            Boolean isWishListed = true;
            Product product = new Product(itemId, title, image, url, zip, shipping, price, condition, isWishListed);
            Log.d(TAG, "addItemsInWishList: " + product.toString());
            wishlistManager.addProductToWishlist(itemId, product);
        }
    }
}