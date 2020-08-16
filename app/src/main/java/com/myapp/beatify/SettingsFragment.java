package com.myapp.beatify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.myapp.beatify.MainActivity.USERNAME;
import static com.myapp.beatify.MainActivity.username;


public class SettingsFragment extends Fragment {

    private View view;
    private EditText usernameTxt;
    private Button profileUpdateBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    SharedPreferences sharedPreferences;

    public static final String SHARED_PREFS = "sharedPrefs";

//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String USERNAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTxt = view.findViewById(R.id.userNameTxt);
        profileUpdateBtn = view.findViewById(R.id.updateBtn);

        usernameTxt.setText(username + "");

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameTxt.getText().toString();

                writeToFirestore();
            }
        });

//        save();
    }

//    public void updateProfile(View view) {
//
//
//    }

    private void writeToFirestore() {

        DocumentReference currentUser = db.collection("Users")
                .document("" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUser.update("username", usernameTxt.getText().toString() + "")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                            saveDataLocally();

                        } else {
                            Log.e("Oops", task.getException().toString());
                        }

                    }
                });
    }

    private void saveDataLocally() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
//        editor.putString(PREFERENCE, recordPref);
        editor.apply();
    }

//    private void save() {
//    SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//    editor.putString(MainActivity.username, "");
//    editor.apply();
//
//}
}