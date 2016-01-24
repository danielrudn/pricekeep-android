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

import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.backend.PriceKeepAuthListener;
import com.dr.pricekeep.backend.PriceKeepBackend;
import com.dr.pricekeep.R;
import com.dr.pricekeep.models.User;
import com.dr.pricekeep.services.NotificationBroadcastReceiver;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void startLogin(final View view) {
        final EditText email = (EditText) findViewById(R.id.login_email);
        final EditText pass = (EditText) findViewById(R.id.login_password);
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Authenticating...");
        PriceKeepBackend priceKeepBackend = FirebaseBackend.getInstance(this);
        priceKeepBackend.authWithPassword(email.getText().toString(), pass.getText().toString(),
                new PriceKeepAuthListener() {
                    @Override
                    public void onSuccess(User user) {
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
                    public void onFailed(Exception ex) {
                        progressDialog.dismiss();
                        TextView errorView = (TextView) findViewById(R.id.login_error);
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText(ex.toString());
                        Animator anim = ViewAnimationUtils.createCircularReveal(errorView, view.getWidth()/2, view.getHeight()/2, 0, Math.max(view.getWidth(), view.getHeight()));
                        anim.start();
                    /*    switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                                email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_black_24dp, 0);
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel_black_24dp, 0);
                                break;
                        } */
                    }
                });

    }

    public void startSignUp(View view) {

    }

}
