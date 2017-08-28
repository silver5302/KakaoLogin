package com.silver5302.kakaologin2;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        test();




    }
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {

            Intent intent=new Intent(IntroActivity.this,MainActivity.class);
            startActivity(intent);

            finish();
        }
    };

    //카톡 해시키 받아오기
    public void test() {

        try {
            PackageInfo Info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : Info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("키", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {


        }
    }
}
