package com.nillawaffer.hue.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by bargokr14 on 4/27/16.
 */

public class splashActivity extends AppCompatActivity {
    private static String TAG = splashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Sleeping
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Intent intent = new Intent(this, PHStartActivity.class);
        startActivity(intent);
        finish();
    }
}
