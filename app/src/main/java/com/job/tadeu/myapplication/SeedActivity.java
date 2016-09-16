package com.job.tadeu.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

public class SeedActivity extends MainActivity{

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    private final int gallery[] = {R.mipmap.i1, R.mipmap.i2, R.mipmap.i3, R.mipmap.i4, R.mipmap.i5, R.mipmap.i6, R.mipmap.i7,
                                    R.mipmap.i8, R.mipmap.i9, R.mipmap.i10, R.mipmap.i11, R.mipmap.i12, R.mipmap.i13, R.mipmap.i14,
                                    R.mipmap.i15, R.mipmap.i16, R.mipmap.i17, R.mipmap.i18, R.mipmap.i19, R.mipmap.i20, R.mipmap.i21,
                                    R.mipmap.i22, R.mipmap.i23, R.mipmap.i24, R.mipmap.i25, R.mipmap.i26, R.mipmap.i27};

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed);


        final ImageButton imgBtn = (ImageButton) findViewById(R.id.playSeed);
        imgBtn.setBackgroundResource(R.mipmap.pause);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBtn(v, imgBtn);
            }
        });

        mContext=this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.seedFlipper);

        ImageView imageSeed= (ImageView) this.findViewById(R.id.imageSeed);
        Glide.with(this).load(gallery[0]).into(imageSeed);;

        for(int element=1; element<gallery.length; element++){

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.activity_seed, null);
            mViewFlipper.addView(view);

            imageSeed = (ImageView) view.findViewById(R.id.imageSeed);
            Glide.with(this).load(gallery[element]).into(imageSeed);
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

    public void imgBtn(View v, final ImageButton imgBtn){
        //mViewFlipper.setAutoStart(false);
        mViewFlipper.stopFlipping();
        imgBtn.setBackgroundResource(R.mipmap.play);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBtn(v, imgBtn);
            }
        });
    }

    public void playBtn(View v, final ImageButton button){
        button.setBackgroundResource(R.mipmap.pause);
        mViewFlipper.startFlipping();
        mViewFlipper.showNext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBtn(v, button);
            }
        });
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
