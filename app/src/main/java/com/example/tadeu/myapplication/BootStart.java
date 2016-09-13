package com.example.tadeu.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

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

        //Intent myIntent = new Intent(context, StartService.class);
      /*  PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //long interval = 1000 * 60 * 60 * 24 * 15;
        long interval = 30000;

        //Log.i("Interval do serviço", Long.toString(interval)+" "+System.currentTimeMillis());
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pIntent);
*/        //context.startService(myIntent);
    }
}
