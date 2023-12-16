package com.example.ebay_search2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ebay_search2.ui.Product;
import com.example.ebay_search2.ui.WishlistManager;
import com.example.ebay_search2.ui.productDetails.ProductDetailTab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebay_search2.DetailPagerAdapter;
import com.example.ebay_search2.databinding.ActivityProductDetailBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailTab.OnDataPassListener {

    private static final int maxTitleLength = 10;
    private static final String TAG = "ProductDetailActivity";
    private ActivityProductDetailBinding binding;
    private View root;
    private Context ctx;
    private Intent intent;
    private FloatingActionButton fab;

    private TextView productTitle;
    private JSONObject allInfo;
    private String itemId;
    private String title;
    private String currentPrice;
    private String viewItemURL;
    private String shrunkTitle;
    private WishlistManager wishlistManager;

    // Implement the interface method to receive the data from the fragment
    @Override
    public void onDataPass(JSONObject data) {
        // Handle the received data here
        Log.d("YourActivity", "Received data from fragment: " + data);
        intent.putExtra("returnPolicy", data.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(root);

        ctx = this;
        
        intent = getIntent();
        try {
            allInfo = new JSONObject(intent.getStringExtra("allInfo"));
            itemId = allInfo.optString("itemId");
            title = allInfo.optString("title");
            shrunkTitle = title.substring(0, maxTitleLength) + "...";
            currentPrice = allInfo.optString("currentPrice");
            viewItemURL = allInfo.optString("viewItemURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: " + allInfo.toString());

        wishlistManager = WishlistManager.getInstance();

        DetailPagerAdapter DetailPagerAdapter = new DetailPagerAdapter(this, allInfo);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(DetailPagerAdapter);
        TabLayout tabs = binding.tabs;
//        tabs.setupWithViewPager(viewPager);
        fab = binding.fab;
        if (wishlistManager.isInWishlist(itemId)) {
            fab.setImageResource(R.drawable.cart_remove);
        } else {
            fab.setImageResource(R.drawable.cart_plus);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wishlistManager.isInWishlist(itemId)) {
//                    delete from wishlist
                    deleteFromWishlist(itemId);
                    fab.setImageResource(R.drawable.cart_plus);
//                    Snackbar.make(view, allInfo.optString("title") + " was removed from wish list", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                } else {
//                    add to wishlist
                    JSONObject requestBody = formRequestBody();
                    addIntoWishList(requestBody);
                    fab.setImageResource(R.drawable.cart_remove);
                }
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

        // Set up back button click listener
        ImageView backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: how to make sure going back to previous page will refresh the wishlist
                finish();
            }
        });

//        Set up page title
        productTitle = root.findViewById(R.id.productTitle);
        setPageTitle(allInfo);

        // Set up Facebook button click listener
        ImageView facebookButton = findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Construct the custom message
                String message = "Buy " + title + " at " + currentPrice + " from " + viewItemURL + " below. #CSCI571Fall2023AndroidApp";

                // Encode the message for URL
                try {
                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
                    // Construct the Facebook sharing URL
                    String facebookShareUrl = "https://www.facebook.com/sharer/sharer.php?u=" + viewItemURL + "&quote=" + encodedMessage;

                    // Open the Facebook sharing dialog in a new window
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareUrl));
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void deleteFromWishlist(String itemId) {
        ApiCall.deleteFromWishlist(ctx, itemId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response
                Log.d(TAG, "product removed into wishlist: " + response.toString());
                String message = shrunkTitle + " removed from wishlist";
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                wishlistManager.removeProductFromWishlist(itemId);
                fab.setImageResource(R.drawable.cart_plus);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "product failed to add into wishlist: " + error.toString());
                String message = shrunkTitle + " failed to remove from wishlist";
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIntoWishList(JSONObject requestBody) {
        ApiCall.postWishlist(ctx, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "product added into wishlist: " + response.toString());
                        String message = shrunkTitle + " added to wishlist";
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                        Product product = makeProduct(requestBody);
                        wishlistManager.addProductToWishlist(itemId, product);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e(TAG, "product failed to add into wishlist: " + error.toString());
                        String message = shrunkTitle + " failed to add into wish list";
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Product makeProduct(JSONObject requestBody) {
        String itemId = requestBody.optString("itemId");
        String title = requestBody.optString("Title");
        String galleryURL = requestBody.optString("Image");
        String viewItemURL = requestBody.optString("Url");
        String postalCode = requestBody.optString("Zip");
        String shippingCost = requestBody.optString("Shipping");
        String currentPrice = requestBody.optString("Price");
        String condition = requestBody.optString("Condition");
        Boolean isWishListed = true;
        String allInfo = requestBody.optString("allInfo");
        return new Product(itemId, title, galleryURL, viewItemURL, postalCode, shippingCost, currentPrice, condition, isWishListed, allInfo);
    }

    private void setPageTitle(JSONObject allInfo) {
        String title = allInfo.optString("title");
        if (title.length() > 25) {
            title = title.substring(0, 25) + " | ...";
        }
        productTitle.setText(title);
    }

    private JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_id", allInfo.optString("itemId"));
            requestBody.put("itemId", allInfo.optString("itemId"));
            requestBody.put("Title", allInfo.optString("title"));
            requestBody.put("Price", allInfo.optString("currentPrice"));
            requestBody.put("Shipping", allInfo.optJSONObject("shippingInfo").optString("shippingCost"));
            requestBody.put("Zip", allInfo.optString("postalCode"));
            requestBody.put("Condition", allInfo.optString("condition"));
            requestBody.put("Image", allInfo.optString("galleryURL"));
            requestBody.put("Url", allInfo.optString("viewItemURL"));
            requestBody.put("allInfo", allInfo.toString());
            // Add more key-value pairs as needed
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}