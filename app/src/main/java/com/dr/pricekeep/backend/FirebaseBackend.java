package com.dr.pricekeep.backend;

import android.content.Context;
import android.util.Log;

import com.dr.pricekeep.factories.UserFactory;
import com.dr.pricekeep.models.User;
import com.dr.pricekeep.views.activities.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class FirebaseBackend implements PriceKeepBackend, Firebase.AuthResultHandler, ValueEventListener {

    public static final String TAG = FirebaseBackend.class.getSimpleName();
    // Singleton code
    private static FirebaseBackend instance = null;
    public static FirebaseBackend getInstance(Context context) {
        if(instance == null) {
            Log.d(TAG, "CREATED BACKEND INSTANCE");
            instance = new FirebaseBackend(context);
        }
        return instance;
    }

    private Firebase ref;
    private User user = null;
    private FirebaseDatabase database;
    private PriceKeepAuthListener authListener = null;
    private PriceKeepNotificationListener notificationListener = null;

    private FirebaseBackend(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase(MainActivity.FIREBASE_URL);
        if(ref.getAuth() != null) {
            user = UserFactory.fromAuthData(ref.getAuth());
        }
        database = new FirebaseDatabase(context);
    }

    @Override
    public void authWithPassword(String email, String password, PriceKeepAuthListener listener) {
        this.authListener = listener;
        ref.authWithPassword(email, password, this);
    }

    @Override
    public PriceKeepDatabase getDatabase() {
        return database;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void logout() {
        ref.unauth();
    }

    @Override
    public boolean isLoggedIn() {
        Log.d(TAG, "LoggedIn: " + (user != null));
        return user != null;
    }

    @Override
    public void loadNotifications(PriceKeepNotificationListener listener) {
        this.notificationListener = listener;
        ref.child("users/" + user.getUUID() + "/notifications")
                .addListenerForSingleValueEvent(this);
    }

    @Override
    public void onAuthenticated(AuthData authData) {
        user = UserFactory.fromAuthData(authData);
        if(authListener != null) {
            authListener.onSuccess(user);
        }
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        authListener.onFailed(firebaseError.toException());
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(notificationListener != null) {
            ArrayList<String> items = new ArrayList<String>();
            for(DataSnapshot item : dataSnapshot.getChildren()) {
                items.add(item.getKey());
            }
            notificationListener.onNotificationsLoaded(items);
            // remove notifications now that they've been seen.
            dataSnapshot.getRef().removeValue();
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.d(TAG, "Error loading notifications: " + firebaseError.getMessage());
    }
}
