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
    protected static String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Assign UI elements to Java
        signUpButton = findViewById(R.id.login_btn);
        emailField = findViewById(R.id.sign_email);
        passField = findViewById(R.id.sign_pass);
        confpassField = findViewById(R.id.sign_conf_pass);
        final Validation validation = new Validation();
        final AuthenticationHandler authHandler = new AuthenticationHandler();



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailField.getText().toString().equals("") && passField.getText().toString().equals("") &&
                confpassField.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this, "Must not leave any field empty !", Toast.LENGTH_SHORT).show();
                }
                else if(validation.emailValidation(emailField.getText().toString().trim()) == false){
                    Toast.makeText(SignUpActivity.this, "Email Id is invalid !", Toast.LENGTH_SHORT).show();
                }
                else if(passField.getText().toString().equals(confpassField.getText().toString())== false){
                    Toast.makeText(SignUpActivity.this, "Passwords don't match !", Toast.LENGTH_SHORT).show();
                }
                else{
                    emailId = emailField.getText().toString().trim();
                    pass = passField.getText().toString();


                    authHandler.signUp(emailId,pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                successDialog(getString(R.string.dial_suc_msg),user);
                            } else{
                                // If sign in fails, display a message to the user.

                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            }

                        }

                    });

                }
            }

        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void successDialog(String title, final FirebaseUser user_val){
        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);
        Button dialog_button = dialog.findViewById(R.id.suc_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.suc_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                intent.putExtra("user",user_val);
                dialog.dismiss();
                startActivity(intent);
            }
        });
        dialog.show();
    }


    public void returnToWelcomeFunc(View view) {
        this.onBackPressed();
    }
}