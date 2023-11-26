package com.example.ebay_search2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebay_search2.ApiCall;
import com.example.ebay_search2.ProductResultsActivity;
import com.example.ebay_search2.R;
import com.example.ebay_search2.ui.WishlistManager;
import com.example.ebay_search2.databinding.FragmentHomeBinding;
import com.example.ebay_search2.ui.Product;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static String TAG = "HomeFragment";
    private static final String URL = "http://10.0.2.2:3000";
    private static final String ERROR_TEXT = "Please enter mandatory field";
    private View root;
    private TextInputLayout inputKeywordLayout;
    private TextInputLayout inputZipcodeLayout;
    private EditText inputKeyword;
    private EditText inputDistance;
//    private EditText inputZipcode;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout nearbySearchSection;
    private RadioGroup searchFromGroup;
    private RadioButton searchFromCurrentButton;
    private RadioButton searchFromZipcodeButton;
    private CheckBox enableNearbySearchCheckbox;
    private Button searchButton;
    private Button clearButton;

    private Handler handler;
    private AutoSuggestAdaptor autoSuggestAdaptor;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    private static WishlistManager wishlistManager = WishlistManager.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        Log.d(TAG, "onCreateView: ");

        initializeForm(root);

        searchFromGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                toggleSearchFromButton(checkedId);
            }
        });
        // Toggle enable nearby search
        enableNearbySearchCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // If CheckBox is checked, make the components visible; otherwise, hide them
            nearbySearchSection.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // set up search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to be executed when the button is clicked
                // For example, you can show a Toast message
                Log.d(TAG, "onClick: search");
                if (!validateForm()) {
                    Toast.makeText(requireContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    return;
                }

//                start product results activity
                Intent intent = new Intent(getActivity(), ProductResultsActivity.class);
                // TODO: pass the form data to the product results activity
                startActivity(intent);
            }
        });

        // set up the clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clear");
                resetForm();
            }
        });

//        auto complete textView test
//        set up auto suggest adapter
        autoSuggestAdaptor = new AutoSuggestAdaptor(requireContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdaptor);

        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        autoCompleteTextView.setText(autoSuggestAdaptor.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void makeApiCall(String text) {
        ApiCall.make(requireContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("postalCodes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("postalCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdaptor.setData(stringList);
                autoSuggestAdaptor.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void initializeForm(View root) {
        nearbySearchSection = root.findViewById(R.id.nearbySearchSection);
        searchFromGroup = root.findViewById(R.id.search_from);
        searchFromCurrentButton = root.findViewById(R.id.search_from_current);
        searchFromZipcodeButton = root.findViewById(R.id.search_from_zipcode);
        inputKeywordLayout = root.findViewById(R.id.input_keyword_layout);
        inputZipcodeLayout = root.findViewById(R.id.input_zipcode_layout);
        inputKeyword = root.findViewById(R.id.input_keyword);
        inputDistance = root.findViewById(R.id.input_distance);
//        inputZipcode = root.findViewById(R.id.input_zipcode);
        enableNearbySearchCheckbox = root.findViewById(R.id.enableNearbySearchCheckbox);
        searchButton = root.findViewById(R.id.button_search);
        clearButton = root.findViewById(R.id.button_clear);
        autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView);

        nearbySearchSection.setVisibility(View.GONE);
        enableNearbySearchCheckbox.setChecked(false);
        searchFromCurrentButton.setChecked(true);
        searchFromZipcodeButton.setChecked(false);
//        inputZipcode.setEnabled(false);
        autoCompleteTextView.setEnabled(false);
        inputKeywordLayout.setErrorIconDrawable(0);
        inputZipcodeLayout.setErrorIconDrawable(0);

        Spinner spinner = (Spinner) root.findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void resetForm() {
        inputKeyword.setText("");
        inputDistance.setText("");
//        inputZipcode.setText("");
        autoCompleteTextView.setText("");
        inputKeywordLayout.setError(null);
        inputZipcodeLayout.setError(null);
        nearbySearchSection.setVisibility(View.GONE);
        enableNearbySearchCheckbox.setChecked(false);
    }

    private void toggleSearchFromButton(int checkedId) {
        if (checkedId == R.id.search_from_current) {
            // Do something when radioButton1 is checked
            Log.d(TAG, "onCheckedChanged: current location");
            searchFromCurrentButton.setChecked(true);
            searchFromZipcodeButton.setChecked(false);
            autoCompleteTextView.setEnabled(false);
            inputZipcodeLayout.setError(null);
//            inputZipcode.setEnabled(false);

        } else if (checkedId == R.id.search_from_zipcode) {
            // Do something when radioButton2 is checked
            Log.d(TAG, "onCheckedChanged: zipcode");
            searchFromCurrentButton.setChecked(false);
            searchFromZipcodeButton.setChecked(true);
            autoCompleteTextView.setEnabled(true);
//            inputZipcode.setEnabled(true);
        }
    }

    private boolean validateForm() {
        boolean isValidForm = true;
        String keyword = inputKeyword.getText().toString().trim();
//        String zipcode = inputZipcode.getText().toString();
        String zipcode = autoCompleteTextView.getText().toString();
        Log.d(TAG, zipcode);
        if (keyword.isEmpty()) {
            inputKeywordLayout.setError(ERROR_TEXT);
            Log.d(TAG, "validateForm: keyword failed");
            isValidForm = false;
        }
        if (autoCompleteTextView.isEnabled() & zipcode.isEmpty()) {
            Log.d(TAG, "validateForm: zipcode failed");
            inputZipcodeLayout.setError(ERROR_TEXT);
            isValidForm = false;
        }

        if (isValidForm) {
            inputKeywordLayout.setError(null);
            inputZipcodeLayout.setError(null);
            Log.d(TAG, "validateForm: success");
        }

        return isValidForm;
    }
}