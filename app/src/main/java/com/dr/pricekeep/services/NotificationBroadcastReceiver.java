package com.dr.pricekeep.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dr.pricekeep.views.activities.MainActivity;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final int SERVICE_REQUEST_ID = 5555566;
    private static final int NOTIFICATION_FREQUENCY = 45 * 60000; // 45 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "NotificationBroadcastReceiver onReceive called");
        // start alarm for service
        AlarmManager alarmManager = ((AlarmManager)context.getSystemService(context.ALARM_SERVICE));
        PendingIntent alarmIntent = PendingIntent.getService(context, SERVICE_REQUEST_ID, new Intent(context, NotificationService.class), 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + NOTIFICATION_FREQUENCY, NOTIFICATION_FREQUENCY, alarmIntent);
    }

}
