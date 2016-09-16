package com.job.tadeu.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView textView = (TextView) this.findViewById(R.id.historyText);



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
    }
}
