package com.example.blockcertify_manufacturerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.blockcertify_manufacturerapp.Extra.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllProducts extends AppCompatActivity {
    ListView listView ;
    public static ArrayList<ListType> AllProducts;
    ProductAdapter myAdapter;
    String ManuId;
    private LoadingDialog loadingDialog;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    boolean Flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);

        getSupportActionBar().setTitle("All Products List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        loadingDialog = new LoadingDialog(this, R.style.DialogLoadingTheme);
        loadingDialog.show();


        listView = findViewById(R.id.ListId);


        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        firebaseAuth = FirebaseAuth.getInstance();
        ManuId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        ShowProducts();
    }

    public void ShowProducts(){
        AllProducts = new ArrayList<ListType>();
        AddProductsToList();







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ProductId = AllProducts.get(position).getId();
                Intent intent2 = new Intent(getBaseContext(), ViewProduct.class);
                intent2.putExtra("PRODUCTID", ProductId);
                startActivity(intent2);
            }
        });
    }

    public void AddProductsToList(){
        AllProducts = new ArrayList<ListType>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ManuIdGet = snapshot.child("companyId").getValue(String.class);
                    if(ManuIdGet.equals(ManuId)){
                        String name = snapshot.child("name").getValue(String.class);
                        String time = snapshot.child("manufacturingDate").getValue(String.class);
                        String proId = snapshot.getKey();

                        ListType Product = new ListType(name,proId,time);
                        AllProducts.add(Product);


                    }
                }
                Show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }

        });
    }

    public void Show(){
        myAdapter = new ProductAdapter(this, AllProducts);
        listView.setAdapter(myAdapter);

        TextView txtWarn = findViewById(R.id.txtWarn);

        if(AllProducts.size() == 0 ){
            txtWarn.setVisibility(View.VISIBLE);
        }
        else{
            txtWarn.setText("All Products List");
            txtWarn.setVisibility(View.VISIBLE);
        }

        loadingDialog.dismiss();
    }

}
