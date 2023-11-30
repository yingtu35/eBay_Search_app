package com.example.ebay_search2.ui.productDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject allInfo;
    private static final String TAG = "ProductDetailTab";
    private LinearLayout imageContainer;
    private LinearLayout specificationsContainer;
    private TextView productTitle;
    private TextView productSubtitle;
    private TextView productPrice;
    private TextView productBrand;
    private int screenWidth;
    private String title;
    private String price;
    private String shipping;
    private String brand = "N/A";
    private JSONArray productImages;
    private JSONArray itemSpecifics;

    public ProductDetailTab() {
        // Required empty public constructor
    }

    public ProductDetailTab(JSONObject allInfo) {
        this.allInfo = allInfo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailTab newInstance(String param1, String param2) {
        ProductDetailTab fragment = new ProductDetailTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fragment_product_detail_tab, container, false);
        rootView.setTag(TAG);

        imageContainer = rootView.findViewById(R.id.imageContainer);
        specificationsContainer = rootView.findViewById(R.id.specificationsContainer);
        productTitle = rootView.findViewById(R.id.productTitle);
        productSubtitle = rootView.findViewById(R.id.productSubtitle);
        productPrice = rootView.findViewById(R.id.productPrice);
        productBrand = rootView.findViewById(R.id.productBrand);

//        get width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        Log.d(TAG, "allInfo: " + allInfo.toString());
        getDetails();
        return rootView;
    }

    private void getDetails() {
        Log.d(TAG, "getDetails: " + allInfo.optString("itemId"));
        ApiCall.getSingleItem(getContext(), allInfo.optString("itemId"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                updateDetails(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getDetails: " + error.getMessage());
            }
        });
    }

    private void updateDetails(JSONObject response) {
        productImages = response.optJSONArray("productImages");
        itemSpecifics = response.optJSONArray("ItemSpecifics");
        Log.d(TAG, "updateDetails: " + productImages.toString());
        Log.d(TAG, "updateDetails: " + itemSpecifics.toString());
        productTitle.setText(allInfo.optString("title"));
        productPrice.setText("$" + allInfo.optString("currentPrice"));
        updateProductSubtitle();
        updateProductImages();
        updateItemSpecifics();
    }

    private void updateProductSubtitle() {
        String subtitle = "";
        String shippingCost = allInfo.optJSONObject("shippingInfo").optString("shippingCost");
        String currentPrice = "$" + allInfo.optString("currentPrice");
        if (shippingCost.equals("0.0")) {
            subtitle = currentPrice + " with Free Shipping";
        } else {
            subtitle = currentPrice + " with $" + shippingCost + " Shipping";
        }
        productSubtitle.setText(subtitle);
    }

    private void updateProductImages() {
        for (int i = 0; i < productImages.length(); i++) {
            ImageView imageView = new ImageView(getContext());
// Set layout parameters for the ImageView (adjust as needed)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    screenWidth,
                    screenWidth);
            imageView.setLayoutParams(layoutParams);
            Picasso.with(getContext()).load(productImages.optString(i)).fit().into(imageView);
            imageContainer.addView(imageView);
        }
    }

    private void updateItemSpecifics() {
        LinkedList<String> itemSpecificsList = new LinkedList<>();
        for (int i = 0; i < itemSpecifics.length(); i++) {
            JSONObject itemSpecific = itemSpecifics.optJSONObject(i);
            String name = itemSpecific.optString("name");
            if (name.equals("Brand")) {
                itemSpecificsList.addFirst("· " + itemSpecific.optString("value"));
                brand = itemSpecific.optString("value");
                productBrand.setText(brand);
            } else {
                itemSpecificsList.add("· " + itemSpecific.optString("value"));
            }
        }
            // Create a new TextView
        for (String item : itemSpecificsList) {
            TextView textView = new TextView(getContext());
            textView.setText(item);

            // Add the TextView to the LinearLayout
            specificationsContainer.addView(textView);
        }

    }
}