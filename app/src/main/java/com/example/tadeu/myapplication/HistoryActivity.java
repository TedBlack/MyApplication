package com.example.tadeu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView textView = new TextView(this);

        textView.setMovementMethod(new ScrollingMovementMethod());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("history.txt")));
            String line;
            while ((line = reader.readLine())!=null){
                textView.append(line+"\n");
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



        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_history);

        if(layout.canScrollVertically(1)){
            layout.setVerticalScrollBarEnabled(true);
            layout.setScrollbarFadingEnabled(false);
        }
        layout.addView(textView);

    }
}
