package com.example.ebay_search2.ui.productDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private LinearLayout progressBarLayout;
    private ScrollView imagesScrollView;
    private LinearLayout imagesContainer;
    private TextView noPhotosFoundTextView;
    private int screenWidth;

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

        progressBarLayout = rootView.findViewById(R.id.progressBarLayout);
        imagesScrollView = rootView.findViewById(R.id.imagesScrollView);
        imagesContainer = rootView.findViewById(R.id.imagesContainer);
        noPhotosFoundTextView = rootView.findViewById(R.id.noPhotosFoundTextView);

        //        get width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        getPhotos();

        return rootView;
    }

    private void getPhotos() {
        ApiCall.getSingleItemPhotos(getContext(), allInfo.optString("title"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
                updatePhotos(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "getDetails: " + error.toString());
            }
        });
    }

    private void updatePhotos(JSONObject response) {
        JSONArray thumbnailLinks = response.optJSONArray("thumbnailLinks");
        if (thumbnailLinks.length() == 0) {
            progressBarLayout.setVisibility(View.GONE);
            noPhotosFoundTextView.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < thumbnailLinks.length(); i++) {
            String link = thumbnailLinks.optString(i) + ".jpg";
            Log.d(TAG, "updatePhotos: " + link);
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    screenWidth,
                    screenWidth);
            layoutParams.setMargins(0, 0, 0, 20);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackground(getResources().getDrawable(R.drawable.image_border));
            imageView.setElevation(10);

            Picasso.with(getContext()).load(link).centerInside().fit().into(imageView);
            imagesContainer.addView(imageView);
        }

        progressBarLayout.setVisibility(View.GONE);
        imagesScrollView.setVisibility(View.VISIBLE);
    }
}