package com.example.tadeu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SongDisplay extends MainActivity {

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_songdisplay);


        Intent intent = getIntent();
        String text = intent.getStringExtra("song")+".txt";

        TextView textView = (TextView) this.findViewById(R.id.textSong);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(text)));
            String line;
            while ((line = reader.readLine())!=null){
                Log.i("line", line.length()+" "+line);
                textView.append(line+"\n");
                //Log.i("line", textView.getText().length()+" "+textView.getText().toString());
            }
        }
        catch (IOException error){
            Log.i("Error1", "No file to read");
        }
        finally {
            textView.append("Isto\t\t\te\t\tum\t\tteste");
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException error){
                    Log.i("Error2", "No reader open");
                }

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.song);
        MenuItem item2 = menu.findItem(R.id.prayer);
        if(item!=null){
            item.setVisible(false);
        }
        if(item2!=null){
            item2.setVisible(false);
        }
        return true;
    }
}
