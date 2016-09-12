package com.example.tadeu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
