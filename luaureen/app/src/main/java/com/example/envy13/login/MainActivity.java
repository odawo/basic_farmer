package com.example.envy13.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    Button Login;
    Button userRegistration;
    TextView forgotPass;

    private FirebaseAuth firebaseAuth;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        userRegistration = findViewById(R.id.tvRegister);
        forgotPass = findViewById(R.id.forgotpassword);

        firebaseAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    loginUser();
                }
            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

//    NEW CHANGE ADDED
//    NEW CHANGE ADDED
//    NEW CHANGE ADDED
//    NEW CHANGE ADDED
    private void forgotPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.reset_email_popup, null);
        builder.setView(view);

        final EditText reset = view.findViewById(R.id.resetemail);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                use the email filled to send an email to it for password reset
                String email = reset.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "email field is empty!", Toast.LENGTH_SHORT).show();
                }

                final ProgressBar progressBar = new ProgressBar(MainActivity.this);
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Check your email for password reset instruction", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to complete request. please retry.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        progressBar.setVisibility(View.GONE);

                    }

                    }).addOnFailureListener(new OnFailureListener() { //incase the process fails
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                }

        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                exit dialog box and bac to login page
//                dialog.dismiss();
                finish();
            }
        });
//        creates and displays the dialog material
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        get current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }
    }

    private void loginUser() {
        
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                if the login task is successful, opens secondactivity else shows error
                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail: SUCCESS");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    finish();
                } else {
                    Log.w(TAG, "signInWithEmail: FAILED", task.getException());
                    Toast.makeText(MainActivity.this, "AUTHENTICATION ERROR. TRY AGAIN", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    private Boolean validate(){
        Boolean result = false;

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter all the Details!", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }

}
