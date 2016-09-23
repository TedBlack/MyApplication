package com.job.tadeu.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private PendingIntent intent;
    private Context context = this;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        //setDefaults("activated", false, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent alarmIntent = new Intent(this, BootStart.class);
        intent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        boolean notStarted = true;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StartService.class.getName().equals(service.service.getClassName())) {
                notStarted = false;
            }
        }
        if(notStarted) {
            StartService.timestamp = 0;
            startService(new Intent(this, StartService.class));
            startAlarm();
        }

        ImageButton myButton = (ImageButton) findViewById(R.id.button);
        assert myButton != null;
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyMessage(v);
            }
        });

        final ImageButton eventButton = (ImageButton) findViewById(R.id.events);
        assert eventButton != null;
        eventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                eventButton(v);
            }
        });

        final ImageButton groupButton = (ImageButton) findViewById(R.id.group);
        assert groupButton != null;
        groupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                groupButton(v);
            }
        });

        final ImageButton seedButton = (ImageButton) findViewById(R.id.seed);
        assert seedButton != null;
        seedButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                seedButton(v);
            }
        });
    }

    public static void setDefaults(String key, boolean value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected void onDestroy(){
        if(thread!=null){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public static boolean getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public void startAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long interval = 1000 * 60 * 60 * 24 * 15;
        //long interval = 10000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, intent);
    }

    public void historyMessage(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void eventButton(View view) {
        if(!InsertEvents.verifyCon(this)){
            connDialog().show();
        }
        else {
            try {
                boolean activated = getDefaults("activated", this);
                if (activated && !thread.isAlive() && !getThreadByName("events")) {
                    thread = new Thread(){
                      public void run(){
                          try {
                              InsertEvents.insertEvents(context);
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                    };
                    thread.setName("events");
                    thread.start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/")));
        }
    }

    public boolean getThreadByName(String threadName) {

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        for (int i = 0; i < threadArray.length; i++) {
            if (threadArray[i].getName().equals(threadName))
                return true;
        }
        return false;
    }


    public void groupButton(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
    }

    public void seedButton(View view){
        Intent intent = new Intent(this, SeedActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.help:
                helpDialog().show();
                return true;
            case R.id.about:
                aboutDialog().show();
                return true;
            case R.id.exit:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                return true;
            case R.id.prayer:
                startActivity(new Intent(getApplicationContext(), PrayerActivity.class));
                return true;
            case R.id.song:
                startActivity(new Intent(getApplicationContext(), SongActivity.class));
                return true;
            case R.id.activate:
                activateDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.activate);
        boolean activated = getDefaults("activated", this);
        if(activated){
            item.setTitle("Activado");
            item.setEnabled(false);
            item.setVisible(false);
        }
        return true;
    }

    public Dialog aboutDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.about);
        build.setMessage(R.string.aboutText);
        build.setIcon(R.mipmap.bear);
        build.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return build.create();
    }

    public Dialog connDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.titleInternet);
        build.setMessage(R.string.internet);
        build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/")));
            }
        });
        build.setNegativeButton(R.string.definitions, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        return build.create();
    }

    public Dialog helpDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.help);
        build.setMessage(R.string.helpText);
        build.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return build.create();
    }

    public Dialog activateDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.activate);
        build.setMessage(R.string.activateText);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20);

        input.setLayoutParams(lp);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        build.setView(input);

        build.setPositiveButton("Seguinte", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                if(pass.equals("job15anos")){
                    setDefaults("activated", true, context);
                    Toast.makeText(context, "Aplicação ativada", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("job2001")){
                    setDefaults("activated", true, context);
                    InsertEvents.website = "https://calendar.google.com/calendar/ical/co5iu26ul8hul9uf80eb06i7ug%40group.calendar.google.com/public/basic.ics";
                    Toast.makeText(context, "Aplicação ativada", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("publico")){
                    setDefaults("activated", true, context);
                    InsertEvents.website = "https://calendar.google.com/calendar/ical/fhumd1cok7d3lblugh2jh541cs%40group.calendar.google.com/public/basic.ics";
                    Toast.makeText(context, "Aplicação ativada", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Password errada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        build.setNeutralButton("Cancel", null);

        return build.create();
    }
}

