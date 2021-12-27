package com.example.usersapp.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CategoryPushPins extends AppCompatActivity {

    private SearchView SearchBtn;
    private RecyclerView searchList,pushpins;
    private RecyclerView recyclerView,recyclerViewPushpins;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutPushpins;
    private FirebaseAuth mAuth;


    private String productID = "";

    private String categoryPushpins="pushpin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_pushpins);
        mAuth = FirebaseAuth.getInstance();

        searchList = findViewById(R.id.search_list1);
        pushpins = findViewById(R.id.category_pushpin);


        recyclerView = findViewById(R.id.search_list1);
        recyclerViewPushpins = findViewById(R.id.category_pushpin);



        productID = getIntent().getStringExtra("pid");

        layoutPushpins = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewPushpins.setLayoutManager(layoutPushpins);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);


        loadTextbookstoRecyclerView();
        loadAllProductstoRecyclerview();


        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("PushPins");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


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

                    Intent intent = new Intent(CategoryPushPins.this, CartActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(CategoryPushPins.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryPushPins.this, MainActivity.class);
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



    private void loadTextbookstoRecyclerView() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        Query query  =reference.orderByChild("category").equalTo(categoryPushpins);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterPushPins = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CategoryPushPins.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout2, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        pushpins.setAdapter(adapterPushPins);
        adapterPushPins.startListening();
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
                        Intent intent = new Intent(CategoryPushPins.this, ProductDetailsActivity.class);
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