package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    TextView welcome;
    FirebaseAuth auth;
    Button logout;
    public static Activity HomeAct;
    private boolean alreadyCalled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeAct = this;
        welcome = findViewById(R.id.welc);
        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        alreadyCalled = false;
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            finish();
            return;
        }
        welcome.setText(user.getEmail());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("FirebaseHome","In Stop");
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d("FirebaseHome","Calling AuthState");
        if(firebaseAuth.getCurrentUser() == null && alreadyCalled == false){
            Log.d("FirebaseHome","Calling start login");
            alreadyCalled = true;
            startLoginActivity();
            return;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}