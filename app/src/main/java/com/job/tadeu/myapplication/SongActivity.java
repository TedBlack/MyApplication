package com.job.tadeu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongActivity extends MainActivity {

    private String songs[] = {"Ninguém te ama como eu", "Onde Deus te levar", "À Tua palavra (eu quero ser a luz)","Faz o que Deus espera de ti",
                            "Pedacinho de Deus", "Deixa Deus entrar", "Confiarei (O Senhor é meu pastor)", "É o meu corpo", "És a minha vida",
                            "Como o Pai me amou", "Maravilhas fez em mim", "Nada nos separará", "Não tenhas medo", "Deixa Deus entrar",
                            "Deus de amor (Amor tão grande)", "Fica junto a nós"};

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_song);

        RelativeLayout songLayout = (RelativeLayout) findViewById(R.id.relativeSong);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsLinear.setMargins(10,0, 0, 0);



        TextView text = new TextView(this);
        text.setText("Cancioneiro");
        text.setTextSize(30);
        text.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setLayoutParams(paramsLinear);
        layout.addView(text);

        for(int song = 0; song <songs.length; song++){
            Button button = new Button(this);
            button.setLayoutParams(paramsLinear);
            //button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText(songs[song]);
            button.setGravity(Gravity.START);
            button.setTextSize(20);
            button.setId(song);
            button.setTransformationMethod(null);
            button.setBackgroundResource(R.color.transparent);
            final int songId = song+1;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songButton(v, songId);
                }
            });
            layout.addView(button);

            ImageView divider = new ImageView(this);
            divider.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            divider.setBackgroundColor(getResources().getColor(R.color.black));

            if(song!=songs.length-1)
                layout.addView(divider);

        }

        songLayout.addView(layout);
    }

    public void songButton(View v, int id){
        Intent intent = new Intent(this, SongDisplay.class);
        String song = "song"+id;
        intent.putExtra("song", song);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.song);
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
