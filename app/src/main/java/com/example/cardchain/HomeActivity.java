package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    TextView welcome;
    FirebaseAuth auth;
    Button logout;
    public static Activity HomeAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeAct = this;
        welcome = findViewById(R.id.welc);
        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        welcome.setText(user.getEmail());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                setResult(Activity.RESULT_OK,intent);
                startActivity(intent);
            }
        });
    }
}