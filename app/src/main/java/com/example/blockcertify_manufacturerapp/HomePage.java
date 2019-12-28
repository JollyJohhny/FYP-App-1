package com.example.blockcertify_manufacturerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blockcertify_manufacturerapp.Logins.LoginDistributor;
import com.example.blockcertify_manufacturerapp.Logins.LoginManufacturer;
import com.example.blockcertify_manufacturerapp.Logins.LoginRetailer;
import com.example.blockcertify_manufacturerapp.Logins.LoginShopkeeper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void OpenManuActivity(View v){
        Intent intent = new Intent(this, LoginManufacturer.class);
        startActivity(intent);
    }

    public void OpenRetActivity(View v){
        Intent intent = new Intent(this, LoginRetailer.class);
        startActivity(intent);
    }

    public void OpenDistActivity(View v){
        Intent intent = new Intent(this, LoginDistributor.class);
        startActivity(intent);
    }


    public void OpenShopActivity(View v){
        Intent intent = new Intent(this, LoginShopkeeper.class);
        startActivity(intent);
    }
}
