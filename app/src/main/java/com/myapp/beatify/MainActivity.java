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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //MainActivity which acts as a fragment holder
    public static FragmentManager fragmentManager;
    private static final String COLLECTION_TITLE = "Music";
    public static AppCompatActivity activity = null;
    private static SettingsFragment test;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    public static final String PREFERENCE = "preference";

    private SharedPreferences sharedPreferences;

    protected static String username = "";
    protected String preferences = "";
    public String recordPref;
    //Initialized a reference of CF
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Map<String, Object> user = new HashMap<>();
    private Map<String, Object> note = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        Intent mI = getIntent();

        int s = mI.getIntExtra("STATUS", 1);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        if (s == 0) {
            username = mI.getStringExtra("USERNAME");
        } else {
            username = sharedPreferences.getString(USERNAME, null);

            if (username == null) {
                readFromFirestore();
            }

//            Toast.makeText(this, "" + sharedPreferences.getString(PREFERENCE, "nope"), Toast.LENGTH_SHORT).show();
        }

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }//inner if ends

//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //PreferencesFragment preferencesFragment = new PreferencesFragment();
            if (s == 0) {
                fragmentManager.beginTransaction().add(R.id.fragment_container, new PreferencesFragment(), "Preferences").commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container, new HostFragment(), "Host").addToBackStack(null).commit();
            }
//            fragmentTransaction.commit();
//            createMusicCollection();
        }//if ends
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

        createUserDoc();
    }


    private void createUserDoc() {
        user.put("username", username);
        user.put("preference", recordPref);

        writeToFirestore();
    }


    private void writeToFirestore() {
        db.collection("Users")
                .document("" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Info", "DocumentSnapshot successfully written!");
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PREFERENCE, recordPref);
        editor.apply();
    }

    public static void onGenreClicked() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new HostFragment(), "Host").addToBackStack(null).commit();

    }


    //    private void createMusicCollection() {
//        note.put("Title1", "");
//
//    }

    public static void logOut() {
        FirebaseAuth.getInstance().signOut();
        activity.finish();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    public static void hideHost() {
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Host")).commit();

        if (fragmentManager.findFragmentByTag("Settings") != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Settings")).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, new SettingsFragment(), "Settings").addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        test = (SettingsFragment) fragmentManager.findFragmentByTag("Settings");
        if (test != null && test.isVisible()) {
            //DO STUFF
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Settings")).commit();
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Host")).commit();
        } else {
            //Whatever
            super.onBackPressed();
        }

    }
}//class ends