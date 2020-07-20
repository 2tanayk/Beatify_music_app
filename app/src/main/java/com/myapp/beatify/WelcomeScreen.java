package com.myapp.beatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreen extends AppCompatActivity {
    //Activity for the welcome screen as soon as user installs the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);
        Intent intent = getIntent();
    }//onCreate ends

    public void loginScreen(View v) {
        Intent l = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(l);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }//login ends
}//class ends