package com.example.blockcertify_manufacturerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blockcertify_manufacturerapp.Extra.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddProduct extends AppCompatActivity {
    private LoadingDialog loadingDialog;

    EditText txtName,txtPrice,txtDetails,txtDate;
    Button btnAdd,btnPrint;
    ImageView img;
    Bitmap bitmap;
    static String TAG = "GenerateQRCode";
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    String ProductId = "", BlockId = "";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String Name,Price,Details,ExpiryDate,CompanyId,ts;
    Blockchain blockchain;
    Block block;
    boolean BlockchainFlag = false;

    // Date Stuff

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        loadingDialog = new LoadingDialog(this, R.style.DialogLoadingTheme);

        // Casting
        txtName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtProductPrice);
        txtDetails = findViewById(R.id.txtProductDetails);
        txtDate = findViewById(R.id.txtExpiryDate);
        img = findViewById(R.id.imgQrCode);
        btnPrint = findViewById(R.id.btnPrint);


        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setDate(View v) {
        // TODO Auto-generated method stub
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void Add(View v){
        boolean flag = true;

        Name = txtName.getText().toString();
        Price = txtPrice.getText().toString();
        Details = txtDetails.getText().toString();
        ExpiryDate = txtDate.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        ts = df.format(c);

        CompanyId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if(TextUtils.isEmpty(Name)){
            Toast.makeText(this, "Please Enter Name of Product!",
                    Toast.LENGTH_LONG).show();
            flag = false;
        }
        if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Please Enter Price of Product!",
                    Toast.LENGTH_LONG).show();
            flag = false;
        }
        if(TextUtils.isEmpty(Details)){
            Toast.makeText(this, "Please Enter Details of Product!",
                    Toast.LENGTH_LONG).show();
            flag = false;
        }
        if(TextUtils.isEmpty(ExpiryDate)){
            Toast.makeText(this, "Please Enter Expiry Date of Product!",
                    Toast.LENGTH_LONG).show();
            flag = false;
        }


        if(flag){

            loadingDialog.show();
            AddProduct();
            AddBlock();
            String encryptedString = encrypt();
            ShowQr(encryptedString);
            btnPrint.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);


        }
    }

    public void AddProduct(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        //Adding Product
        Product pro = new Product(Name,Price,Details,CompanyId,ts,ExpiryDate,"M");
        ProductId = databaseReference.push().getKey();
        databaseReference.child(ProductId).setValue(pro);

    }

    public void AddBlock(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Blockchain");

        GetBlockChain();


    }

    public void GetBlockChain(){
        blockchain = new Blockchain(4);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int a = 1;
                    String data = snapshot.child("data").getValue(String.class);
                    String hash = snapshot.child("hash").getValue(String.class);
                    int index = snapshot.child("index").getValue(int.class);
                    long timestamp = snapshot.child("timestamp").getValue(long.class);
                    if(blockchain.blocks.size() == 0){
                        block = new Block(index, timestamp, null, data);
                    }
                    else {
                        block = new Block(index, timestamp, blockchain.latestBlock().getHash(), data);
                    }

                    block.mineBlock(4);
                    blockchain.blocks.add(block);
                }
                AddBlockNow();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }

        });
    }

    public void AddBlockNow(){
        blockchain.addBlock(blockchain.newBlock(ProductId));
        block = blockchain.latestBlock();
        BlockId = databaseReference.push().getKey();
        databaseReference.child(BlockId).setValue(block);

        loadingDialog.dismiss();

        Toast.makeText(AddProduct.this, "Product Added and QR code is generated!", Toast.LENGTH_SHORT).show();


    }

    public String encrypt(){
        // Encrytion Process
        String encrypted = "";
        try {
            encrypted = AESUtils.encrypt(ProductId);
            Log.d("TEST", "encrypted:" + encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public void ShowQr(String encrypted){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(encrypted , null, QRGContents.Type.TEXT, smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }

    public void PrintIt(View v){


        PrintHelper photoPrinter = new PrintHelper(AddProduct.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        photoPrinter.printBitmap("QR_" + ProductId,bitmap);
    }




}
