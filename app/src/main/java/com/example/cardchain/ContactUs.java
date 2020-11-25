package com.example.cardchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactUs extends AppCompatActivity {
    EditText emailMsg;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        emailMsg = findViewById(R.id.email_msg);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void contact_us_back(View view) {
        this.onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void sendEmail(View view) {
        if(emailMsg.getText().toString().equals("") == false){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {"bknarfk@gmail.com","srrnatar@gmail.com"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
            String cust_name = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : "User";
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from CardChain customer: "+cust_name);
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailMsg.getText().toString());
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Choose an email Client"));

        }else{
            Snackbar snackbar = Snackbar.make(view, R.string.empty_msg_box,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}