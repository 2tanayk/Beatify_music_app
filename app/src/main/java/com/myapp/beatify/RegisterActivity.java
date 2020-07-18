package com.myapp.beatify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{
    public static String TAG="Register page activity";

    private EditText emailTxt;
    private EditText passwordTxt;

    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent i=getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back arrow icon created in the toolbar
        //set LoginActivity as parent activity of RegisterActivity to enable functionality

        mAuth=FirebaseAuth.getInstance();

        emailTxt=(EditText)findViewById(R.id.emailEditText2);
        passwordTxt=(EditText)findViewById(R.id.passwordEditText2);
    }//onCreate ends

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }//onBackPressed ends

    public void register(View v)
    {
        String emailId=emailTxt.getText().toString();
        String password=passwordTxt.getText().toString();

        if(TextUtils.isEmpty(emailId) || TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this,"One or more fields are empty!",Toast.LENGTH_SHORT).show();
        }//if ends
        else if(password.length()<6){
            Toast.makeText(RegisterActivity.this,"Password should be min. 6 characters",Toast.LENGTH_SHORT).show();
        }//else-if ends
        else
        {
            registerUser(emailId,password);
        }//else ends
    }//register ends

    private void registerUser(String emailId, String password)
    {
       mAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task)
           {
              if(task.isSuccessful())
              {
                  Toast.makeText(RegisterActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();
                  Intent n=new Intent(RegisterActivity.this,MainActivity.class);
                  startActivity(n);
                  finish();
              }//if ends
              else {
                  Toast.makeText(RegisterActivity.this,"Failed :(",Toast.LENGTH_SHORT).show();
              }//else ends
           }//onComplete ends
       });

    }//registerUser ends

}//class ends