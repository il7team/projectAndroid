package com.bitm.alfa_travel_mate.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.bitm.alfa_travel_mate.R;


public class FlashScreenActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        relativeLayout= (RelativeLayout) findViewById(R.id.activity_flash_screen);

        Animation fadeout = AnimationUtils.loadAnimation(FlashScreenActivity.this, R.anim.fade_out);

        relativeLayout.startAnimation(fadeout);
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {



                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(FlashScreenActivity.this,LoginActivity.class);

                        startActivity(mainIntent);
                        finish();

                    }
                }, 1500);
                Animation fadeIn = AnimationUtils.loadAnimation(FlashScreenActivity.this, R.anim.fade_in);
                relativeLayout.startAnimation(fadeIn);



            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });



    }
}
