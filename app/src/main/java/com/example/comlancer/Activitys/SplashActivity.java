package com.example.comlancer.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.comlancer.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView ivLogo = findViewById(R.id.iv_logo);

        Glide.with(SplashActivity.this).load(R.drawable.logo).into(ivLogo);


        splash();

    }


    void splash() {

        /****** Create Thread that will sleep for 5 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(SplashActivity.this, LoginRegistrationActivity.class);
                    startActivity(i);


                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }


}