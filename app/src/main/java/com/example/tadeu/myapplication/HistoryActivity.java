package com.example.tadeu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("history.txt")));
            String line;
            while ((line = reader.readLine())!=null){
                textView.setText(line);
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
        layout.addView(textView);
    }
}
