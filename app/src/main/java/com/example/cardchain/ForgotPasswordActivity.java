package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

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
                            successDialog(getString(R.string.forg_dial));
                        }
                        else{
                            forgProg.setVisibility(View.INVISIBLE);
                            forg_btn.setVisibility(View.VISIBLE);
                            if(task.getException() instanceof FirebaseAuthInvalidUserException || task.getException() instanceof FirebaseAuthEmailException){
                                errorDialog(getString(R.string.forg_pass_email_err));
                            }else if(task.getException() instanceof FirebaseNetworkException){
                                errorDialog(getString(R.string.internet_lost));
                            }else{
                                errorDialog(getString(R.string.other_issue));
                            }
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
        Button dialog_button = dialog.findViewById(R.id.err_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.err_dialog_txt);
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

    public void errorDialog(String title){
        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
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

    public void returnToLoginFunc(View view) {
        this.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        forgProg.setVisibility(View.INVISIBLE);
    }
}