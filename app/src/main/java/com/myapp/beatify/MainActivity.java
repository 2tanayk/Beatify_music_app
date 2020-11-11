package com.myapp.beatify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//0-user has registered
//1-user has logged in
public class MainActivity extends AppCompatActivity {
    //MainActivity which acts as a fragment holder
    private FragmentManager fragmentManager;
    //private final String COLLECTION_TITLE = "Music";
    //public AppCompatActivity activity = null;
    private SettingsFragment test;//

    public final String SHARED_PREFS = "sharedPrefs";

    public final String USERNAME = "username";
    public final String PREFERENCE = "preference";
    public final String IMG_URL = "image_url";

    private SharedPreferences sharedPreferences;

    protected String username = "";//
    protected String preferences = "";
    public String recordPref;
    public String userDpURL;
    //Initialized a reference of CF
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Map<String, Object> user = new HashMap<>();
    private Map<String, Object> musicDoc = new HashMap<>();

    public int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "onCreate()");
        //activity = this;

        Intent mI = getIntent();
        //to check whether user has logged in or just registered
        s = mI.getIntExtra("STATUS", 1);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        if (s == 0) {
            username = mI.getStringExtra("USERNAME");//we set username by default as email id
        }//if ends
        else {
            //we retrieve the username that has been stored in shared prefs
            username = sharedPreferences.getString(USERNAME, null);

            if (username == null) {
                readFromFirestore();//in an event that user deletes the app we retrieve user name from firestore
            }//inner if ends

//            Toast.makeText(this, "" + sharedPreferences.getString(PREFERENCE, "nope"), Toast.LENGTH_SHORT).show();
        }//else ends

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null) { //null check
            if (savedInstanceState != null) {
                return;
            }//inner if ends

//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //PreferencesFragment preferencesFragment = new PreferencesFragment();

            if (s == 0) {//if the user has just registered we ask him his choice of music
                fragmentManager.beginTransaction().add(R.id.fragment_container, new PreferencesFragment(), "Preferences").commit();
                Log.e("Info", "In user registration condition");
            } else { //else we directly take him to home page
                fragmentManager.beginTransaction().add(R.id.fragment_container, new HostFragment(), "Host").addToBackStack(null).commit();
                Log.e("Info", "In user login condition");
            }
//            fragmentTransaction.commit();
//            createMusicCollection();
        }//if ends
        //createMusicCollection();

    }//onCreate ends

    private void readFromFirestore() {
//        String username="";
//        String preferences = "";

        DocumentReference userDoc = db.collection("Users")
                .document("" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                        username = document.getString("username");
                        recordPref = document.getString("preferences");
                        userDpURL = document.getString("image_url");

                        saveDataLocally();
//                        Log.i("LOGGER","First "+document.getString("first"));
//                        Log.i("LOGGER","Last "+document.getString("last"));
//                        Log.i("LOGGER","Born "+document.getString("born"));
                    } else {
                        Log.d("Result", "No such document");
                    }
                } else {
                    Log.d("Result", task.getException().toString());
                }

            }
        });


    }


    public void setRecordPref(String recordPref) {
        this.recordPref = recordPref;
        Log.e("Info:", "in setRecordPref()" + System.currentTimeMillis());
        createUserDoc();//we create the User document for Firebase Firestore

    }


    private void createUserDoc() {
        user.put("username", username);
        user.put("preference", recordPref);
        user.put("image_url", null);
        user.put("last logged in", new Timestamp(new Date()));
        Log.e("Info:", "in createUserDoc()" + System.currentTimeMillis());

        writeToFirestore(); //writing all of this to Firebase
    }


    private void writeToFirestore() {

        db.collection("Users")
                .document("" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Info", "DocumentSnapshot successfully written!");
                        Log.e("Info:", "in onSuccess()" + System.currentTimeMillis());
                        saveDataLocally();
                    }

//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("Document added", documentReference.getId() + "");
//                        saveDataLocally();
//                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Error adding document", e);
            }
        });
    }

    private void saveDataLocally() {
        Log.e("Info:", "in saveDataLocally()" + System.currentTimeMillis());
        SharedPreferences.Editor editor = sharedPreferences.edit(); //saving the data locally for fast retrieval and saving no of reads in the firestore

        editor.putString(USERNAME, username);
        editor.putString(PREFERENCE, recordPref);
        editor.putString(IMG_URL, userDpURL);

        editor.apply();
    }

    public void onGenreClicked() {
        Log.e("Info:", "in onGenreClicked()" + System.currentTimeMillis());
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new HostFragment(), "Host").commit();
    }


    public void logOut() {//to logout of the app
        FirebaseAuth.getInstance().signOut();
        //activity.finish();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void hideHost() {
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Host")).commit();//to hide the host fragment
        //add the settings page or if already added show it
        if (fragmentManager.findFragmentByTag("Settings") != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Settings")).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, new SettingsFragment(), "Settings").addToBackStack(null).commit();
        }
    }

//    private void createMusicCollection() {
//        musicDoc.put("Title", "");
//
//    }

    @Override
    public void onBackPressed() {
        Log.e("Info", "in onBackPressed()");
        test = (SettingsFragment) fragmentManager.findFragmentByTag("Settings");
        if (test != null && test.isVisible()) { //here we see where the back button has been hit
            //if the back has been hit in the settings fragment we hide it and show the host fragment again
            Log.e("Info", "In special settings condn");
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Settings")).commit();
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Host")).commit();
        } else {
            //default behavior
            Log.e("Info", "default behavior");

            super.onBackPressed();
            finish();

        }

    }//onBackPressed() ends

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity", "onRestart()");
    }
}//class ends