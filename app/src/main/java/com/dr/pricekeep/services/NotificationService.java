package com.dr.pricekeep.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dr.pricekeep.R;
import com.dr.pricekeep.factories.ItemFactory;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.views.activities.MainActivity;
import com.dr.pricekeep.views.activities.SingleItemActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service implements ValueEventListener {

    private Firebase mRef;

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Firebase.setAndroidContext(this);
        mRef = new Firebase(MainActivity.FIREBASE_URL);
        Log.d(MainActivity.TAG, "Started NotificationService");
        if(mRef.getAuth() != null) {
            mRef.child("users/" + mRef.getAuth().getUid() + "/notifications").addListenerForSingleValueEvent(this);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.TAG, "Destroyed NotificationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * ValueEventListener Methods
     */

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(MainActivity.TAG, "OnDataChange:\n" + dataSnapshot.toString() + "\nPath: " + dataSnapshot.getRef().getPath().toString());
        if(dataSnapshot.getChildrenCount() != 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_attach_money_white_24dp)
                    .setVibrate(new long[]{300, 300});
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            Intent contentIntent = null;
            builder.setContentTitle("PriceKeep")
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentText(dataSnapshot.getChildrenCount() + " items on sale");
            contentIntent = new Intent(this, MainActivity.class);
            contentIntent.putExtra("notification", "start");
            // add item UUIDs extras to open the item list
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                contentIntent.putExtra(snapshot.getKey(), snapshot.getValue().toString());
            }
            stackBuilder.addNextIntent(contentIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).notify(5555, builder.build());
            // remove these items from notifications
            dataSnapshot.getRef().removeValue();
        }
        stopSelf();
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

}