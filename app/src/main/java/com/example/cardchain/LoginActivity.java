package com.example.cardchain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private static final int HOME_LOGIN_SIGNAL = 10;
    private static final String TAG  = "LoginActivity";
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivityForResult(intent,HOME_LOGIN_SIGNAL);
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == HOME_LOGIN_SIGNAL){
        if(resultCode == Activity.RESULT_OK){
            Log.d(TAG,"Closing HomeActivity");
            HomeActivity.HomeAct.finish();
        }
    }


    }
}