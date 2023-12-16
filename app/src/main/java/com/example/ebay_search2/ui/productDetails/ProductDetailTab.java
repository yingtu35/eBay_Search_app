package com.example.ebay_search2.ui.productDetails;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
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

    // Define an interface
    public interface OnDataPassListener {
        void onDataPass(JSONObject data); // You can change the type of data as needed
    }

    private OnDataPassListener dataPassListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject allInfo;
    private static final String TAG = "ProductDetailTab";
    private View rootView;
    private LinearLayout imageContainer;
    private LinearLayout specificationsContainer;
    private TextView productTitle;
    private TextView productSubtitle;
    private TextView productPrice;
    private TextView productBrand;
    private LinearLayout progressBarLayout;
    private ScrollView scrollView;
    private TableRow productPriceRow;
    private TableRow productBrandRow;
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Ensure that the hosting activity implements the interface
        try {
            dataPassListener = (OnDataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataPassListener");
        }
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
//        TODO: Hide a section if data is not available
//        TODO: Display no results if all data is not available
        rootView =
                inflater.inflate(R.layout.fragment_product_detail_tab, container, false);
        rootView.setTag(TAG);

        bindView();

//        get width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        Log.d(TAG, "allInfo: " + allInfo.toString());
        getDetails();
        return rootView;
    }

    private void bindView() {
        progressBarLayout = rootView.findViewById(R.id.progressBarLayout);
        scrollView = rootView.findViewById(R.id.productDetailScrollView);
        imageContainer = rootView.findViewById(R.id.imageContainer);
        productPriceRow = rootView.findViewById(R.id.productPriceRow);
        productBrandRow = rootView.findViewById(R.id.productBrandRow);
        specificationsContainer = rootView.findViewById(R.id.specificationsContainer);
        productTitle = rootView.findViewById(R.id.productTitle);
        productSubtitle = rootView.findViewById(R.id.productSubtitle);
        productPrice = rootView.findViewById(R.id.productPrice);
        productBrand = rootView.findViewById(R.id.productBrand);
    }

    private void getDetails() {
        Log.d(TAG, "getDetails: " + allInfo.optString("itemId"));
        ApiCall.getSingleItem(getContext(), allInfo.optString("itemId"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                updateDetails(response);
                progressBarLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getDetails: " + error.getMessage());
            }
        });
    }


//    TODO: skip rows if data is not available
    private void updateDetails(JSONObject response) {
        sendReturnPolicyToActivity(response);
        productImages = response.optJSONArray("productImages");
        itemSpecifics = response.optJSONArray("ItemSpecifics");
        Log.d(TAG, "updateDetails: " + productImages.toString());
        Log.d(TAG, "updateDetails: " + itemSpecifics.toString());
        if (allInfo.has("title") && !allInfo.optString("title").equals("")) {
            productPrice.setText("$" + allInfo.optString("currentPrice"));
        }
        productTitle.setText(allInfo.optString("title"));
        if (allInfo.has("currentPrice") && !allInfo.optString("currentPrice").equals("")) {
            productPrice.setText("$" + allInfo.optString("currentPrice"));
        }
//        productPrice.setText("$" + allInfo.optString("currentPrice"));
        updateProductSubtitle();
        updateProductImages();
        updateItemSpecifics();
        updateVisibility();
    }

    private void updateVisibility() {
        if (productImages.length() == 0) {
            imageContainer.setVisibility(View.GONE);
        }
        if (productTitle.getText().length() == 0) {
            productTitle.setVisibility(View.GONE);
        }
        if (productSubtitle.getText().length() == 0) {
            productSubtitle.setVisibility(View.GONE);
        }
        if (productPrice.getText().length() == 0) {
            productPriceRow.setVisibility(View.GONE);
        }
        if (productBrand.getText().length() == 0) {
            productBrandRow.setVisibility(View.GONE);
        }
if (specificationsContainer.getChildCount() == 0) {
            specificationsContainer.setVisibility(View.GONE);
        }


    }

//    private boolean dataExists(JSONObject response) {
//        boolean dataExists = false;
//        if (response.has("productImages")) {
//            dataExists = true;
//        }
//        else {
//            imageContainer.setVisibility(View.GONE);
//        }
//
//        if (response.has("Title")) {
//            dataExists = true;
//        }
//        else {
//            productTitle.setVisibility(View.GONE);
//        }
//
//        if (allInfo.has("currentPrice") || (allInfo.has("shippingInfo") && allInfo.optJSONObject("shippingInfo").has("shippingCost"))) {
//            dataExists = true;
//        }
//        else {
//            productSubtitle.setVisibility(View.GONE);
//        }
//
//        if (allInfo.has("currentPrice")) {
//            dataExists = true;
//        }
//        else {
//            productPriceRow.setVisibility(View.GONE);
//        }
//
//        if (response.optString("shippingInfo").equals("")) {
//            dataExists = false;
//        }
//        if (response.optString("ItemSpecifics").equals("")) {
//            dataExists = false;
//        }
//        return dataExists;
//    }

//    private void updateShipping(JSONObject response) {
//        Log.d(TAG, "updateShipping: called");
//        JSONObject returnPolicy = response.optJSONObject("returnPolicy");
//        ShippingTab fragmentB = (ShippingTab) getChildFragmentManager().findFragmentById(R.id.fragment_shipping_tab);
//
//        if (fragmentB != null) {
//            fragmentB.updateReturnPolicy(returnPolicy);
//            fragmentB.updateVisibility();
//        } else {
//            Log.d(TAG, "updateShipping: fragmentB is null");
//
//        }
//    }

    private void updateProductSubtitle() {
        String subtitle = "";
        String shippingCost = allInfo.has("shippingInfo") ? allInfo.optJSONObject("shippingInfo").optString("shippingCost") : "";
        String currentPrice = allInfo.has("currentPrice") ? "$" + allInfo.optString("currentPrice") : "";
        if (shippingCost.equals("0.0")) {
            subtitle = currentPrice + " with Free Shipping";
        } else if (!shippingCost.equals("")){
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

    private void sendReturnPolicyToActivity(JSONObject response) {
        JSONObject returnPolicy = response.optJSONObject("returnPolicy");
        passReturnPolicyToActivity(returnPolicy);
    }
    // Method to pass data to the activity
    private void passReturnPolicyToActivity(JSONObject returnPolicy) {
        Log.d(TAG, "passReturnPolicyToActivity: " + returnPolicy.toString());
        if (dataPassListener != null && returnPolicy != null) {
            dataPassListener.onDataPass(returnPolicy);
        }
    }
}