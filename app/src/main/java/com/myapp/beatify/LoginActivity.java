package com.myapp.beatify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //Login page activity"

    private EditText emailTxt;
    private EditText passwordTxt;
    private ProgressBar progressBar;
    private Button loginBtn;

    public static FirebaseAuth mAuth;
//    FirebaseUser currentUser;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }//onStart ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = getIntent();

        emailTxt = findViewById(R.id.emailEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.loginProgressBar);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

    }//onCreate ends

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }//onBackPressed ends


    public void registerPage(View v) {
        Intent r = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(r);
    }//registerPage ends


    public void login(View v) {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);

        String emailId = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (TextUtils.isEmpty(emailId) || TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.GONE);
            loginBtn.setEnabled(true);

            Toast.makeText(LoginActivity.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
        }//if ends
        else {
            loginUser(emailId, password);

        }//else ends
    }//login ends

    private void loginUser(String emailId, String password) {
        mAuth.signInWithEmailAndPassword(emailId, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();

                Intent b = new Intent(LoginActivity.this, MainActivity.class);
                b.putExtra("STATUS", 1);
                startActivity(b);
                finish();
            }//onSuccess ends
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);

                Toast.makeText(LoginActivity.this, "Failed :(", Toast.LENGTH_SHORT).show();
                Log.e("Error", e.toString());
            }
        });
    }//loginUser ends

    public void forgotPassword(View v) {
        try { //try and catch block to catch NullPointerException
            mAuth.sendPasswordResetEmail(emailTxt.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("task:", "Email sent.");
                                Toast.makeText(LoginActivity.this, "Email sent!check your inbox..", Toast.LENGTH_SHORT).show();
                            }//if ends
                        }//onComplete ends
                    });
        }//try ends
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Enter your email id!", Toast.LENGTH_SHORT).show();
        }//catch block ends

    }//forgotPassword ends

}//class ends