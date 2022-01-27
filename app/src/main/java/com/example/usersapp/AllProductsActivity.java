 package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersapp.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

 public class AllProductsActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef,reference,def;
    private RecyclerView recyclerView;
    private SearchView inputText;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private String type = "", productDescription, productName,imageUrl;
   private int productPrice,productQty;
    private Button addToCartButton ,tapBtn;
    private TextView searchBtn;
    private String SearchInput;
    private boolean isMembersVisible= false;
    private int initialValue = 1,newQty=0;
    private String productID="";
     private String productRandomKey;
    private ServerValue add;
//    private String initialQty = "1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        reference = FirebaseDatabase.getInstance().getReference().child("Products");


        mAuth = FirebaseAuth.getInstance();

//        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All Products");
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

//        inputText = findViewById(R.id.app_bar_search);
//        SearchBtn = findViewById(R.id.search_iv_icon);
//
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


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

                    Intent intent = new Intent(AllProductsActivity.this, CartActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(AllProductsActivity.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AllProductsActivity.this, MainActivity.class);
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


        switch (item.getItemId()){

            case R.id.Cart:
                checkCart();
//                Intent intent = new Intent(MainActivity.this, CartActivity.class);
//                startActivity(intent);
                return true;

            case R.id.app_bar_search:
                Intent intent = new Intent(AllProductsActivity.this, NewSearchActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);

        }



    }

//    private void incrementCounter() {
//        def.runTransaction(new Transaction.Handler() {
//            @NonNull
//            @NotNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull @NotNull MutableData currentData) {
//                if (currentData.getValue()== null){
//                    currentData.setValue(1);
//                }else {
//                    currentData.setValue((Long) currentData.getValue()+1);
//                }
//                return Transaction.success(currentData);
//            }
//
//            @Override
//            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, boolean committed, @Nullable @org.jetbrains.annotations.Nullable DataSnapshot currentData) {
//
//
//            }
//        });
//
//    }
//




    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname"), Products.class)
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

                                    Intent intent = new Intent(AllProductsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }


                        });
//
                        holder.tapBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("UserView").child(mAuth.getCurrentUser().getUid());
                                productsRef.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
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

                                            cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("Products")
                                                    .child(productID).updateChildren(cartMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                cartListRef.child("Orders View").child(mAuth.getCurrentUser().getUid())
                                                                        .child("Products").child(productID)
                                                                        .updateChildren(cartMap)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(AllProductsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
//                                                                                    Intent intent = new Intent(AllProductsActivity.this, AllProductsActivity.class);
//                                                                                    startActivity(intent);
//                                                                                    finish();
                                                                                }
                                                                            }
                                                                        });
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

                                            cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("Products")
                                                    .child(productID).updateChildren(cartMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                cartListRef.child("Orders View").child(mAuth.getCurrentUser().getUid()).child(productRandomKey)
                                                                        .child("Products").child(productID)
                                                                        .updateChildren(cartMap)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(AllProductsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                            Intent intent = new Intent(AllProductsActivity.this, AllProductsActivity.class);
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//     private void checkCartStatus() {
//         DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("UserView").child(mAuth.getCurrentUser().getUid());
//         productsRef.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(DataSnapshot snapshot) {
//                 if (snapshot.exists()) {
//
//                     Toast.makeText(AllProductsActivity.this, "Adding To cart", Toast.LENGTH_SHORT).show();
//                 } else {
//                     Toast.makeText(AllProductsActivity.this, "Nothing to Show", Toast.LENGTH_SHORT).show();
//
//                 }
//             }
//
//             @Override
//             public void onCancelled( DatabaseError error) {
//
//             }
//         });
//
//
//     }
 }