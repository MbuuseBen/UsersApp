package com.example.usersapp.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usersapp.Products.AllProductsActivity;
import com.example.usersapp.CartActivity;
import com.example.usersapp.MainActivity;
import com.example.usersapp.Model.Products;
import com.example.usersapp.NewSearchActivity;
import com.example.usersapp.Products.ProductDetailsActivity;
import com.example.usersapp.R;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CategoryRulers extends AppCompatActivity {

    private SearchView SearchBtn;
    private RecyclerView searchList,rulers;
    private RecyclerView recyclerView,recyclerViewRulers;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutRulers;
    private FirebaseAuth mAuth;
    private TextView seeAllProducts;

    private String productID = "";

    private String categoryRulers="ruler";

    private String productRandomKey;
    private ServerValue add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_rulers);
        mAuth = FirebaseAuth.getInstance();

        searchList = findViewById(R.id.search_list1);
        rulers = findViewById(R.id.category_rulers);


        recyclerView = findViewById(R.id.search_list1);
        recyclerViewRulers = findViewById(R.id.category_rulers);



        productID = getIntent().getStringExtra("pid");

        layoutRulers = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewRulers.setLayoutManager(layoutRulers);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);


        loadRulerstoRecyclerView();
        loadAllProductstoRecyclerview();


        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Rulers");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        seeAllProducts = findViewById(R.id.see_all_products);
        seeAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryRulers.this, AllProductsActivity.class);
//                intent.putExtra("pid", model.getPid());
                startActivity(intent);
            }
        });
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

                    Intent intent = new Intent(CategoryRulers.this, CartActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(CategoryRulers.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryRulers.this, MainActivity.class);
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
            case R.id.app_bar_search:
                Intent intent = new Intent(CategoryRulers.this, NewSearchActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);

        }


    }



    private void loadRulerstoRecyclerView() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        Query query  =reference.orderByChild("category").equalTo(categoryRulers);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterRulers = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CategoryRulers.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
                holder.tapBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("UserView").child(mAuth.getCurrentUser().getUid());
                        productsRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Toast.makeText(AllProductsActivity.this, "Adding To cart", Toast.LENGTH_SHORT).show();

                                    String productID = model.getPid();
                                    String prdctName = model.getPname();
                                    String sellerName= model.getSellerName();
                                    int prdctPrice = model.getPrice();
                                    //  String initQty ="1";
                                    String imageUrl = model.getImage();

                                    String saveCurrentTime, saveCurrentDate;
                                    Calendar callForDate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                    saveCurrentDate = currentDate.format(callForDate.getTime());

                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                    saveCurrentTime = currentTime.format(callForDate.getTime());


                                    //  productRandomKey = saveCurrentDate + saveCurrentTime;
                                    // String newQty = productQty;

                                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                    //final HashMap<String, object> cartMap = new HashMap<>();
                                    final HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("pid", productID);
                                    cartMap.put("pname", prdctName);
                                    cartMap.put("price", prdctPrice);
                                    cartMap.put("date", saveCurrentDate);
                                    cartMap.put("time", saveCurrentTime);
                                    cartMap.put("image",imageUrl);
                                    cartMap.put("sellerName",sellerName);
                                    cartMap.put("quantity",add.increment(1));
                                    cartMap.put("discount", "");

                                    cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("products")
                                            .child(productID).updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                                            Toast.makeText(CategoryRulers.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
//                                                                                    Intent intent = new Intent(AllProductsActivity.this, AllProductsActivity.class);
//                                                                                    startActivity(intent);
//
                                                    }
                                                }
                                            });
//                                            Intent intent = new Intent(AllProductsActivity.this, AllProductsActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                } else {

                                    String productID = model.getPid();
                                    String prdctName = model.getPname();
                                    int prdctPrice = model.getPrice();
                                    //  String initQty ="1";
                                    String sellerName= model.getSellerName();
                                    String imageUrl = model.getImage();

                                    String saveCurrentTime, saveCurrentDate;
                                    Calendar callForDate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                    saveCurrentDate = currentDate.format(callForDate.getTime());

                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                    saveCurrentTime = currentTime.format(callForDate.getTime());


                                    productRandomKey = saveCurrentDate + saveCurrentTime;
                                    // String newQty = productQty;

                                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                    //final HashMap<String, object> cartMap = new HashMap<>();
                                    final HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("pid", productID);
                                    cartMap.put("pname", prdctName);
                                    cartMap.put("price", prdctPrice);
                                    cartMap.put("date", saveCurrentDate);
                                    cartMap.put("time", saveCurrentTime);
                                    cartMap.put("image",imageUrl);
                                    cartMap.put("sellerName",sellerName);
                                    cartMap.put("quantity",add.increment(1));
                                    cartMap.put("discount", "");

                                    cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("products")
                                            .child(productID).updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                                            Toast.makeText(CategoryRulers.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
//                                                                                    Intent intent = new Intent(AllProductsActivity.this, AllProductsActivity.class);
//                                                                                    startActivity(intent);
//
                                                    }
                                                }
                                            });
                                    Intent intent = new Intent(CategoryRulers.this, AllProductsActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //  Toast.makeText(AllProductsActivity.this, "Nothing to Show", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout2, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        rulers.setAdapter(adapterRulers);
        adapterRulers.startListening();
    }



    protected void loadAllProductstoRecyclerview() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").limitToFirst(8),Products.class).build();

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
                        Intent intent = new Intent(CategoryRulers.this, ProductDetailsActivity.class);
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