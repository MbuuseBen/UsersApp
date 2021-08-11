package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.usersapp.Model.Products;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class BookActivity extends AppCompatActivity {

    private SearchView SearchBtn;
    private EditText inputText;
    private RecyclerView searchList,pencils,calculators;
    private String SearchInput;
    private RecyclerView recyclerView,recyclerViewPencils,recyclerViewCalculators;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutPencils;
    RecyclerView.LayoutManager layoutCalculators;

    private String productID = "";

    private String categoryPencils="pencil", categoryCalculators="calculator";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        searchList = findViewById(R.id.search_list1);
        pencils = findViewById(R.id.category_pencils);
        calculators = findViewById(R.id.category_calculators);

        recyclerView = findViewById(R.id.search_list1);
        recyclerViewPencils = findViewById(R.id.category_pencils);
        recyclerViewCalculators = findViewById(R.id.category_calculators);


        productID = getIntent().getStringExtra("pid");

//        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

       layoutManager = new GridLayoutManager(this,2);
 //       layoutManager = new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false);
  //     layoutManager = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
 //       layoutManager = new GridLayoutManager(this,4,GridLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        layoutPencils = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPencils.setLayoutManager(layoutPencils);

        layoutCalculators = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCalculators.setLayoutManager(layoutCalculators);

//        recyclerView.setHasFixedSize(true);

        loadPencilstoRecyclerView();
        loadCalculatorstoRecyclerView();



        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Book");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);


//
//        mToolbar = (Toolbar) findViewById(R.id.topAppBar);
//        mToolbar.setTitle("Details");
//        setSupportActionBar(mToolbar);


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

    private void loadCalculatorstoRecyclerView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        Query query  =reference.orderByChild("category").equalTo(categoryCalculators);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterCalculators = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BookActivity.this, ProductDetailsActivity.class);
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
        calculators.setAdapter(adapterCalculators);
        adapterCalculators.startListening();

    }

    private void loadPencilstoRecyclerView() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        Query query  =reference.orderByChild("category").equalTo(categoryPencils);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

//        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
//                .setQuery(reference.orderByChild("pname").startAt(SearchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterPencils = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BookActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout1, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        pencils.setAdapter(adapterPencils);
        adapterPencils.startListening();
    }



    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput),Products.class).build();

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
                        Intent intent = new Intent(BookActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layoutgrid, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();

//        pencils.setAdapter(adapter);
//        adapter.startListening();



//        calculators.setAdapter(adapter);
//        adapter.startListening();


    }

}
