package com.example.ebay_search2.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebay_search2.R;
import com.example.ebay_search2.ui.ProductAdaptor;
import com.example.ebay_search2.ui.WishlistManager;
import com.example.ebay_search2.databinding.FragmentDashboardBinding;
import com.example.ebay_search2.ui.Product;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements ProductAdaptor.OnButtonClickListener, ProductAdaptor.OnProductClickListener {

    private static final String URL = "http://10.0.2.2:3000";
    private static final String TAG = "DashboardFragment";
    private static final int SPAN_COUNT = 2;
    private RequestQueue queue;

    private ProductAdaptor productAdaptor;
    private RecyclerView recyclerView;
    private RelativeLayout wishlistTotalLayout;
    private TextView noItemsInWishlistTextView;
    private TextView wishlistTotalItems;
    private TextView wishlistTotalPrice;

    private List<Product> wishList;

    private FragmentDashboardBinding binding;
    private WishlistManager wishlistManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.wishListRecyclerView);
        noItemsInWishlistTextView = root.findViewById(R.id.noItemsInWishlistTextView);
        wishlistTotalLayout = root.findViewById(R.id.wishlistTotalLayout);
        wishlistTotalItems = root.findViewById(R.id.wishlistTotalItems);
        wishlistTotalPrice = root.findViewById(R.id.wishlistTotalPrice);

        // set up request queue
        queue = Volley.newRequestQueue(requireContext());
        queue.start();

//        retrieve wishlist from wishListManager
        wishlistManager = WishlistManager.getInstance();
        wishList = new ArrayList<>(wishlistManager.getWishlist());

        // set up the productAdaptor
        // set up the recycler view with grid layout
        productAdaptor = new ProductAdaptor(wishList, DashboardFragment.this, DashboardFragment.this);
        recyclerView.setAdapter(productAdaptor);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), SPAN_COUNT));
        Log.d(TAG, "itemCount: " + productAdaptor.getItemCount());

        updateWishlistTotal();

////        send a http request
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL+"/get-wish-list",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                      create a wishList
//
//                        Log.d(TAG, response);
//                        try {
//                            if (response != null) {
//                                // parse the response
//                                JSONArray items = new JSONArray(response);
//                                Log.d(TAG, "items: "+items);
//                                addItemsInWishList(wishList, items);
//                            }
//                        }
//                        catch(JSONException e) {
//                            e.printStackTrace();
//                        }
//                        // set up the productAdaptor
//                        productAdaptor = new ProductAdaptor(wishList, DashboardFragment.this, DashboardFragment.this);
//                        // set up the recycler view with grid layout
//                        recyclerView.setAdapter(productAdaptor);
//                        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), SPAN_COUNT));
//                        Log.d(TAG, "itemCount: " + productAdaptor.getItemCount());
//                        // set up the click listener
//                        // set up the long click listener
//                        // set up the swipe listener
//                        // set up the swipe to delete listener
//                        // set up the swipe to share listener
//                        // set up the swipe to detail listener
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, error.toString());
//            }
//        });

//        queue.add(stringRequest);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queue.stop();
        binding = null;
    }

//    private void addItemsInWishList(List<WishListItem> wishList, JSONArray items) throws JSONException {
//        Log.d(TAG, "addItemsInWishList: " + items.length());
//        for (int i = 0; i < items.length(); i++) {
//            JSONObject item = items.getJSONObject(i);
//            String itemId = item.getString("_id");
//            String title = item.has("Title") ? item.getString("Title") : "unknown";
//            String price = item.has("Price") ? item.getString("Price") : "unknown";
//            String shipping = item.has("Shipping") ? item.getString("Shipping") : "unknown";
//            String zip = item.has("Zip") ? item.getString("Zip") : "unknown";
//            String condition = item.has("Condition") ? item.getString("Condition") : "unknown";
//            String image = item.has("Image") ? item.getString("Image") : "";
//            String url = item.has("Url") ? item.getString("Url") : "";
//            WishListItem wishListItem = new WishListItem(itemId, title, image, url, zip, shipping, price, condition);
//            Log.d(TAG, "addItemsInWishList: " + wishListItem.toString());
//            wishList.add(wishListItem);
//        }
//    }

    @Override
    public void onButtonClick(Product wishListItem) {
        // Handle the button click here based on the clicked item
        Log.d(TAG, "onButtonClick: " + wishListItem.getTitle());
//        delete the wishListItem from the wishList
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL+"/delete-wish-list-item/"+wishListItem.getItemId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        Log.d(TAG, "product removed into wishlist: " + response.toString());
//                      add a toast message indicating product successfully added into wish list
                        String message = wishListItem.getTitle() + " was removed from the wish list";
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                      remove the product from the wish list
                        wishlistManager.removeProductFromWishlist(wishListItem.getItemId());
                        updateWishlistTotal();
//                      update the isWishListed field of the product
                        int position = wishList.indexOf(wishListItem);
                        productAdaptor.removeItem(position);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "delete error: " + error.toString());
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onProductClick(Product wishListItem) {
        // Handle item click here based on the clicked item
        Log.d(TAG, "onItemClick: " + wishListItem.getTitle());
        // You can perform any action you need when an item is clicked
    }

    private void updateWishlistTotal() {
        wishlistTotalItems.setText("Wishlist total (" + wishlistManager.getWishlistSize() + " items):");
        wishlistTotalPrice.setText("$" + wishlistManager.getTotalPrice());
        if (wishlistManager.getWishlistSize() == 0) {
            wishlistTotalLayout.setVisibility(View.GONE);
            noItemsInWishlistTextView.setVisibility(View.VISIBLE);
        }
        else {
            wishlistTotalLayout.setVisibility(View.VISIBLE);
            noItemsInWishlistTextView.setVisibility(View.GONE);
        }
    }
}