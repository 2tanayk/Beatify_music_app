package com.myapp.beatify;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {
    private static final int GET_IMAGE_REQUEST = 1010;

    private Uri mImageUri;

    private MainActivity maRef;

    private String dbImageUri;
    String check;


    private View view;
    private EditText usernameTxt;
    private ImageView userImg;
    private Button profileUpdateBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    DocumentReference currentUser = db.collection("Users")
            .document("" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    SharedPreferences sharedPreferences;

    public final String SHARED_PREFS = "sharedPrefs";


//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String USERNAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maRef = (MainActivity) getActivity();
        MainActivity haRef = (MainActivity) getActivity();

//        Log.e("Address1", maRef.toString());
//        Log.e("Address2", this.getActivity().toString());
//        Log.e("Address3", haRef.toString());

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        if (MainActivity.s == 1 &&)

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        try {
            check = sharedPreferences.getString(maRef.IMG_URL, null);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Exception!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTxt = view.findViewById(R.id.userNameTxt);
        userImg = view.findViewById(R.id.userDPImg);
        profileUpdateBtn = view.findViewById(R.id.updateBtn);

        usernameTxt.setText(maRef.username + "");

        if (check != null) {
            try {

                //Toast.makeText(getActivity(), "fetching...", Toast.LENGTH_SHORT).show();
                mImageUri = Uri.parse(sharedPreferences.getString(maRef.IMG_URL, null) + "");

                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(mImageUri)
                        .into(userImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Its null", Toast.LENGTH_SHORT).show();
        }

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maRef.username = usernameTxt.getText().toString();

                updateUsername();
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromGallery();
            }
        });

//        save();
    }


    private void getFromGallery() {
        Intent imgIntent = new Intent();
        imgIntent.setType("image/*");
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgIntent, GET_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(mImageUri)
                    .into(userImg);
            uploadToStorage();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadToStorage() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_LONG).show();

//                            dbImageUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            Toast.makeText(getActivity(), "Image uploaded!", Toast.LENGTH_SHORT).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dbImageUri = uri.toString();
                                    updateUserDP();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Update failed :(", e.toString() + "");
                                    Toast.makeText(getActivity(), "Oops :(", Toast.LENGTH_SHORT).show();
                                }
                            });


//                            saveDataLocally(1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error", e.toString());
                    Toast.makeText(getActivity(), "Oops :(", Toast.LENGTH_SHORT).show();
                }
            });

//            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    getmImageUri = uri;
//                }
//            });

        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    //    public void updateProfile(View view) {
//
//
//    }

    private void updateUsername() {
        currentUser.update("username", usernameTxt.getText().toString() + "")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                            saveDataLocally(0);
                        } else {
                            Log.e("Oops", Objects.requireNonNull(task.getException()).toString());
                        }

                    }
                });
    }

    private void updateUserDP() {
        currentUser.update("image_url", dbImageUri + "")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                            saveDataLocally(1);
                        } else {
                            Toast.makeText(getActivity(), "couldnt write to firebase", Toast.LENGTH_SHORT).show();
                            Log.e("Oops", Objects.requireNonNull(task.getException()).toString());
                        }

                    }
                });

    }

    private void saveDataLocally(int w) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (w) {
            case 0:
                editor.putString(maRef.USERNAME, maRef.username);
                break;
            case 1:
                editor.putString(maRef.IMG_URL, mImageUri.toString());
                break;
            default:
                Toast.makeText(getActivity(), "No match :(", Toast.LENGTH_SHORT).show();
        }

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