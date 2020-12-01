package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    Button signUpButton;
    EditText emailField, passField, confpassField;
    private String emailId;
    private String pass;
    private String confPass;
    private ProgressBar progressBar;
    protected static String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Assign UI elements to Java
        signUpButton = findViewById(R.id.forg_btn);
        emailField = findViewById(R.id.forgEmail);
        passField = findViewById(R.id.logPass);
        confpassField = findViewById(R.id.sign_conf_pass);
        progressBar = findViewById(R.id.loginProgress);
        final Validation validation = new Validation();
        final AuthenticationHandler authHandler = new AuthenticationHandler();



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailField.getText().toString().equals("") || passField.getText().toString().equals("") ||
                confpassField.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this, "Must not leave any field empty !", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(validation.emailValidation(emailField.getText().toString().trim()) == false){
                    DialogMessage.display(SignUpActivity.this, getString(R.string.dial_invalid_email), DialogMessage.ERROR_MESSAGE_TYPE);

                    return;
                }
                else if(passField.getText().toString().equals(confpassField.getText().toString())== false){
                    DialogMessage.display(SignUpActivity.this, getString(R.string.dial_wrong_password), DialogMessage.ERROR_MESSAGE_TYPE);
                    return;
                }

                signUpButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                emailId = emailField.getText().toString().trim();
                pass = passField.getText().toString();


                authHandler.signUp(emailId,pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            signUpButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DialogMessage.display(SignUpActivity.this, getString(R.string.dial_suc_msg), DialogMessage.SUCCESS_MESSAGE_TYPE);
                        }
                        else{
                            // If sign in fails, display a message to the user.
                            signUpButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }

                    }

                });


            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }




//    public void returnToWelcomeFunc(View view) {
//        this.onBackPressed();
//    }
}