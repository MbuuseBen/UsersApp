package com.example.usersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishOrderActivity extends AppCompatActivity {
    private Button orders_Btn;
    private TextView home_Btn;
    private ImageView closeBth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        orders_Btn = findViewById(R.id.orders_Btn);
        home_Btn = findViewById(R.id.home_text);
        closeBth = findViewById(R.id.close_icon);

        orders_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishOrderActivity.this,OrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishOrderActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        closeBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishOrderActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}