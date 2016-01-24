package com.dr.pricekeep.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.backend.PriceKeepBackend;
import com.dr.pricekeep.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Check to see if user is logged in and start the right activity
        PriceKeepBackend priceKeepBackend = FirebaseBackend.getInstance(this);
        if(!priceKeepBackend.isLoggedIn()) {
            // log in activity
            this.startActivity(new Intent(this, LoginActivity.class));
        } else {
            // main activity
            this.startActivity(new Intent(this, MainActivity.class));
        }
    }
}
