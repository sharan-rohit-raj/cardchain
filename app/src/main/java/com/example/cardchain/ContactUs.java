package com.example.cardchain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactUs extends AppCompatActivity {
    EditText emailMsg;
    FirebaseAuth auth;
    FirebaseUser user;
    ConnectivityManager cm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        emailMsg = findViewById(R.id.email_msg);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private boolean checkConnection() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
        if(checkConnection()) {
            if (emailMsg.getText().toString().equals("") == false) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"bknarfk@gmail.com", "srrnatar@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                String cust_name = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : "User";
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from CardChain customer: " + cust_name);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailMsg.getText().toString());
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an email Client"));

            } else {
                Snackbar snackbar = Snackbar.make(view, R.string.empty_msg_box, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }else{
            errorDialog(getString(R.string.connectivity_err));
        }
    }

    public void errorDialog(String title){
        final Dialog dialog = new Dialog(ContactUs.this);
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
}