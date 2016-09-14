package com.example.tadeu.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private PendingIntent intent;
    private Context context = this;
    protected static boolean activated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

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

    public void startAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long interval = 1000 * 60 * 60 * 24 * 15;
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
                if (activated) {
                    Thread thread = new Thread(){
                      public void run(){
                          try {
                              InsertEvents.insertEvents(context);
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                    };
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/")));
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
        if(activated){
            item.setTitle("Activado");
            item.setEnabled(false);
        }
        return true;
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20,20);
        input.setLayoutParams(lp);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        build.setView(input);

        build.setPositiveButton("Seguinte", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                if(pass.equals("job15anos")){
                    activated=true;
                    Toast.makeText(context, "Aplicação ativada", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("job2001")){
                    activated=true;
                    InsertEvents.website = "https://calendar.google.com/calendar/ical/co5iu26ul8hul9uf80eb06i7ug%40group.calendar.google.com/public/basic.ics";
                    Toast.makeText(context, "Aplicação ativada", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("publico")){
                    activated=true;
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

