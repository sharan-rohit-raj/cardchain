package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.View;
import android.view.Window;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    Button forg_btn;
    EditText emailField;
    ProgressBar forgProg;
    private String email;
    private String TAG = "ForgotPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forg_btn = findViewById(R.id.forg_btn);
        emailField = findViewById(R.id.forgEmail);
        forgProg = findViewById(R.id.forg_prog);
        final Validation validation = new Validation();
        final AuthenticationHandler authHandler = new AuthenticationHandler();
        forg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailField.getText().toString().equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Cannot leave Email field empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(validation.emailValidation(emailField.getText().toString().trim()) == false){
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid EmailId", Toast.LENGTH_SHORT).show();
                }

                forgProg.setVisibility(View.VISIBLE);
                forg_btn.setVisibility(View.INVISIBLE);
                email = emailField.getText().toString().trim();

                authHandler.forgotPassword(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            forgProg.setVisibility(View.INVISIBLE);
                            forg_btn.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Email sent.");
                            successDialog(getString(R.string.forg_dial));
                        }
                        else{
                            forgProg.setVisibility(View.INVISIBLE);
                            forg_btn.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Failed to send email.");
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void successDialog(String title){
        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);
        Button dialog_button = dialog.findViewById(R.id.suc_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.suc_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    public void returnToLoginFunc(View view) {
        this.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        forgProg.setVisibility(View.INVISIBLE);
    }
}