package com.example.tadeu.myapplication;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tadeu on 07/09/2016.
 */
public class StartService extends Service{

    private Looper looper;
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

        looper = thread.getLooper();
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
            notification.setSmallIcon(R.mipmap.icon);
            Date date= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            notification.setContentTitle(PrayerActivity.months[month]);
            notification.setContentText(PrayerActivity.prayers[month]);

            NotificationManager manager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notification.build());
        }
    }
}
