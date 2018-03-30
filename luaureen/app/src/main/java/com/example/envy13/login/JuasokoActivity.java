package com.example.envy13.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class JuasokoActivity extends AppCompatActivity {
    
    Button postProduct, viewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juasoko);
        
        postProduct = findViewById(R.id.postproduct);
        viewProduct = findViewById(R.id.viewProducts);

        postProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JuasokoActivity.this, PostProduct.class));
            }
        });

        viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JuasokoActivity.this, ViewProduct.class));
            }
        });
    }
}
