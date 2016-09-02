package com.example.tadeu.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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

    public void eventButton(View view) throws Exception {

        String website = "https://calendar.google.com/calendar/ical/ijuh72fq0otnvo5edesmj72" +
                "fag%40group.calendar.google.com/public/basic.ics";

        DownloadFile dlFile = new DownloadFile(this);

        dlFile.doInBackground(website);

        Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
        calendarIntent.setType("vnd.android.cursor.item/event");

        //calendarIntent.putExtra("title", "Title");
        //calendarIntent.putExtra("beginTime", startTimeMillis);
        //calendarIntent.putExtra("endTime", endTimeMillis);
        //calendarIntent.putExtra("description", "Description");
        //Intent intent = new Intent(this, EventActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(calendarIntent);
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

class DownloadFile extends AsyncTask<String, Integer, String> {

    private Context context;

    public DownloadFile(Context context){
        this.context = context;
    }


    protected String doInBackground(String... sUrl) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String filename = "calendar.ics";
        //String filename = "irule.ics";
        File aux = new File(context.getFilesDir()+"calendar.ics");
        //File aux = new File(context.getFilesDir()+filename);
        /*try {
            aux.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(context.getFilesDir()+filename);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        Log.i("Fim", "Cheguei ao fim");
        return null;
    }
}
