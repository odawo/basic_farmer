package com.example.envy13.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewProduct extends AppCompatActivity {

    RecyclerView recycle;
    List<UserProduct> list;
    DatabaseReference myRef, myRef2;
    FirebaseDatabase database;

    String TAG, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        recycle = findViewById(R.id.recyclerView);
        recycle.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("farmer");
        myRef2 = database.getReference("imgUrl");

//        myRef = FirebaseDatabase.getInstance().getReference("farmer");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                this is called once within the initial value, and whenever data is updated

                list = new ArrayList<UserProduct>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserProduct value = dataSnapshot1.getValue(UserProduct.class);
                    UserProduct product = new UserProduct();

                    String uemail = value.getEmail();
                    String ucontact = value.getContact();
                    String ulocation = value.getLocation();
                    String uprodname = value.getProductName();
                    String uprodamount = value.getProductAmount();
                    String uproddescr = value.getProductDescription();
//                    String img = value.getImgUrl();
                    ucontact = phone;

                    product.setEmail(uemail);
                    product.setContact(ucontact);
                    product.setLocation(ulocation);
                    product.setProductName(uprodname);
                    product.setProductAmount(uprodamount);
                    product.setProductDescription(uproddescr);
//                    product.setImgUrl(img);

                    list.add(product);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: Unable to obtain values. Refresh", databaseError.toException());
            }
        });

        final Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");

        refreshPage();

        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list, ViewProduct.this);
                RecyclerView.LayoutManager recyce = new LinearLayoutManager(ViewProduct.this);  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rowns
                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
                recycle.setAdapter(recyclerAdapter);
            }
        });

    }

//    method for contacting farmer via text. sends a text message to farmer who posted the product information
    private void callFarmer() {

        Toast.makeText(this, "sorry. unable to send text at this moment", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshPage();
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "None available", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_logout:
                logoutUser();
            default:
                break;
        }
        return true;
    }

    private void logoutUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(ViewProduct.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        mAuth.signOut();
    }

    private void refreshPage() {

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list, ViewProduct.this);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(ViewProduct.this);  //new GridLayoutManager(viewproduct.this, 2) ... 2 is number or rowns
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator(new DefaultItemAnimator()); //recycclerview.itemanimator..
        recycle.setAdapter(recyclerAdapter);

    }

}
