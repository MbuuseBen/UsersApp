package com.example.usersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersapp.Model.Products;
import com.example.usersapp.Prevalent.Prevalent;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
//    private Button addToCartButton;

    private boolean isMembersVisible= false;
    NavigationView navigationView;

    private int initialValue = 1;
    private String productID ="";

    private String type = "";
    private  String productPrice, productDescription, productName,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productID = getIntent().getStringExtra("pid");

//        getProductDetails(productID);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        mAuth = FirebaseAuth.getInstance();


        Paper.init(this);

//        addToCartButton = (Button) findViewById(R.id.add_to_cart_btn1);
//
//        addToCartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addToCartList();
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Janzzy Store");
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!type.equals("Admin")){

                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                }

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       navigationView.getMenu().setGroupVisible(R.id.members_group,false);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


            userNameTextView.setText(mAuth.getCurrentUser().getEmail());
           Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);



        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }



    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName = products.getPname();
                    productPrice = products.getPrice();
                    productDescription = products.getDescription();
                    imageUrl = products.getImage();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void addToCartList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //final HashMap<String, object> cartMap = new HashMap<>();
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName);
        cartMap.put("price", productPrice);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("image",imageUrl);
        cartMap.put("quantity", initialValue);
        cartMap.put("discount", "");

        cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(mAuth.getCurrentUser().getUid())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "Added to Cart List", Toast.LENGTH_SHORT);
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(type.equals("Admin")){

                                    Intent intent = new Intent(MainActivity.this, MaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }



                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        navigationView.getMenu().setGroupVisible(R.id.members_group,false);
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if(!type.equals("Admin")){

                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }


        }
//        else if (id == R.id.nav_orders)
//        {
//
//        }


        else if (id == R.id.nav_search)
        {
            if(!type.equals("Admin")){
                Intent intent = new Intent(MainActivity.this, SearchProductActivity.class);
                startActivity(intent);

            }

        }
        else if (id == R.id.nav_categories)
        {

            if(!isMembersVisible){

            }
        }
        else if (id == R.id.nav_settings)
        {
            if(!type.equals("Admin")){

                Intent intent = new Intent(MainActivity.this, ViewSettingsActivity.class);
                startActivity(intent);
            }


        }
        else if (id == R.id.nav_logout)
        {

            Paper.book().destroy();

            FirebaseAuth.getInstance().signOut();


            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
