package com.example.ebay_search2.ui.productDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.R;
import com.example.ebay_search2.ui.SimilarProductsAdapter;
import com.example.ebay_search2.ui.SimilarProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SimilarProductsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimilarProductsTab extends Fragment implements SimilarProductsAdapter.OnProductClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject allInfo;
    private static final String TAG = "SimilarProductsTab";
    private Spinner sortingCategorySpinner;
    private Spinner sortingDirectionSpinner;
    private List<SimilarProduct> similarProductList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SimilarProductsAdapter similarProductsAdapter;
    private RecyclerView similarProductsRecyclerView;

    public SimilarProductsTab() {
        // Required empty public constructor
    }

    public SimilarProductsTab(JSONObject allInfo) {
        this.allInfo = allInfo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimilarProductsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static SimilarProductsTab newInstance(String param1, String param2) {
        SimilarProductsTab fragment = new SimilarProductsTab();
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
                inflater.inflate(R.layout.fragment_similar_products_tab, container, false);
        rootView.setTag(TAG);

        similarProductsRecyclerView = (RecyclerView) rootView.findViewById(R.id.similarProductsRecyclerView);

        sortingCategorySpinner = (Spinner) rootView.findViewById(R.id.sorting_category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> sortingCategoryAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sorting_categories,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        sortingCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingCategorySpinner.setAdapter(sortingCategoryAdapter);

        sortingDirectionSpinner = (Spinner) rootView.findViewById(R.id.sorting_direction_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> sortingDirectionsAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sorting_directions,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        sortingDirectionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingDirectionSpinner.setAdapter(sortingDirectionsAdapter);

        getSimilarProducts();
//        TODO: add sorting functionality
        return rootView;
    }

    private void getSimilarProducts() {
        ApiCall.getSimilarProducts(getContext(), allInfo.optString("itemId"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response.toString());
                addSimilarProducts(response);

                linearLayoutManager = new LinearLayoutManager(getContext());
                similarProductsAdapter = new SimilarProductsAdapter(similarProductList, SimilarProductsTab.this);
                similarProductsRecyclerView.setLayoutManager(linearLayoutManager);
                similarProductsRecyclerView.setAdapter(similarProductsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
    }

    private void addSimilarProducts(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            JSONObject product = response.optJSONObject(i);
            Log.d(TAG, "addSimilarProducts: " + product.toString());
            String title = product.optString("productName");
            String productImage = product.optString("productImage");
            String price = product.optString("price");
            String shippingCost = product.optString("shippingCost");
            String daysLeft = product.optString("daysLeft");
            String itemURL = product.optString("itemURL");
            SimilarProduct similarProduct = new SimilarProduct(title, productImage, shippingCost, daysLeft, price, itemURL);
            similarProductList.add(similarProduct);
        }
    }

    @Override
    public void onProductClick(SimilarProduct similarProduct) {
        Log.d(TAG, "onProductClick: clicked");
//        link to page outside the app
        String url = similarProduct.getItemURL();
        Log.d(TAG, "onProductClick: " + url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(url));
        startActivity(intent);
    }
}