package com.example.ebay_search2;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static final String fakePostal = "90009";

    private static final String TAG = "ApiCall";

    private static final String USERNAME = "yingtu35";
    private static final String MAX_ROWS = "5";

//    private static final String URL = "http://10.0.2.2:3000";
    private static final String URL = "http://ebaysearch.us-east-2.elasticbeanstalk.com";
    private static final String IPINFOTOKEN = "543916f91709cc";

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

    public static void getSearchResults(Context ctx, JSONObject jsonRequest, Response.Listener<JSONArray>
            listener, Response.ErrorListener errorListener) {
        Log.d(TAG, "getSearchResults: " + jsonRequest);
        try {
            if (jsonRequest.get("locationOption").equals("current")) {
                // use current location
                Log.d(TAG, "getSearchResults: " + jsonRequest.get("locationOption"));
                getCurrentLocation(ctx, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: " + response.optString("postal"));
                            jsonRequest.put("zipCode", response.has("postal") ? response.optString("postal") : fakePostal);
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: " + e.getMessage());
                        }
                        String url = formatURL(jsonRequest);
                        Log.d(TAG, "getSearchResults: " + url);
                        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                listener, errorListener);
                        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
                    }
                }, new Response.ErrorListener() {
//                  give fake postal code if location is not available
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "getCurrentLocation: " + error.getMessage());
                        try {
                            jsonRequest.put("zipCode", fakePostal);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String url = formatURL(jsonRequest);
                        Log.d(TAG, "getSearchResults: " + url);
                        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                listener, errorListener);
                        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
                    }
                });
                return;
            }
            else {
                // use zip code
                String url = formatURL(jsonRequest);
                Log.d(TAG, "getSearchResults: " + url);
                JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        listener, errorListener);
                ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
            }
        } catch (Exception e) {
            Log.d(TAG, "getSearchResults: " + e.getMessage());
        }
    }

    public static void getSearchResultsExample(Context ctx, Response.Listener<JSONArray>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/search-ebay-example";
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void getSingleItem(Context ctx, String itemId, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/get-single-item/" + itemId;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void getSingleItemPhotos(Context ctx, String title, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/search-google" + "?keyword=" + title;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void getSimilarProducts(Context ctx, String itemId, Response.Listener<JSONArray>
            listener, Response.ErrorListener errorListener) {
        String url = URL + "/search-similar" + "?itemId=" + itemId;
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    private static void getCurrentLocation(Context ctx, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        String url = "https://ipinfo.io/json?token=" + IPINFOTOKEN;
        Log.d(TAG, "getCurrentLocation: " + url);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    private static String formatURL(JSONObject jsonRequest) {
        String url = URL + "/search-ebay" + "?keyword=" + jsonRequest.optString("keyword")
                + "&category=" + jsonRequest.optString("category")
                + "&New=" + jsonRequest.optString("New")
                + "&Used=" + jsonRequest.optString("Used")
                + "&Unspecified=" + jsonRequest.optString("Unspecified")
//                + "&conditions=" + jsonRequest.optString("conditions")
                + "&localPickup=" + jsonRequest.optString("localPickup")
                + "&freeShipping=" + jsonRequest.optString("freeShipping")
                + "&distance=" + jsonRequest.optString("distance")
                + "&locationOption=" + jsonRequest.optString("locationOption");
        if (jsonRequest.has("otherLocation")) {
            url += "&otherLocation=" + jsonRequest.optString("otherLocation");
        }
        else {
            url += "&zipCode=" + jsonRequest.optString("zipCode");
        }
        return url;
    }
}
