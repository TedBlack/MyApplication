package com.example.tadeu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "Hello!! Is it me you looking for??";
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                eventButton(v);
            }
        });

        final Button groupButton = (Button) findViewById(R.id.group);
        groupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                groupButton(v);
            }
        });
    }

    public void historyMessage(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void eventButton(View view){
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void groupButton(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
