package MainFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cardchain.HomeActivity;
import com.example.cardchain.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

public class FragmentAdd extends Fragment {
    Button addCard;
    private static final int CAMERA_PERMISSION_CODE=101;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ImageView barcodeIm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final View view =inflater.inflate((R.layout.fragment_add),container,false);
        addCard=view.findViewById(R.id.addCard);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewCard(view);
            }

        });
        return view;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WELP","IT IS HERE");
        if (requestCode== IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    final Activity act= getActivity();
                    String barcodeData=result.getContents();
                    Toast.makeText(act,"Barcode Data Read! :  "+ barcodeData,Toast.LENGTH_SHORT).show();
                    String barcodeType=result.getFormatName();
                    Map<String, Object> cardDetails = new HashMap<>();
                    cardDetails.put("Data",barcodeData);
                    cardDetails.put("BarcodeType",barcodeType);
                    db.collection("users").document(auth.getUid()).collection("cards")
                            .add(cardDetails)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference ref) {
                                    Toast.makeText(act,"DocumentSnapshot added with ID: ",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(act,"Error adding document " + e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        }
    }

    private void openScanner() {
        new IntentIntegrator(getActivity()).initiateScan();
    }

    private boolean checkPermission(String permission){
        int result= ContextCompat.checkSelfPermission(getActivity(),permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(String permission,int code){
        if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permission)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{permission},code);
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
