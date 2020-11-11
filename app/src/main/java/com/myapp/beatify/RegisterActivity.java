package com.myapp.beatify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    //Register page activity

    private EditText emailTxt;
    private EditText passwordTxt;
    private Button registerBtn;

    private String username = "";
    private ProgressBar progressBar;

    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent i = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back arrow icon created in the toolbar
        //set LoginActivity as parent activity (in Manifests) of RegisterActivity to enable functionality

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailEditText2);
        passwordTxt = findViewById(R.id.passwordEditText2);
        progressBar = findViewById(R.id.registerProgressBar);
        registerBtn = findViewById(R.id.registerBtn);
    }//onCreate ends

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }//onBackPressed ends

    public void register(View v) {
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);


        String emailId = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();


        if (TextUtils.isEmpty(emailId) || TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.GONE);
            registerBtn.setEnabled(true);

            Toast.makeText(RegisterActivity.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
        }//if ends
        else if (password.length() < 6) {
            progressBar.setVisibility(View.GONE);
            registerBtn.setEnabled(true);

            Toast.makeText(RegisterActivity.this, "Password should be min. 6 characters", Toast.LENGTH_SHORT).show();
        }//else-if ends
        else {
            username = emailId.substring(0, emailId.indexOf('@'));
            registerUser(emailId, password);
        }//else ends
    }//register ends

    private void registerUser(String emailId, String password) {
        mAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    registerBtn.setEnabled(true);

                    Toast.makeText(RegisterActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                    Intent n = new Intent(RegisterActivity.this, MainActivity.class);
                    //0 value to indicate that the user registered
                    //n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    n.putExtra("STATUS", 0);
                    n.putExtra("USERNAME", username + "");
                    startActivity(n);
                    finishAffinity();
                }//if ends
                else {
                    progressBar.setVisibility(View.GONE);
                    registerBtn.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Failed :(", Toast.LENGTH_SHORT).show();
                }//else ends
            }//onComplete ends
        });

    }//registerUser ends

}//class ends