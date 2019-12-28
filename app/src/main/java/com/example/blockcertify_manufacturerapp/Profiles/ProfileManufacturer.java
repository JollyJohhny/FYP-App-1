package com.example.blockcertify_manufacturerapp.Profiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blockcertify_manufacturerapp.AddProduct;
import com.example.blockcertify_manufacturerapp.ChangePasswords.ChangePassword;
import com.example.blockcertify_manufacturerapp.HomePage;
import com.example.blockcertify_manufacturerapp.R;
import com.example.blockcertify_manufacturerapp.Register.RegisterDistributor;
import com.example.blockcertify_manufacturerapp.Register.RegisterRetailer;
import com.example.blockcertify_manufacturerapp.ViewAllProducts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileManufacturer extends AppCompatActivity {
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manufacturer);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();
    }

    public void AddProduct(View v){
        Intent intent2 = new Intent(getBaseContext(), AddProduct.class);
        startActivity(intent2);

    }

    public void Logout(View v){
        FirebaseAuth.getInstance().signOut(); //End user session
        startActivity(new Intent(ProfileManufacturer.this, HomePage.class)); //Go back to home page
        finish();
    }

    public void ViewProducts(View v){
        Intent intent2 = new Intent(getBaseContext(), ViewAllProducts.class);

        startActivity(intent2);
    }

    public void ChangePassword(View v){
        Intent intent2 = new Intent(getBaseContext(), ChangePassword.class);
        startActivity(intent2);
    }

    public void RegDist(View v){
        Intent intent2 = new Intent(getBaseContext(), RegisterDistributor.class);
        startActivity(intent2);
    }
}
