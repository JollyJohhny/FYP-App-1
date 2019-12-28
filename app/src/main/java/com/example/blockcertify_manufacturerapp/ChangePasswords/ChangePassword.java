package com.example.blockcertify_manufacturerapp.ChangePasswords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blockcertify_manufacturerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    EditText txtNewPwd,txtCnfrmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        txtNewPwd = findViewById(R.id.txtNewPwd);
        txtCnfrmPwd = findViewById(R.id.txtCnfrmPwd);
    }

    public void SaveChanges(View v){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(txtNewPwd.getText().toString().equals(txtCnfrmPwd.getText().toString())){
            user.updatePassword(txtCnfrmPwd.getText().toString());

            Toast.makeText(ChangePassword.this, "Password Updated!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ChangePassword.this, "Password does not match!", Toast.LENGTH_SHORT).show();
        }

        finish(); // Move back to last activity



    }
}
