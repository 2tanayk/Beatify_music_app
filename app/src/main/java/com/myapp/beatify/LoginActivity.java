package com.myapp.beatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i=getIntent();
    }
    public void register(View v)
    {
        Intent r=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(r);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}