package com.example.envy13.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondActivity extends AppCompatActivity {

    Button jielimishe, juasoko;
    FirebaseAuth mAuth;
    TextView userEmail;
    String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.userEmail);
        jielimishe = findViewById(R.id.btnJielimishe);
        juasoko = findViewById(R.id.btnJuasoko);

        // check currently logged in user and set details to screen
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
//            String displayName = firebaseUser.getDisplayName();
//            if (TextUtils.isEmpty(displayName)) displayName = "No display name available";
//            userName.setText(displayName);
            String email = firebaseUser.getEmail();
            userEmail.setText(email);
        }

        jielimishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, JielimisheActivity.class));
            }
        });

        juasoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, JuasokoActivity.class));
            }
        });
    }

//    options mnu at top containing the logout button

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                Toast.makeText(this, "None available", Toast.LENGTH_SHORT)
//                        .show();
//                break;
            case R.id.action_deleteacc:
                deleteAccount();
                break;
            case R.id.action_logout:
                logoutUser();
                Toast.makeText(this, "Logging user out", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }

    private void deleteAccount() {

//        remove db value from database of current user
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("farmer");
        databaseReference.removeValue();

        Log.d(TAG, "deleteAccount: STARTING POINT");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: works fine");
                Toast.makeText(SecondActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "onFailure: failed", null);
                Toast.makeText(SecondActivity.this, "Account deleting process failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        mAuth.signOut();
    }
}
