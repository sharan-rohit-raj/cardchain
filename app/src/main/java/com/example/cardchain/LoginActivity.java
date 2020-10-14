package com.example.cardchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void forgotPassFunc(View view) {
        Intent forgotPass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(forgotPass);
    }
    public void returnToWelcomeFunc(View view) {
        this.onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


}