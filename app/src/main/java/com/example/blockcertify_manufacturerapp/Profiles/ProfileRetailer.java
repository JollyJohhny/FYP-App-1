package com.example.blockcertify_manufacturerapp.Profiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blockcertify_manufacturerapp.ChangePasswords.ChangePassword;
import com.example.blockcertify_manufacturerapp.HomePage;
import com.example.blockcertify_manufacturerapp.R;
import com.example.blockcertify_manufacturerapp.Register.RegisterRetailer;
import com.example.blockcertify_manufacturerapp.Register.RegisterShopkeeper;
import com.example.blockcertify_manufacturerapp.Scans.CameraScan;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileRetailer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_retailer);
    }

    public void Logout(View v){
        FirebaseAuth.getInstance().signOut(); //End user session
        startActivity(new Intent(ProfileRetailer.this, HomePage.class)); //Go back to home page
        finish();
    }

    public void ChangePassword(View v){
        Intent intent2 = new Intent(getBaseContext(), ChangePassword.class);
        startActivity(intent2);
    }

    public void ScanProduct(View v){
        Intent intent2 = new Intent(getBaseContext(), CameraScan.class);
        startActivity(intent2);
    }

    public void RegShop(View v){
        Intent intent2 = new Intent(getBaseContext(), RegisterShopkeeper.class);
        startActivity(intent2);
    }
}
