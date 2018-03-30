package com.example.envy13.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class JielimisheActivity extends Activity {

    Button tomatoes, onions, potatoes, carrots;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jielimishe);

        tomatoes = findViewById(R.id.tomatoes);
        onions = findViewById(R.id.onions);
        potatoes = findViewById(R.id.potatoes);
        carrots = findViewById(R.id.carrots);

        tomatoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              creates the popup dialogs with the basic information
                final AlertDialog.Builder builder = new AlertDialog.Builder(JielimisheActivity.this);
                builder.setTitle("Tomatoes in Kenya");
//                the line below is getting information from the folders : res>values>strings from a string called tomato
                builder.setMessage(R.string.tomato);
                builder.setCancelable(true);
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss alert dialog
                        startActivity(new Intent(JielimisheActivity.this, JielimisheActivity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        onions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(JielimisheActivity.this);
                builder.setTitle("Onions in Kenya");
//                the line below is getting information from the folders : res>values>strings from a string called onion..
                builder.setMessage(R.string.onion);
                builder.setCancelable(true);
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dismiss alert dialog
                        startActivity(new Intent(JielimisheActivity.this, JielimisheActivity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        potatoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(JielimisheActivity.this);
                builder.setTitle("Potatoes in Kenya");
                builder.setMessage(R.string.potato);
                builder.setCancelable(true);
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss alert dialog
                        startActivity(new Intent(JielimisheActivity.this, JielimisheActivity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        carrots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(JielimisheActivity.this);
                builder.setTitle("Carrots in Kenya");
                builder.setMessage(R.string.carrot); //R.string.tomatoinfo
                builder.setCancelable(true);
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(JielimisheActivity.this, JielimisheActivity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}
