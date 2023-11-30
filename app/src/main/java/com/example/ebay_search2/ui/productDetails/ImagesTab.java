package com.example.ebay_search2.ui.productDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagesTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ImagesTab";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject allInfo;

    public ImagesTab() {
        // Required empty public constructor
    }

    public ImagesTab(JSONObject allInfo) {
        this.allInfo = allInfo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagesTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesTab newInstance(String param1, String param2) {
        ImagesTab fragment = new ImagesTab();
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
                inflater.inflate(R.layout.fragment_images_tab, container, false);
        rootView.setTag(TAG);

        getPhotos();

        return rootView;
    }

    private void getPhotos() {
        ApiCall.getSingleItemPhotos(getContext(), allInfo.optString("title"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getDetails: " + error.getMessage());
            }
        });
    }
}