package com.example.herodex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashArt extends AppCompatActivity {

    MediaPlayer mp;

    private Handler mWaitHandler = new Handler();
    ImageView image,image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashart);

        mp =MediaPlayer.create(this,R.raw.eshop);


      // image = (ImageView) findViewById(R.id.logo);

       // rotarImagen(image);

        //Glide.with(this).asGif().load(R.drawable.logogif).into(image);

        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //The following code will execute after the 5 seconds.

                try {
                    //Go to next page i.e, start the next activity.
                    Intent intent = new Intent(getApplicationContext(), MenuHeroes.class);

                    startActivity(intent);
                    mp.start();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, 5020);  // Give a 5 seconds delay.
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }

    private void rotarImagen(View view) {
        RotateAnimation animation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

}
