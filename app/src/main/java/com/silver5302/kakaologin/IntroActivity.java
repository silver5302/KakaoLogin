package com.silver5302.kakaologin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        linearLayout=(LinearLayout)findViewById(R.id.linear);

        Animation ani= AnimationUtils.loadAnimation(this,R.anim.appear_logo);
        linearLayout.startAnimation(ani);

        timer.schedule(timerTask,3500);



    }
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {

            Intent intent=new Intent(IntroActivity.this,MainActivity.class);
            startActivity(intent);

            finish();
        }
    };
}