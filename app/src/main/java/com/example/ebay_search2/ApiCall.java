package com.example.ebay_search2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private static final String USERNAME = "yingtu35";
    private static final String MAX_ROWS = "5";

    private static final String URL = "http://10.0.2.2:3000";

    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=" + query + "&maxRows=" + MAX_ROWS + "&username=" + USERNAME + "&country=US";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void getWishlist(Context ctx, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/get-wish-list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void postWishlist(Context ctx, JSONObject requestBody, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/post-wish-list";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void deleteFromWishlist(Context ctx, String itemId, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/delete-wish-list-item/" + itemId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void getSearchResults(Context ctx, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/search-ebay-example";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
