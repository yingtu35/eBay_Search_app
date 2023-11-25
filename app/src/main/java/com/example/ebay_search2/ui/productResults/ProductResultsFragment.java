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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.ebay_search2.ProductDetailActivity;
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


    private RequestQueue queue;
    private static final String URL = "http://10.0.2.2:3000";

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
//        ProductResultsActivity activity = (ProductResultsActivity) getActivity();

//        result = activity.getIntent().getExtras().getString("result");
//        Log.d(TAG, "onCreateView: "+result);

        //        initialise the wishlistManager
        wishlistManager = WishlistManager.getInstance();

        queue = Volley.newRequestQueue(requireContext());

        Log.d(TAG, "onClick: send http request");
        StringRequest stringRequest = searchResults();
        queue.add(stringRequest);

        return rootView;
    }

//    TODO: include parameters in the request
    private StringRequest searchResults() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "/search-ebay-example",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, response);
                        try {
//            Log.d(TAG, "onCreateView: "+result);
                            if (response != null) {
                                resultJson = new JSONObject(response);
                                //Log.d(TAG, "onResponse: "+response);
//                Log.d(TAG, "onResponse object: "+resultJson);
                                JSONArray items = getItems(resultJson);
//                Log.d(TAG, "item: "+items);
                                addItemIntoProductList(items, productsList);

                                Log.d(TAG, "productsList: " + productsList);
                                //      Create adapter and set it to the RecyclerView
                                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                                productAdaptor = new ProductAdaptor(productsList, ProductResultsFragment.this, ProductResultsFragment.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(productAdaptor);
                                Log.d(TAG, "onCreateView: " + productAdaptor.getItemCount());
                                progressBarLayout.setVisibility(View.GONE);
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
        return stringRequest;
    }

    private JSONArray getItems(JSONObject resultJson) throws JSONException {
        JSONArray items = resultJson
                .getJSONArray("findItemsAdvancedResponse").getJSONObject(0)
                .getJSONArray("searchResult").getJSONObject(0)
                .getJSONArray("item");
        return items;
    }
    private void addItemIntoProductList(JSONArray items, List<Product> productsList) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String itemId = item.getJSONArray("itemId").getString(0);
            String title = item.getJSONArray("title").getString(0);
            String galleryURL = item.getJSONArray("galleryURL").getString(0);
            String viewItemURL = item.getJSONArray("viewItemURL").getString(0);
            String postalCode = item.getJSONArray("postalCode").getString(0);
            String shippingCost = item.getJSONArray("shippingInfo").getJSONObject(0)
                    .getJSONArray("shippingServiceCost").getJSONObject(0)
                    .getString("__value__");
            String currentPrice = item.getJSONArray("sellingStatus").getJSONObject(0)
                    .getJSONArray("currentPrice").getJSONObject(0)
                    .getString("__value__");
            String condition = item.getJSONArray("condition").getJSONObject(0)
                    .getJSONArray("conditionDisplayName").getString(0);
            Boolean isWishListed = wishlistManager.isInWishlist(itemId);
            Product product = new Product(itemId, title, galleryURL, viewItemURL,
                    postalCode, shippingCost, currentPrice, condition, isWishListed);
            productsList.add(product);
        }
    }

    @Override
    public void onProductClick(Product product) {
        // Handle item click here based on the clicked item
        Log.d(TAG, "onItemClick: " + product.getTitle());
        // You can perform any action you need when an item is clicked
//        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//        intent.putExtra("itemId", product.getItemId());
//        intent.putExtra("title", product.getTitle());
//        intent.putExtra("galleryURL", product.getGalleryURL());
//        intent.putExtra("viewItemURL", product.getViewItemURL());
//        intent.putExtra("postalCode", product.getPostalCode());
//        intent.putExtra("shippingCost", product.getShippingCost());
//        intent.putExtra("currentPrice", product.getCurrentPrice());
//        intent.putExtra("condition", product.getCondition());
//        startActivity(intent);
    }

    @Override
    public void onButtonClick(Product product) {
        // Handle the button click here based on the clicked item
        Log.d(TAG, "onButtonClick: " + product.getTitle());
//        check if we are to add the product into the wish list or delete it from the wish list
        if (product.getIsWishListed()) {
//            delete the product from the wish list
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL+"/delete-wish-list-item/"+product.getItemId(),
                    new Response.Listener<String>() {
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
            queue.add(stringRequest);
        }
        else {
            // Create the JSON object to be sent in the request body
            JSONObject requestBody = formRequestBody(product);

//        Add the product into the wish list
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL+"/post-wish-list", requestBody,
                    new Response.Listener<JSONObject>() {
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
            queue.add(jsonObjectRequest);
        }
    }

    private JSONObject formRequestBody(Product product) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_id", product.getItemId());
            requestBody.put("Title", product.getTitle());
            requestBody.put("Price", product.getCurrentPrice());
            requestBody.put("Shipping", product.getShippingCost());
            requestBody.put("Zip", product.getPostalCode());
            requestBody.put("Condition", product.getCondition());
            requestBody.put("Image", product.getGalleryURL());
            requestBody.put("Url", product.getViewItemURL());
            // Add more key-value pairs as needed
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }


}