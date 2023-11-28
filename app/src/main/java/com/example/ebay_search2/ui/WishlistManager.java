package com.example.ebay_search2.ui;
import android.util.Log;

//import com.example.ebay_search2.ui.dashboard.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WishlistManager {
    private static final String TAG = "WishlistManager";
    private static WishlistManager instance;
    private Map<String, Product> wishlistMap;
    private double totalPrice;


    // Private constructor to prevent instantiation from outside the class
    private WishlistManager() {
        wishlistMap = new HashMap<>();
        totalPrice = 0.0;
    }

    // Method to get the singleton instance
    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    // Method to add a wishListItem to the wishlist
    public void addProductToWishlist(String itemId, Product wishListItem) {
        wishlistMap.put(itemId, wishListItem);
        totalPrice += Double.parseDouble(wishListItem.getCurrentPrice());
        Log.d(TAG, "addProductToWishlist: " + itemId);
    }

    // Method to remove a wishListItem from the wishlist
    public void removeProductFromWishlist(String itemId) {
        if (!wishlistMap.containsKey(itemId)) {
            return;
        }
        double price = Double.parseDouble(wishlistMap.get(itemId).getCurrentPrice());
        wishlistMap.remove(itemId);
        totalPrice -= price;
        Log.d(TAG, "removeProductFromWishlist: " + itemId);
    }

    // Method to check if an itemId is in the wishlist
    public boolean isInWishlist(String itemId) {
        return wishlistMap.containsKey(itemId);
    }

    // Method to get the entire wishlist map
    public Collection<Product> getWishlist() {
        return wishlistMap.values();
    }

    // Method to get a specific wishListItem from the wishlist
    public Product getProductFromWishlist(String itemId) {
        return wishlistMap.get(itemId);
    }

    public int getWishlistSize() {
        return wishlistMap.size();
    }
//    TODO: totalPrice should have fixed precision
    public double getTotalPrice() {
        return totalPrice;
    }
}
