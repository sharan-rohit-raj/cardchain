package com.example.cardchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void SignUpFunc(View view){
        Intent signUpIntent = new Intent(WelcomeActivity.this,SignUpActivity.class);
        startActivity(signUpIntent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void LoginFunc(View view){
        Intent loginIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}