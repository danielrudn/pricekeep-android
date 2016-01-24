package com.dr.pricekeep.views.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.dr.pricekeep.R;
import com.dr.pricekeep.services.NotificationBroadcastReceiver;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRef = new Firebase(MainActivity.FIREBASE_URL);
    }
    public void startLogin(final View view) {
        final EditText email = (EditText) findViewById(R.id.login_email);
        final EditText pass = (EditText) findViewById(R.id.login_password);
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Authenticating...");
        mRef.authWithPassword(email.getText().toString(), pass.getText().toString(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        progressDialog.dismiss();
                        startActivity(new Intent(view.getContext(), MainActivity.class));
                        finish();
                        // start notification service
                        PendingIntent notificationBroadcast = PendingIntent.getBroadcast(view.getContext(), NotificationBroadcastReceiver.SERVICE_REQUEST_ID, new Intent(view.getContext(), NotificationBroadcastReceiver.class), 0);
                        try {
                            notificationBroadcast.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    @TargetApi(21)
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        TextView errorView = (TextView) findViewById(R.id.login_error);
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText(firebaseError.getMessage());
                        Animator anim = ViewAnimationUtils.createCircularReveal(errorView, view.getWidth()/2, view.getHeight()/2, 0, Math.max(view.getWidth(), view.getHeight()));
                        anim.start();
                        switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                                email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_black_24dp, 0);
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_black_24dp, 0);
                                break;
                        }
                    }
                });

    }

    public void startSignUp(View view) {

    }

}
