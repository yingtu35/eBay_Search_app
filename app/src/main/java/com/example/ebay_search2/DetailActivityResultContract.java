package com.example.ebay_search2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailActivityResultContract extends ActivityResultContract<Intent, Boolean> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent input) {
        // Create the intent for starting the activity
        return input;
    }

    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        // Parse the result from the activity
        return resultCode == Activity.RESULT_OK;
    }
}
