package com.example.tadeu.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Tadeu on 07/09/2016.
 */
public class ReBoot extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, BootStart.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long interval = 1000 * 60 * 60 * 24 * 15;
            //long interval = 10000;

            Log.i("Interval do serviço", Long.toString(interval));
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            context.startService(alarmIntent);
        }
    }
}
