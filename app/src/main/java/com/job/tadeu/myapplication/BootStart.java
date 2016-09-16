package com.job.tadeu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

public class BootStart extends BroadcastReceiver {

    public static File file;

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
