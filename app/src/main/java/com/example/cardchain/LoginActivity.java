package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
}