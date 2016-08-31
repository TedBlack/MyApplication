package com.example.tadeu.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

public class GroupActivity extends MainActivity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    private final int gallery [] = {R.drawable.cesar, R.drawable.jessica, R.drawable.joao, R.drawable.tadeu, R.drawable.pedro, R.drawable.diogo,
                                    R.drawable.annie, R.drawable.rita, R.drawable.patricia, R.drawable.sam, R.drawable.sara, R.drawable.cristina,
                                    R.drawable.alexandre, R.drawable.leonel};
    private final String subtitles [] ={"César Freitas", "Jéssica Teixeira", "João Abreu", "Tadeu Freitas", "Pedro Carvalho", "Diogo Pereira",
                                        "Annie Martins", "Rita Pereira", "Patricia Fernandes", "Samuel Fernandes", "Sara Silva", "Cristina Salgado",
                                        "Alexandre Ribeiro", "Leonel Gomes"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mContext=this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);


        ImageView image = (ImageView) this.findViewById(R.id.image);
        Glide.with(this).load(gallery[0]).into(image);

        TextView text = (TextView) this.findViewById(R.id.imageText);
        text.setText(subtitles[0]);

        for(int element=1; element<gallery.length; element++){

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.activity_group, null);
            mViewFlipper.addView(view);

            image = (ImageView) view.findViewById(R.id.image);
            Glide.with(this).load(gallery[element]).into(image);

            text = (TextView) view.findViewById(R.id.imageText);
            text.setText(subtitles[element]);
        }


        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(5000);

        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        mViewFlipper.setInAnimation(mContext, R.anim.left_in);
        mViewFlipper.setOutAnimation(mContext, R.anim.left_out);
        mViewFlipper.startFlipping();
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                mViewFlipper.setInAnimation(mContext, R.anim.left_in);
                mViewFlipper.setOutAnimation(mContext, R.anim.left_out);
            }

            return false;
        }
    }
}
