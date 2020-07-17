package com.myapp.beatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private final int DELAY=2000;
//    SharedPreferences pref;
      boolean firstStart;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        firstStart = prefs.getBoolean("firstStart", true);

        openSplashScreen();

    }

    private void openSplashScreen()
    {
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method

            @Override

            public void run() {
                if (firstStart) {
                    //startWelcomeActivity();
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();


                Intent i = new Intent(SplashScreen.this, StartScreen.class);

                startActivity(i);


                // close this activity

                finish();

                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }//fn ends


        },DELAY); // wait for 5 seconds

    }//fn ends

//    private void startWelcomeActivity() {
//
//    }//fn ends

}