package com.example.tadeu.myapplication;

import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Tadeu on 06/09/2016.
 */
public class PrayerActivity extends MainActivity{

    protected static String prayers[]={
            "Senhor, faz-nos capazes de olhar para ti em qualquer momento. Muitas vezes esquecemo-nos " +
            "de que somos habitados pelo teu Espírito Santo, de que rezas em nós, de que amas nós. O teu " +
            "milagre em nós é a tua confiança e o teu contínuo perdão.",
            "Abençoa-nos, Jesus Ressuscitado, a nós que gostaríamos de viver da tua confiança de tal forma " +
            "que as fontes do júbilo nunca sequem.", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
            "Outubro", "Novembro", "Dezembro"
    };

    protected static String months[]={"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
                            "Outubro", "Novembro", "Dezembro"};

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_prayer);

        RelativeLayout relative = (RelativeLayout) findViewById(R.id.relativePrayer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


        for(int prayer=0; prayer<prayers.length; prayer++){
            TextView textMonth = new TextView(this);
            TextView textPrayer = new TextView(this);

            ImageView divider = new ImageView(this);
            divider.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            divider.setBackgroundColor(getResources().getColor(R.color.white));

            textMonth.setId(prayer);
            textPrayer.setId(prayer);

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
}
