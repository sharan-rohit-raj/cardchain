package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class AddCardActivity extends AppCompatActivity {
    EditText ecardHoldName, ecardName;
    TextView vcardNumber, vcardName, vcardCompany;
    Button photoScan;
    ImageView addCardImage;
    Button addCard;
    ImageButton backBtn;
    private static final int CAMERA_PERMISSION_CODE=101;
    private static final int SCAN_PHOTO=1001;
    FirebaseAuth auth;
    FirebaseFirestore db;
    int cardnumCount;
    ConnectivityManager cm;
    Map<String, Object> cardDetails;
    String barcodeData;
    String barcodeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        addCard= findViewById(R.id.scan_btn);
        ecardHoldName = findViewById(R.id.edit_card_hold);
        ecardName = findViewById(R.id.card_name);
        vcardNumber = findViewById(R.id.card_num_view);
        vcardName = findViewById(R.id.card_hold_name);
        vcardCompany = findViewById(R.id.card_name_view);
        addCardImage = findViewById(R.id.add_card_img);
        backBtn = findViewById(R.id.add_card_back_btn);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        barcodeData="";
        ArrayList<Integer> cardImages = new ArrayList<>();
        cardImages.add(R.drawable.pattern1);
        cardImages.add(R.drawable.pattern2);
        cardImages.add(R.drawable.pattern3);
        Random rand = new Random();
        int randomElement = cardImages.get(rand.nextInt(cardImages.size()));
        addCardImage.setImageResource(randomElement);
        ImageBlur imageBlur = new ImageBlur(addCardImage.getContext());
        imageBlur.makeBlur(addCardImage);

        cardnumCount = 0;
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ecardHoldName.getText().toString().equals("") || ecardName.getText().toString().equals("")){
                    Toast.makeText(AddCardActivity.this,"Please Fill Out Holder Name and Card Name",Toast.LENGTH_SHORT).show();
                }else {
                    AddNewCard(view);
                }
            }
        });

        TextWatcher cardHoldWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                vcardName.setText(editable.toString());
            }
        };
        ecardHoldName.addTextChangedListener(cardHoldWatcher);

        TextWatcher cardMonthWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                vcardCompany.setText(editable.toString());
            }
        };
        ecardName.addTextChangedListener(cardMonthWatcher);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCardActivity.this.onBackPressed();
            }
        });
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
        if (requestCode== IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    final Activity act= AddCardActivity.this;
                    barcodeData=result.getContents();
//                    Toast.makeText(act,"Barcode Data Read! :  "+ barcodeData,Toast.LENGTH_SHORT).show();
                    barcodeType=result.getFormatName();
                    vcardNumber.setText(barcodeData);
                }
            }
        }else if (requestCode == SCAN_PHOTO){
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                //getting the image
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //decoding bitmap
            Bitmap bMap = BitmapFactory.decodeStream(imageStream);
            int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
            // copy pixel data from the Bitmap into the 'intArray' array
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
                    bMap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
                    bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            MultiFormatReader reader = new MultiFormatReader();// use this otherwise
            // ChecksumException
            try {
                Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
                decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

                Result result = reader.decode(bitmap, decodeHints);
                //*I have created a global string variable by the name of barcode to easily manipulate data across the application*//
                if (result != null) {
                    final Activity act = AddCardActivity.this;
                    barcodeData = result.getText().toString();
                    barcodeType = result.getBarcodeFormat().toString();
                    vcardNumber.setText(barcodeData);

                }
            } catch( Exception e){
                Toast.makeText(AddCardActivity.this,getString(R.string.use_clear_img),Toast.LENGTH_SHORT).show();

            }
        }
//        finish();
    }
    public void ScanPhoto(View view){
        if(checkConnection()){
            if (ecardHoldName.getText().toString().equals("") || ecardName.getText().toString().equals("")){
                Toast.makeText(AddCardActivity.this,getString(R.string.card_fill_name_and_holder),Toast.LENGTH_SHORT).show();
            }
            else {
                Intent photoPic = new Intent(Intent.ACTION_PICK);
                photoPic.setType("image/*");
                startActivityForResult(photoPic, SCAN_PHOTO);
            }
        }else{
            errorDialog(getString(R.string.connectivity_err));
        }

    }

    public void SaveCard(View view){
        cardDetails=new HashMap<>();
        if (!barcodeData.equals("")){
                cardDetails.put("cardholder", ecardHoldName.getText().toString());
                cardDetails.put("cardname", ecardName.getText().toString());
//                    if (ecardNumber.getText().toString().equals("")) {
                cardDetails.put("cardnumber", barcodeData.substring(0, Math.min(16, barcodeData.length())));
//                    } else {
//                        cardDetails.put("cardnumber", ecardNumber.getText().toString());
//                    }


                cardDetails.put("Data", barcodeData);
                cardDetails.put("BarcodeType", barcodeType);
                cardDetails.put("tag", "all");

                Query docu = db.collection("users").document(auth.getUid()).collection("cards").whereEqualTo("tag","all");
                docu.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String firecard = document.get("Data").toString();
                                if(firecard.equals(barcodeData)){
                                    errorCardDialog(getString(R.string.card_exists), cardDetails);
                                    return;
                                }
                            }

                            saveCardToFireStore(cardDetails);

                        } else {
                            Toast.makeText(AddCardActivity.this,getString(R.string.other_issue),Toast.LENGTH_SHORT).show();
                        }
                    }


                });

        }
        else{
            Toast.makeText(AddCardActivity.this,"Scan Barcode First",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCardToFireStore(Map<String, Object> cardDetails) {
        db.collection("users").document(auth.getUid()).collection("cards")
                .add(cardDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference ref) {
                        successDialog(getString(R.string.card_saved));
                        //Toast.makeText(AddCardActivity.this, getString(R.string.card_saved), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCardActivity.this, getString(R.string.card_save_err), Toast.LENGTH_LONG).show();
                    }
                });
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

    private void openScanner() {
        new IntentIntegrator(AddCardActivity.this).initiateScan();
    }

    private boolean checkPermission(String permission){
        int result= ContextCompat.checkSelfPermission(this,permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(String permission,int code){
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
            ActivityCompat.requestPermissions(this,new String[]{permission},code);
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

    private boolean checkConnection() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void errorDialog(String title){
        final Dialog dialog = new Dialog(AddCardActivity.this);
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

    public void errorCardDialog(String title, final Map<String, Object> cardDetails){
        final Dialog dialog = new Dialog(AddCardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exist_card_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.err_ok_btn);
        Button dialog_cancel = dialog.findViewById(R.id.err_ok_btn2);
        TextView dialog_text = dialog.findViewById(R.id.err_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCardToFireStore(cardDetails);
                dialog.dismiss();
            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void successDialog(String title){
        final Dialog dialog = new Dialog(AddCardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.err_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.err_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}