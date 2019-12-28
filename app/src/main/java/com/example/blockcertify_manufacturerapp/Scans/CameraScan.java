package com.example.blockcertify_manufacturerapp.Scans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blockcertify_manufacturerapp.AESUtils;
import com.example.blockcertify_manufacturerapp.ListType;
import com.example.blockcertify_manufacturerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CameraScan extends AppCompatActivity {

    private IntentIntegrator qrScan;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String decrypted = "";
    boolean Flag = false;
    String realId = "";
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                String scannedValue = result.getContents();
                // Check if product exists
                ProductExists(scannedValue);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void ProductExists(String value){
        //        Decryption Process

        try {
            decrypted = AESUtils.decrypt(value);
            Log.d("TEST", "decrypted:" + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Product Check
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ProductId = snapshot.getKey();
                    if(ProductId.equals(decrypted)){
                        status = snapshot.child("status").getValue(String.class);
                        realId = ProductId;
                        Flag = true;
                        break;
                    }
                }
                Checked();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }

        });
    }

    public void Checked(){
        if(Flag == true){
            ScanTypeCheck();
        }
        else {
            Toast.makeText(this, "Not a Blockcertify Code! Product do not exists!", Toast.LENGTH_LONG).show();
            finish();
        }
    }



    public void ScanTypeCheck(){
        String loggedInEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String[] splitted = loggedInEmail.split("@");
        if(splitted.length >0) {
            if(splitted[1].equals("blockcertify.retailer.com")){
                ScanAsRetailer();
            }
            if(splitted[1].equals("blockcertify.distributor.com")){
                ScanAsDisttributor();
            }
            if(splitted[1].equals("blockcertify.shopkeeper.com")){
                ScanAsShopkeeper();
            }

        }
    }

    public void ScanAsRetailer(){
        databaseReference.child(realId).child("status").setValue(status + "R");
        Toast.makeText(this, "Product Scanned!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void ScanAsDisttributor(){
        databaseReference.child(realId).child("status").setValue(status + "D");
        Toast.makeText(this, "Product Scanned!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void ScanAsShopkeeper(){
        databaseReference.child(realId).child("status").setValue(status + "S");
        Toast.makeText(this, "Product Scanned!", Toast.LENGTH_LONG).show();
        finish();
    }


}
