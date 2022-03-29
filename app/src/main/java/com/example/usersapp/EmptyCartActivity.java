package com.example.usersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EmptyCartActivity extends AppCompatActivity {
    private TextView home_Btn;
    private ImageView closeBth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cart);

        home_Btn = findViewById(R.id.home_text);
        closeBth = findViewById(R.id.close_icon);


        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyCartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        closeBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyCartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}