package com.example.usersapp.Categories;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.usersapp.MainActivity;
import com.example.usersapp.R;

public class Productcategories extends AppCompatActivity {
    private ImageView viewCalculators,viewPencils,viewNotebooks,viewTextBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productcategories);



        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Product Categories");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        viewPencils = findViewById(R.id.product_image_calculator);
        viewPencils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, Categorycalculators.class);
                startActivity(intent);
            }
        });

        viewCalculators = findViewById(R.id.product_image_pencil);
        viewCalculators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryPencils.class);
                startActivity(intent);
            }
        });

        viewNotebooks = findViewById(R.id.product_image_notebooks);
        viewNotebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryNotebooks.class);
                startActivity(intent);
            }
        });

        viewTextBooks = findViewById(R.id.product_image_glue);
        viewTextBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryTextbooks.class);
                startActivity(intent);
            }
        });



    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        return super.onPrepareOptionsMenu(menu);
    }
}