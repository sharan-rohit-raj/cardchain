package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    EditText firstName, lastName, phNo, emailField;
    Button saveBtn;
    ImageButton backBtn;
    String first_name, last_name, ph_num, email_id;
    EditProfileModel editProfileModel;
    ProgressBar progressBar, initialProgBar;
    Validation validation;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean valueChanged = false;
    String TAG = "EditProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firstName = findViewById(R.id.edit_firstName);
        TextWatcher firstNameWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                valueChanged = true;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };
        firstName.addTextChangedListener(firstNameWatcher);

        lastName = findViewById(R.id.edit_lastName);
        TextWatcher lastNameWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                valueChanged = true;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        lastName.addTextChangedListener(lastNameWatcher);


        phNo = findViewById(R.id.edit_phNo);
        TextWatcher phNoWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                valueChanged = true;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };
        phNo.addTextChangedListener(phNoWatcher);


        saveBtn = findViewById(R.id.edit_prof_save);
        backBtn = findViewById(R.id.edit_prof_back);
        progressBar = findViewById(R.id.edit_progBar);
        initialProgBar = findViewById(R.id.edit_prof_prog_bar);
        initialProgBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        emailField = findViewById(R.id.edit_email);
        TextWatcher emailFieldWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                valueChanged = true;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };
        emailField.addTextChangedListener(emailFieldWatcher);
        emailField.setText(user.getEmail());

        validation = new Validation();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || phNo.getText().toString().equals("") || emailField.getText().toString().equals("")){
                    Toast.makeText(EditProfile.this, "Must fill all the fields...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(validation.phoneNumValidation(phNo.getText().toString().trim()) == false){
                    Toast.makeText(EditProfile.this, "Seems you have provided an invalid phone number...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(validation.emailValidation(emailField.getText().toString().trim()) == false) {
                    Toast.makeText(EditProfile.this, "Seems you have provided an invalid email address...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(valueChanged == false){
                    Toast.makeText(EditProfile.this, "The information you see is already saved...", Toast.LENGTH_SHORT).show();
                } else{
                    saveBtn.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    first_name = firstName.getText().toString().trim();
                    last_name = lastName.getText().toString().trim();
                    ph_num = phNo.getText().toString().trim();
                    email_id = emailField.getText().toString().trim();
                    editProfileModel = new EditProfileModel(first_name, last_name, ph_num);
                    saveAuthInfo(editProfileModel);
                    saveData(editProfileModel);
                }
            }
        });
    }


    public void  saveAuthInfo(EditProfileModel editProfileModel){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editProfileModel.getFirst_name())
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialProgBar.setVisibility(View.VISIBLE);
        fillFields();
        initialProgBar.setVisibility(View.INVISIBLE);
    }

    public void saveData(EditProfileModel editProfileModel){
        db.collection("users").document(auth.getUid())
                .set(editProfileModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this, "Profile information successfully saved!", Toast.LENGTH_SHORT).show();
                saveBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                saveBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Log.w(TAG, "Error writing document", e);
            }
        });
        return;
    }

    public void fillFields(){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                initialProgBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> userInfos =  document.getData();
                        if(userInfos.get("first_name") != null && userInfos.get("last_name") != null && userInfos.get("phone_num") != null){
                            firstName.setText(userInfos.get("first_name").toString());
                            lastName.setText(userInfos.get("last_name").toString());
                            phNo.setText(userInfos.get("phone_num").toString());
                            valueChanged = false;
                            initialProgBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                        initialProgBar.setVisibility(View.INVISIBLE);

                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                    initialProgBar.setVisibility(View.INVISIBLE);

                }
            }
        });
        return;
    }
}
