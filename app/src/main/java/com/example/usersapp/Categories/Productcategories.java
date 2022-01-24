package com.example.usersapp.Categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usersapp.CartActivity;
import com.example.usersapp.MainActivity;
import com.example.usersapp.Model.Products;
import com.example.usersapp.ProductDetailsActivity;
import com.example.usersapp.R;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Productcategories extends AppCompatActivity {
    private ImageView viewCalculators,viewPencils,viewNotebooks,viewTextBooks,viewScissors;
    private ImageView viewClips,viewPushpins,viewStaplers,viewRulers,viewPunch,viewTape,viewCutters;
    private RecyclerView searchList,recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productcategories);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Product Categories");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        searchList = findViewById(R.id.search_list1);
        loadAllProductstoRecyclerview();
        recyclerView = findViewById(R.id.search_list1);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        viewPencils = findViewById(R.id.product_image_calculator);
        viewPencils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryPencils.class);
                startActivity(intent);
            }
        });

        viewCalculators = findViewById(R.id.product_image_pencil);
        viewCalculators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, Categorycalculators.class);
                startActivity(intent);
            }
        });

        viewScissors = findViewById(R.id.product_image_scissors);
        viewScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategotyScissors.class);
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

        viewClips = findViewById(R.id.product_image_clips);
        viewClips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryClips.class);
                startActivity(intent);
            }
        });

        viewPushpins = findViewById(R.id.product_image_pins);
        viewPushpins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryPushPins.class);
                startActivity(intent);
            }
        });

        viewStaplers = findViewById(R.id.product_image_stapler);
        viewStaplers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryStapler.class);
                startActivity(intent);
            }
        });

        viewStaplers = findViewById(R.id.product_image_stapler);
        viewStaplers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryStapler.class);
                startActivity(intent);
            }
        });

        viewRulers = findViewById(R.id.product_image_rulers);
        viewRulers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryRulers.class);
                startActivity(intent);
            }
        });

        viewPunch = findViewById(R.id.product_image_punch);
        viewPunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryPunch.class);
                startActivity(intent);
            }
        });

        viewTape = findViewById(R.id.product_image_tape);
        viewTape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryTape.class);
                startActivity(intent);
            }
        });

        viewCutters = findViewById(R.id.product_image_cutter);
        viewCutters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Productcategories.this, CategoryCutters.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void checkCart() {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("UserView");
        productsRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Intent intent = new Intent(Productcategories.this, CartActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(Productcategories.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Productcategories.this, MainActivity.class);
                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

//        navigationView.getMenu().setGroupVisible(R.id.members_group,false);
//        int id = item.getItemId();
//
////        if (id == R.id.action_settings)
////        {
////            return true;
////        }

        switch (item.getItemId()){

            case R.id.Cart:
                checkCart();
//                Intent intent = new Intent(MainActivity.this, CartActivity.class);
//                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }


    protected void loadAllProductstoRecyclerview() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {


                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Productcategories.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layouthorizontal1, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();



    }
}