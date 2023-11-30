package com.example.ebay_search2.ui.productResults;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.ebay_search2.ProductDetailActivity;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.ProductDetailActivity;
import com.example.ebay_search2.ProductResultsActivity;
import com.example.ebay_search2.R;
import com.example.ebay_search2.ui.WishlistManager;
import com.example.ebay_search2.databinding.FragmentProductResultsBinding;
import com.example.ebay_search2.ui.Product;
import com.example.ebay_search2.ui.ProductAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class ProductResultsFragment extends Fragment implements ProductAdaptor.OnProductClickListener{
public class ProductResultsFragment extends Fragment implements ProductAdaptor.OnProductClickListener, ProductAdaptor.OnButtonClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int SPAN_COUNT = 2;

    private static String TAG = "ProductResultsFragment";
//    private String result;

    private JSONObject resultJson;
    private List<Product> productsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductAdaptor productAdaptor;

    private WishlistManager wishlistManager;
    private LinearLayout progressBarLayout;
    private String searchParameters;

    public ProductResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductResultsFragment newInstance(String param1, String param2) {
        ProductResultsFragment fragment = new ProductResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_product_results, container, false);
        rootView.setTag(TAG);

        progressBarLayout = (LinearLayout) rootView.findViewById(R.id.progressBarLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.productResultsRecyclerView);

//        TODO: get the form data from the ProductResultsActivity (uncomment the following line)
        ProductResultsActivity activity = (ProductResultsActivity) getActivity();

        searchParameters = activity.getIntent().getExtras().getString("parameters");
        Log.d(TAG, "onCreateView: "+searchParameters);

        //        initialise the wishlistManager
        wishlistManager = WishlistManager.getInstance();

        Log.d(TAG, "onClick: send http request");
        searchResults(searchParameters);

//        searchResultsExample();
        return rootView;
    }

    private void searchResultsExample() {
        ApiCall.getSearchResultsExample(requireContext(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Display the first 500 characters of the response string.
                Log.d(TAG, response.toString());
                Log.d(TAG, "onResponse: "+response.length());
//            Log.d(TAG, "onCreateView: "+result);
                if (response.length() != 0) {
                    try {
                        addItemIntoProductList(response);
                        Log.d(TAG, "productsList: " + productsList);
//                          Create adapter and set it to the RecyclerView
                        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                        productAdaptor = new ProductAdaptor(productsList, ProductResultsFragment.this, ProductResultsFragment.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(productAdaptor);
                        Log.d(TAG, "onCreateView: " + productAdaptor.getItemCount());
                        progressBarLayout.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "searchResults error: " + error.toString());
            }
        });
    }

    private void searchResults(String searchParameters) {
        try {
            JSONObject jsonRequest = new JSONObject(searchParameters);
            Log.d(TAG, "searchResults: " + jsonRequest);

            ApiCall.getSearchResults(requireContext(), jsonRequest, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Display the first 500 characters of the response string.
                    Log.d(TAG, response.toString());
                    Log.d(TAG, "onResponse: "+response.length());
//            Log.d(TAG, "onCreateView: "+result);
                    if (response.length() != 0) {
                        try {
                            addItemIntoProductList(response);
                            Log.d(TAG, "productsList: " + productsList);
//                          Create adapter and set it to the RecyclerView
                            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                            productAdaptor = new ProductAdaptor(productsList, ProductResultsFragment.this, ProductResultsFragment.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(productAdaptor);
                            Log.d(TAG, "onCreateView: " + productAdaptor.getItemCount());
                            progressBarLayout.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "searchResults error: " + error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addItemIntoProductList(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String allInfo = item.toString();
            String itemId = item.getString("itemId").toString();
            String title = item.getString("title").toString();
            String galleryURL = item.getString("galleryURL").toString();
            String viewItemURL = item.getString("viewItemURL").toString();
            String postalCode = item.getString("postalCode").toString();
            String shippingCost = item.getJSONObject("shippingInfo").getString("shippingCost").toString();
            String currentPrice = item.getString("currentPrice").toString();
            String condition = item.getString("condition").toString();
            Boolean isWishListed = wishlistManager.isInWishlist(itemId);
            Product product = new Product(itemId, title, galleryURL, viewItemURL,
                    postalCode, shippingCost, currentPrice, condition, isWishListed, allInfo);
            productsList.add(product);
        }
    }

    @Override
    public void onProductClick(Product product) {
        // Handle item click here based on the clicked item
        Log.d(TAG, "onItemClick: " + product.getTitle());
        // You can perform any action you need when an item is clicked
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//        intent.putExtra("details", "details");
        intent.putExtra("allInfo", product.getAllInfo());
//        intent.putExtra("googleImage", "googleImage");
        startActivity(intent);
    }

    @Override
    public void onButtonClick(Product product) {
        // Handle the button click here based on the clicked item
        Log.d(TAG, "onButtonClick: " + product.getTitle());
//        check if we are to add the product into the wish list or delete it from the wish list
        if (product.getIsWishListed()) {
            deleteFromWishlist(product);
        }
        else {
            JSONObject requestBody = formRequestBody(product);
            addIntoWishList(product, requestBody);
        }
    }

    private JSONObject formRequestBody(Product product) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_id", product.getItemId());
            requestBody.put("itemId", product.getItemId());
            requestBody.put("Title", product.getTitle());
            requestBody.put("Price", product.getCurrentPrice());
            requestBody.put("Shipping", product.getShippingCost());
            requestBody.put("Zip", product.getPostalCode());
            requestBody.put("Condition", product.getCondition());
            requestBody.put("Image", product.getGalleryURL());
            requestBody.put("Url", product.getViewItemURL());
            requestBody.put("allInfo", product.getAllInfo());
            // Add more key-value pairs as needed
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    private void deleteFromWishlist(Product product) {
        ApiCall.deleteFromWishlist(requireContext(), product.getItemId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response
                Log.d(TAG, "product removed into wishlist: " + response.toString());
                String message = product.getTitle() + " was removed from the wish list";
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                wishlistManager.removeProductFromWishlist(product.getItemId());
//                      update the isWishListed field of the product
                int position = productsList.indexOf(product);
                productAdaptor.setProductWishListed(position, false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "product failed to add into wishlist: " + error.toString());
                String message = product.getTitle() + " failed to remove from wish list";
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIntoWishList(Product product, JSONObject requestBody) {
        ApiCall.postWishlist(requireContext(), requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "product added into wishlist: " + response.toString());
                        String message = product.getTitle() + " was added to the wish list";
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        wishlistManager.addProductToWishlist(product.getItemId(), product);
//                      update the isWishListed field of the product
                        int position = productsList.indexOf(product);
                        productAdaptor.setProductWishListed(position, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e(TAG, "product failed to add into wishlist: " + error.toString());
                        String message = product.getTitle() + " failed to add into wish list";
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}