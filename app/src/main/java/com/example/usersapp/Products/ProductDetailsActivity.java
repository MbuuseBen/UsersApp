package com.example.usersapp.Products;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.usersapp.CartActivity;
import com.example.usersapp.MainActivity;
import com.example.usersapp.Model.Products;
import com.example.usersapp.Search.NewSearchActivity;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    //private FloatingActionButton addToCartBtn;
    private ImageView productImage,productImage1,wishlistBtn;
    private ElegantNumberButton numberButton;
//    private TextView productPrice, productDescription, productName;
    private TextView productPrice1, productDescription1, productName1;
    private TextView sellerName,sellerEmail,sellerAddress;
    private TextView sellerMore;
    private String productID = "", state = "Normal",imageUrl;
    private int productPrice;
    private String productDescription;
    private String productName,sellerName1,itemID;
    private Button addToCartButton,migrateButton;
    private Toolbar mToolbar;
    private DatabaseReference ProductsRef;

    private ServerValue add;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView searchList;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        recyclerView = findViewById(R.id.search_list1);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        searchList = findViewById(R.id.search_list1);
        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById((R.id.number_btn));
        productImage1 = (ImageView) findViewById(R.id.product_image_details);
        productName1 = (TextView) findViewById(R.id.product_name_details);
        productDescription1 = (TextView) findViewById(R.id.product_description_details);
        productPrice1 = (TextView) findViewById(R.id.product_price_details);
    //    migrateButton = (Button) findViewById(R.id.migrate_btn);

        wishlistBtn = (ImageView)findViewById(R.id.wishlist);
        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWishList();
            }
        });


        sellerName = (TextView) findViewById(R.id.sellers_name);
        sellerEmail = (TextView) findViewById(R.id.sellers_email);
        sellerAddress = (TextView) findViewById(R.id.sellers_address);

        getProductDetails(productID);
        loadAllProductstoRecyclerview();

        viewProductDetails(productID);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

//        migrateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                migrateItem();
//            }
//        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          //      CheckOrderState();
                addingToCartList();

            }
        });

        sellerMore = (TextView) findViewById(R.id.sellers_more);
        sellerMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailsActivity.this, OtherSellerProductsActivity.class);
                intent.putExtra("sellerName", sellerName1);
                startActivity(intent);
                finish();
            }
        });

    }



    private void addToWishList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());


                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(mAuth.getCurrentUser().getUid());
                productsRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            // Toast.makeText(AllProductsActivity.this, "Adding To cart", Toast.LENGTH_SHORT).show();

                            final HashMap<String, Object> cartMap = new HashMap<>();
                            cartMap.put("pid", productID);
                            cartMap.put("pname", productName);
                            cartMap.put("price", productPrice);
                            cartMap.put("date", saveCurrentDate);
                            cartMap.put("time", saveCurrentTime);
                            cartMap.put("image",imageUrl);
                            cartMap.put("discount", "");

                            productsRef.child("products")
                                    .child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "Added to Wishlist ", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                        } else {
                            // Toast.makeText(AllProductsActivity.this, "Adding To cart", Toast.LENGTH_SHORT).show();

                            final HashMap<String, Object> cartMap = new HashMap<>();
                            cartMap.put("pid", productID);
                            cartMap.put("pname", productName);
                            cartMap.put("price", productPrice);
                            cartMap.put("date", saveCurrentDate);
                            cartMap.put("time", saveCurrentTime);
                            cartMap.put("image",imageUrl);
                            cartMap.put("discount", "");

                            productsRef.child("products")
                                    .child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();

                                        }

                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


    private void viewProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName1.setText(products.getPname());
                    productPrice1.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(products.getPrice())));
                    productDescription1.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage1);

                    sellerName.setText("Seller Name : " +products.getSellerName());
                    sellerEmail.setText("Email : " +products.getSellerEmail());
                    sellerAddress.setText("Address : " +products.getSellerAddress());

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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

                    Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                    startActivity(intent);
                    finish();


                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

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


        switch (item.getItemId()){

            case R.id.Cart:
                checkCart();

                return true;

            case R.id.app_bar_search:
                Intent intent = new Intent(ProductDetailsActivity.this, NewSearchActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);

        }


    }


    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();
    }


//    private void addingToCartList() {
//        String saveCurrentTime, saveCurrentDate;
//        Calendar callForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentDate.format(callForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentTime.format(callForDate.getTime());
//
//        UUID uuid = UUID.randomUUID();
//
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//
//        //final HashMap<String, object> cartMap = new HashMap<>();
//        final HashMap<String, Object> cartMap = new HashMap<>();
//        cartMap.put("pid", productID);
//        cartMap.put("pname", productName);
//        cartMap.put("price", productPrice);
//        cartMap.put("date", saveCurrentDate);
//        cartMap.put("time", saveCurrentTime);
//        cartMap.put("image",imageUrl);
//        cartMap.put("quantity",add.increment((Integer.parseInt(numberButton.getNumber()))));
//        cartMap.put("discount", "");
//        cartMap.put("sellerName",sellerName1);
//
//
//        cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("Products")
//                .child(productID).updateChildren(cartMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            cartListRef.child("Orders").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid))
//                                    .child("Products").child(productID)
//                                    .updateChildren(cartMap)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
////                                                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
////                                                startActivity(intent);
//                                            }
//                                         }
//                                    });
//                        }
//                    }
//                });
//    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //final HashMap<String, object> cartMap = new HashMap<>();
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName);
        cartMap.put("price", productPrice);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("image",imageUrl);
        cartMap.put("quantity",add.increment((Integer.parseInt(numberButton.getNumber()))));
        cartMap.put("discount", "");
        cartMap.put("sellerName",sellerName1);


        cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                           // finish();
//                            Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
//                            startActivity(intent);

                        }
                    }
                });
    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    itemID = products.getPid();
                    productName = products.getPname();
                    productPrice = products.getPrice();
                    productDescription = products.getDescription();
                    imageUrl = products.getImage();

                    sellerName1 = products.getSellerName();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    protected void loadAllProductstoRecyclerview() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

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
                        Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
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