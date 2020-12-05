package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private static final int HOME_LOGIN_SIGNAL = 10;
    private static final String TAG  = "LoginActivity";
    private Button login, signup;
    private ProgressBar loginProg;
    private EditText emailField;
    private EditText passField;
    private String emailId;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.login_preference), Context.MODE_PRIVATE);
        String emailValue = sharedPref.getString("lastEmail", "");
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.forg_btn);
        loginProg = findViewById(R.id.loginProgress);
        emailField = findViewById(R.id.forgEmail);
        passField = findViewById(R.id.logPass);
        signup = findViewById(R.id.login_signup_btn);
        final Validation validation = new Validation();
        final AuthenticationHandler authHandle = new AuthenticationHandler();
        emailField.setText(emailValue);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailField.getText().toString().equals("") || passField.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Must not leave any field empty !", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(validation.emailValidation(emailField.getText().toString().trim()) == false){
                    Toast.makeText(LoginActivity.this, "Email Id is invalid !", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginProg.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                emailId = emailField.getText().toString().trim();
                password = passField.getText().toString();

                authHandle.signIn(emailId,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "login:success");
                            login.setVisibility(View.VISIBLE);
                            loginProg.setVisibility(View.INVISIBLE);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            updateUI(user);
                            editor.putString("lastEmail", emailField.getText().toString());
                            editor.commit();
                        }
                        else{
                            // If sign in fails, display a message to the user.
                            login.setVisibility(View.VISIBLE);
                            loginProg.setVisibility(View.INVISIBLE);

                            //TODO:Yet to add a wrong credentials login dialog box
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                errorDialog(getString(R.string.invalid_cred));
                            }
                            else if(task.getException() instanceof FirebaseTooManyRequestsException){
                                errorDialog((getString(R.string.too_many_attempts)));
                            }else if(task.getException() instanceof FirebaseNetworkException){
                                errorDialog(getString(R.string.connectivity_err));
                            }
                            else{
                                errorDialog((getString(R.string.other_issue)));
                            }
                            Log.d(TAG, "login :failure", task.getException());

                        }
                    }
                });
            }
        });
    }


    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void forgotPassFunc(View view) {
        Intent forgotPass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(forgotPass);
    }
//    public void returnToWelcomeFunc(View view) {
//        this.onBackPressed();
//    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginProg.setVisibility(View.INVISIBLE);
    }
    public void errorDialog(String title){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.err_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.err_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}