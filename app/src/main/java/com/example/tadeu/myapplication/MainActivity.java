package com.example.tadeu.myapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {


    private AlarmManager manager;
    private PendingIntent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, StartService.class));

        Intent alarmIntent = new Intent(this, BootStart.class);
        intent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        startAlarm();


        Button myButton = (Button) findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyMessage(v);
            }
        });

        final Button eventButton = (Button) findViewById(R.id.events);
        eventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                eventButton(v);
            }
        });

        final Button groupButton = (Button) findViewById(R.id.group);
        groupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                groupButton(v);
            }
        });

        final ImageButton seedButton = (ImageButton) findViewById(R.id.seed);
        seedButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                seedButton(v);
            }
        });

        final Button prayerButton = (Button) findViewById(R.id.prayers);
        prayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prayerButton(v);
            }
        });

        final Button songButton = (Button) findViewById(R.id.songs);
        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, intent);
    }

    public void songButton(View view){
        Intent intent = new Intent(this, SongActivity.class);
        startActivity(intent);
    }

    public void prayerButton(View view){
        Intent intent = new Intent(this, PrayerActivity.class);
        startActivity(intent);
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
            Intent calendarIntent = new Intent();
            ComponentName componentName = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
            calendarIntent.setComponent(componentName);
            try {
                InsertEvents.insertEvents(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(calendarIntent);
        }
    }

    public void groupButton(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
    }

    public void seedButton(View view){
        Intent intent = new Intent(this, SeedActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Dialog aboutDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.about);
        build.setMessage(R.string.aboutText);
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
}

