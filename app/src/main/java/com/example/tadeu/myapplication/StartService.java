package com.example.tadeu.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class StartService extends Service{

    private ServiceHandler service;
    private Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        context = this;

        HandlerThread thread = new HandlerThread("JObApp", Process.THREAD_PRIORITY_BACKGROUND);

        thread.start();

        Looper looper = thread.getLooper();
        service = new ServiceHandler(looper);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        // call a new service handler. The service ID can be used to identify the service
        Message message = service.obtainMessage();
        message.arg1 = startId;
        service.sendMessage(message);

        return START_STICKY;
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
                super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Well calling mServiceHandler.sendMessage(message); from onStartCommand,
            // this method will be called.

            if(InsertEvents.verifyCon(context)){
                try {
                    InsertEvents.insertEvents(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(getNotificationIcon());
            Date date= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            int lineCounter = 0;

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("prayers.txt")));
                String line;
                while ((line = reader.readLine())!=null){
                    PrayerActivity.prayers[lineCounter]=line;
                    lineCounter++;
                }
            }
            catch (IOException error){
                Log.i("Error1", "No file to read");
            }
            finally {
                if(reader != null){
                    try{
                        reader.close();
                    } catch (IOException error){
                        Log.i("Error2", "No reader open");
                    }

                }

            }

            notification.setContentTitle(PrayerActivity.months[month]);
            notification.setContentText(PrayerActivity.prayers[month]);

            Intent myIntent = new Intent(context, PrayerActivity.class);
            PendingIntent pendIntent = PendingIntent.getActivity(context, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification.setContentIntent(pendIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, notification.build());
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.icon2 : R.mipmap.icon;
    }
}
