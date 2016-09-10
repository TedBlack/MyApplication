package com.example.tadeu.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Tadeu on 06/09/2016.
 */
public class PrayerActivity extends MainActivity {

    protected static String prayers[]=new String[12];

    protected static String months[]={"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
                            "Outubro", "Novembro", "Dezembro"};

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_prayer);

        RelativeLayout relative = (RelativeLayout) findViewById(R.id.relativePrayer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsLinear.setMargins(10,0, 0, 0);

        int lineCounter = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("prayers.txt")));
            String line;
            while ((line = reader.readLine())!=null){
                prayers[lineCounter]=line;
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


        for(int prayer=0; prayer<prayers.length; prayer++){
            TextView textMonth = new TextView(this);
            TextView textPrayer = new TextView(this);

            ImageView divider = new ImageView(this);
            divider.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            divider.setBackgroundColor(getResources().getColor(R.color.white));

            textMonth.setId(prayer);
            textPrayer.setId(prayer);
            textMonth.setLayoutParams(paramsLinear);
            textPrayer.setLayoutParams(paramsLinear);
            textMonth.setText(months[prayer]);
            textMonth.setTextSize(25);
            textMonth.setTextColor(getResources().getColor(R.color.white));

            textPrayer.setText(prayers[prayer]);
            textPrayer.setTextSize(15);
            textPrayer.setTextColor(getResources().getColor(R.color.white));

            if(prayer!=0){
                params.addRule(RelativeLayout.BELOW, prayer-1);
            }
            layout.addView(textMonth);
            layout.addView(textPrayer);
            if(prayer!=prayers.length-1)
                layout.addView(divider);


        }
        relative.addView(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.prayer);
        if(item!=null){
            item.setVisible(false);
        }
        return true;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent= new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
