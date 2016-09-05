package com.example.tadeu.myapplication;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "Hello!! Is it me you looking for??";
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

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
                try {
                    eventButton(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    }

    public void historyMessage(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    public void eventButton(View view) throws Exception {

        if(!verifyCon()){
            connDialog().show();
        }
        else {
            String website = "https://calendar.google.com/calendar/ical/ijuh72fq0otnvo5edesmj72" +
                    "fag%40group.calendar.google.com/public/basic.ics";

            DownloadFile dlFile = new DownloadFile(this);

            dlFile.doInBackground(website);

            Intent calendarIntent = new Intent();
            ComponentName componentName = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
            calendarIntent.setComponent(componentName);

            FileInputStream calendarFile = new FileInputStream(getFilesDir() + "calendar.ics");
            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar calendar = builder.build(calendarFile);

            TimeZoneRegistry registry = builder.getRegistry();
            TimeZone timeZone = registry.getTimeZone("Europe/Lisbon");

            ContentValues event = new ContentValues();
            ContentResolver cr = this.getContentResolver();

            for (Iterator events = calendar.getComponents().iterator(); events.hasNext(); ) {

                Component component = (Component) events.next();

                Long start = SDF.parse(component.getProperty("DTSTART").getValue()).getTime();
                Long end = SDF.parse(component.getProperty("DTEND").getValue()).getTime();
                String summary = component.getProperty("SUMMARY").getValue();
                String description = component.getProperty("DESCRIPTION").getValue();
                String UID = component.getProperty("UID").getValue();

                event.put("calendar_id", 1);
                event.put("title", summary);
                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("description", description);
                event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getDisplayName());
                event.put(CalendarContract.Events.UID_2445, UID);
                if (verifyEvent(UID, this))
                    cr.insert(Uri.parse("content://com.android.calendar/events"), event);
                else
                    cr.insert(Uri.parse("content://com.android.calendar/events"), event);
            }

            startActivity(calendarIntent);
        }
    }

    private boolean verifyCon(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni!=null && ni.isConnectedOrConnecting();
    }

    public void groupButton(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void seedButton(View view){
        Intent intent = new Intent(this, SeedActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
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

    private boolean verifyEvent(String eventID, Context context){
        Uri uri = Uri.parse("content://com.android.calendar/events");
        Cursor cursor = context.getContentResolver().query( uri, null, null, null, null);
        cursor.moveToFirst();
        String uid;
        int id;
        for(int myCursor = 0; myCursor< cursor.getCount(); myCursor++) {
            uid = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.UID_2445));
            id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events._ID));
            if (uid!=null && uid.equals(eventID)) {
                Uri deleteUri = ContentUris.withAppendedId(uri, id);
                context.getContentResolver().delete(deleteUri, null, null);
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }
}

