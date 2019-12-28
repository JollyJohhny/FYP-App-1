 package com.example.blockcertify_manufacturerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

 public class ViewProduct extends AppCompatActivity {

     TextView txtName,txtPrice,txtDetails,txtManuDate,txtExpireDate;
     ImageView img;
     Bitmap bitmap;
     String TAG = "GenerateQRCode";
     String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
     String ProductId;

     DatabaseReference databaseReference;
     FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        txtName = findViewById(R.id.txtName);
        txtManuDate = findViewById(R.id.txtManuDate);
        txtExpireDate = findViewById(R.id.txtExpiryDate);
        txtPrice = findViewById(R.id.txtPrice);
        txtDetails = findViewById(R.id.txtDetails);
        img = findViewById(R.id.imgQR);


        Intent intent = getIntent();
        ProductId= intent.getStringExtra("PRODUCTID");

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference.child(ProductId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String price = dataSnapshot.child("price").getValue(String.class);
                String details = dataSnapshot.child("details").getValue(String.class);
                String ManuDate = dataSnapshot.child("manufacturingDate").getValue(String.class);
                String ExpireDate = dataSnapshot.child("expiryDate").getValue(String.class);



                txtName.setText("Product Name: " + name);
                txtPrice.setText("Product Price: " +price);
                txtDetails.setText("Product Details: "+details);
                txtExpireDate.setText("Expiry Date: "+ExpireDate);
                txtManuDate.setText("Manufaturing Date: "+ManuDate);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        ShowQr();
    }

     public void ShowQr(){
         WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
         Display display = manager.getDefaultDisplay();
         Point point = new Point();
         display.getSize(point);
         int width = point.x;
         int height = point.y;
         int smallerDimension = width < height ? width : height;
         smallerDimension = smallerDimension * 3 / 4;

         // Encrytion Process
         String encrypted = "";
         try {
             encrypted = AESUtils.encrypt(ProductId);
             Log.d("TEST", "encrypted:" + encrypted);
         } catch (Exception e) {
             e.printStackTrace();
         }



         QRGEncoder qrgEncoder = new QRGEncoder(encrypted, null, QRGContents.Type.TEXT, smallerDimension);
         try {
             bitmap = qrgEncoder.encodeAsBitmap();
             img.setImageBitmap(bitmap);
         } catch (WriterException e) {
             Log.v(TAG, e.toString());
         }
     }

     public void PrintIt(View v){


         PrintHelper photoPrinter = new PrintHelper(ViewProduct.this);
         photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
         Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
         photoPrinter.printBitmap("QR_" + txtManuDate.getText(),bitmap);
     }
}
