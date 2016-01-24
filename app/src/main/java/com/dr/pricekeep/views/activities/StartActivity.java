package com.dr.pricekeep.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dr.pricekeep.R;
import com.dr.pricekeep.views.activities.LoginActivity;
import com.dr.pricekeep.views.activities.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Firebase.setAndroidContext(this);
        // Check to see if user is logged in and start the right activity
        Firebase mRef = new Firebase(MainActivity.FIREBASE_URL);
        if(mRef.getAuth() == null) {
            // log in activity
            this.startActivity(new Intent(this, LoginActivity.class));
        } else {
            // main activity
            this.startActivity(new Intent(this, MainActivity.class));
        }
    }
}
