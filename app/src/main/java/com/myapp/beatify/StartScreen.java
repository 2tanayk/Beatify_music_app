package com.myapp.beatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        Intent intent=getIntent();

    }//onCreate ends

    public void login(View view)
    {
     Intent l=new Intent(StartScreen.this,LoginActivity.class);
     startActivity(l);
     overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
     finish();
    }


}//class ends