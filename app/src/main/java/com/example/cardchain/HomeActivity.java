package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    TextView welcome;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Button logout;
    private ImageView barcode;
    private boolean alreadyCalled;
    private static final int CAMERA_PERMISSION_CODE=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcome = findViewById(R.id.welc);
        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        alreadyCalled = false;
        FirebaseUser user = auth.getCurrentUser();
        barcode=findViewById(R.id.BarcodeTestDisplay);
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
        if(firebaseAuth.getCurrentUser() == null && !alreadyCalled){
            Log.d("FirebaseHome","Calling start login");
            alreadyCalled = true;
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void AddNewCard(View view) {
        if(Build.VERSION.SDK_INT>=23){
            if(checkPermission(Manifest.permission.CAMERA)){
                openScanner();
            }
            else{
                requestPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
            }
        }
        else{
            openScanner();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    String barcodeData=result.getContents();
                    String barcodeType=result.getFormatName();
                    Map<String, Object> cardDetails = new HashMap<>();
                    cardDetails.put("Data",barcodeData);
                    cardDetails.put("BarcodeType",barcodeType);
                    Log.v("BENNy",auth.getUid());
                    db.collection("users").document(auth.getUid()).collection("cards")
                            .add(cardDetails)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference ref) {
                                    Toast.makeText(HomeActivity.this,"DocumentSnapshot added with ID: ",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(HomeActivity.this,"Error adding document " + e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(barcodeData, BarcodeFormat.valueOf(barcodeType), 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        barcode.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void openScanner() {
        new IntentIntegrator(HomeActivity.this).initiateScan();
    }

    private boolean checkPermission(String permission){
        int result= ContextCompat.checkSelfPermission(HomeActivity.this,permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(String permission,int code){
        if(!ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permission)){
            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{permission},code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openScanner();
                }
        }
    }
}