package com.example.tadeu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

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
