package com.example.tadeu.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tadeu on 07/09/2016.
 */
public class BootStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
        /*Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        */
        /***** For start Service  ****/
        Intent myIntent = new Intent(context, StartService.class);
        context.startService(myIntent);
    }
}
