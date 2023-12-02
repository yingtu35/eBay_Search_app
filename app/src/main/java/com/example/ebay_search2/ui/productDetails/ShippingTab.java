package com.example.ebay_search2.ui.productDetails;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ebay_search2.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private static String TAG = "ShippingTab";
    private JSONObject allInfo;
    private LinearLayout progressBarLayout;
    private LinearLayout shippingDetailsLayout;
    private TextView storeName;
    private TextView feedbackScore;
    private TextView popularity;
    private ImageView feedbackRatingStar;
    private TextView shippingCost;
    private TextView globalShipping;
    private TextView handlingTime;
    private TextView policyView;
    private TextView returnsWithinView;
    private TextView refundModeView;
    private TextView shippedByView;
    private int popularityScore;
    private String policy;
    private String returnsWithin;
    private String refundMode;
    private String shippedBy;
    private ProgressBar circularScore;

//    private String storeName;
//    private String feedbackScore;
//    private String popularityScore;
//    private String feedBackRatingStar;
//    private String shippingCost;
//    private String globalShipping;
//    private String handlingTime;



    public ShippingTab() {
        // Required empty public constructor
    }

    public ShippingTab(JSONObject allInfo) {
        this.allInfo = allInfo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingTab.
     */
    public static ShippingTab newInstance(String param1, String param2) {
        ShippingTab fragment = new ShippingTab();
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
//        TODO: Hide a section if data is not available
//        TODO: Display no results if all data is not available
//        TODO: Skip a row if data is not available
//        TODO: Slide animation when clicking on store name
        // Inflate the layout for this fragment
        rootView = (View) inflater.inflate(R.layout.fragment_shipping_tab, container, false);

        bindViews();

        Log.d(TAG, "onCreateView: " + allInfo.toString());
        if (allInfo != null) {
            updateShipping(allInfo);
        }

        storeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + storeName.getText());
//        link to page outside the app
                String url = allInfo.optJSONObject("sellerInfo").optString("storeURL");
                Log.d(TAG, "onProductClick: " + url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse(url));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void bindViews() {
        storeName = rootView.findViewById(R.id.storeName);
        storeName.setPaintFlags(storeName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        feedbackScore = rootView.findViewById(R.id.feedbackScore);
        circularScore = rootView.findViewById(R.id.circularScore);
        popularity = rootView.findViewById(R.id.popularity);
        feedbackRatingStar = rootView.findViewById(R.id.feedbackStar);
        shippingCost = rootView.findViewById(R.id.shippingCost);
        globalShipping = rootView.findViewById(R.id.globalShipping);
        handlingTime = rootView.findViewById(R.id.handlingTime);
        policyView = rootView.findViewById(R.id.policy);
        returnsWithinView = rootView.findViewById(R.id.returnsWithin);
        refundModeView = rootView.findViewById(R.id.refundMode);
        shippedByView = rootView.findViewById(R.id.shippedBy);

        progressBarLayout = rootView.findViewById(R.id.progressBarLayout);
        shippingDetailsLayout = rootView.findViewById(R.id.shippingDetailsLayout);
    }

    private void updateShipping(JSONObject allInfo) {
        storeName.setText(allInfo.optJSONObject("sellerInfo").optString("storeName"));
        feedbackScore.setText(allInfo.optJSONObject("sellerInfo").optString("feedbackScore"));
        popularityScore = (int) Math.round(Double.parseDouble(allInfo.optJSONObject("sellerInfo").optString("popularity")));
        adjustPopularityPaddingStart();
        popularity.setText(popularityScore + "%");
        circularScore.setProgress(popularityScore);
        updateFeedbackRatingStar(allInfo.optJSONObject("sellerInfo").optString("feedbackRatingStar"));
        Double shipCost = Double.parseDouble(allInfo.optJSONObject("shippingInfo").optString("shippingCost"));
        shippingCost.setText(shipCost == 0.0 ? "Free shipping" : shipCost.toString());
        globalShipping.setText(allInfo.optJSONObject("shippingInfo").optString("shipToLocations").equals("Worldwide") ? "Yes" : "No");
        handlingTime.setText(allInfo.optJSONObject("shippingInfo").optString("handlingTime"));
    }

    private void adjustPopularityPaddingStart() {
        if (popularityScore == 100) {
            popularity.setPadding(8, 20, 0, 0);
        }
        else if (popularityScore >= 10) {
            popularity.setPadding(15, 20, 0, 0);
        }
        else {
            popularity.setPadding(25, 20, 0, 0);
        }
    }

    private void updateFeedbackRatingStar(String feedBackRatingStar) {
        Log.d(TAG, "updateFeedbackRatingStar: " + feedBackRatingStar);
        boolean isShooting = feedBackRatingStar.length() > 8 && feedBackRatingStar.substring(feedBackRatingStar.length() - 8).equals("Shooting");
        int tintColor;
        switch (feedBackRatingStar) {
            case "Yellow":
                tintColor = ContextCompat.getColor(getContext(), R.color.yellow);
                break;
            case "Blue":
                tintColor = ContextCompat.getColor(getContext(), R.color.blue);
                break;
            case "Turquoise":
                tintColor = ContextCompat.getColor(getContext(), R.color.turquoise);
                break;
            case "Purple":
            case "PurpleShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.purple_500);
                break;
            case "Red":
                tintColor = ContextCompat.getColor(getContext(), R.color.red);
                break;
            case "Green":
                tintColor = ContextCompat.getColor(getContext(), R.color.green);
                break;
            case "Silver":
                tintColor = ContextCompat.getColor(getContext(), R.color.silver);
                break;
            case "YellowShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.yellow);
                break;
            case "TurquoiseShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.turquoise);
                break;
            case "RedShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.red);
                break;
            case "GreenShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.green);
                break;
            case "SilverShooting":
                tintColor = ContextCompat.getColor(getContext(), R.color.silver);
                break;
            case "None":
                tintColor = ContextCompat.getColor(getContext(), R.color.black);
                break;
            default:
                tintColor = ContextCompat.getColor(getContext(), R.color.black);
                break;
        }
        if (isShooting) {
            feedbackRatingStar.setImageResource(R.drawable.star_circle);
        }
        else {
            feedbackRatingStar.setImageResource(R.drawable.star_circle_outline);
        }
        feedbackRatingStar.setColorFilter(tintColor);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        if (shippingDetailsLayout.getVisibility() == View.VISIBLE) {
            return;
        }

        if (getReturnPolicy()) {
            progressBarLayout.setVisibility(View.GONE);
            shippingDetailsLayout.setVisibility(View.VISIBLE);
        }
    }

    private boolean getReturnPolicy() {
//        TODO: ask for return policy from activity
        String value = getActivity().getIntent().getExtras().getString("returnPolicy");
        Log.d(TAG, "getReturnPolicy: " + value);
        if (value != null) {
            JSONObject returnPolicy = null;
            try {
                returnPolicy = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "updateReturnPolicy: " + returnPolicy.toString());
            policy = returnPolicy.optString("ReturnsAccepted") != null ? returnPolicy.optString("ReturnsAccepted") : "";
            returnsWithin = returnPolicy.optString("ReturnsWithin") != null ? returnPolicy.optString("ReturnsWithin") : "";
            refundMode = returnPolicy.optString("Refund") != null ? returnPolicy.optString("Refund") : "";
            shippedBy = returnPolicy.optString("ShippingCostPaidBy") != null ? returnPolicy.optString("ShippingCostPaidBy") : "";
            policyView.setText(policy);
            returnsWithinView.setText(returnsWithin);
            refundModeView.setText(refundMode);
            shippedByView.setText(shippedBy);
            return true;
        }
        return false;
    }
}