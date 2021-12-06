package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersapp.Model.Cart;
import com.example.usersapp.Model.Products;
import com.example.usersapp.Prevalent.Prevalent;
import com.example.usersapp.ViewHolder.CartViewHolder;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView,recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,checkOutBtn;
    private ImageView productImage1,deleteBtn;
    private TextView txtTotalAmount,txtmsg1;
    private int overTotalPrice = 0;

    private RecyclerView searchList;
    RecyclerView.LayoutManager layoutManager1;

    private String productID = "";

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productID = getIntent().getStringExtra("pid");

        txtTotalAmount = findViewById(R.id.total_price);

        deleteBtn = findViewById(R.id.deleteBtn1);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.cart_list1);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager1 = new GridLayoutManager(this,2);
        recyclerView1.setLayoutManager(layoutManager1);


        productImage1 = (ImageView) findViewById(R.id.cart_product_image);

        NextProcessBtn = findViewById(R.id.next_process_btn);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckAddress();



//                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        mToolbar.setTitle("Cart");
        setSupportActionBar(mToolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


       // addTotaltoCart();
        viewMoreProducts();

    }

    private void viewMoreProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").limitToFirst(4),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {


                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
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

    }


    private void addTotaltoCart() {
        final DatabaseReference preOrderRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("UserView").child(mAuth.getCurrentUser().getUid());

        //final HashMap<String, object> cartMap = new HashMap<>();
        final HashMap<String, Object> cartMap = new HashMap<>();
      //  cartMap.put("pid", productID);
        cartMap.put("total", overTotalPrice);
//        cartMap.put("price", prdctPrice);
//        cartMap.put("date", saveCurrentDate);
//        cartMap.put("time", saveCurrentTime);
//        cartMap.put("image",imageUrl);
//        cartMap.put("quantity",add.increment(1));
//        cartMap.put("discount", "");

        preOrderRef.child("CartTotal")
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                      //  Toast.makeText(CartActivity.this, "Total Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CheckAddress() {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef.child(mAuth.getCurrentUser().getUid()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
//                    viewUserDetails();
//                    ConfirmOrder();

                    addTotaltoCart();
                    Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                    intent.putExtra("TotalAmount", overTotalPrice);
                    startActivity(intent);
                }else {
                    Toast.makeText(CartActivity.this, "Address Details Required.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CartActivity.this, SettingsActivity.class);
                    startActivity(intent);
 //                   viewUserDetails();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }





    @Override
    protected void onStart() {
        super.onStart();
   //     CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions
                .Builder<Cart>()
                .setQuery(cartListRef.child("UserView")
                        .child(mAuth.getCurrentUser().getUid()).child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =  new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NotNull CartViewHolder holder, int position, @NotNull Cart model) {

                holder.txtProductQuantity.setText("Qty : " + model.getQuantity());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                int subTotal = (Integer.valueOf(model.getPrice()) * Integer.valueOf(model.getQuantity()));
                int oneTypeProductTPrice = (Integer.valueOf(model.getPrice()) * Integer.valueOf(model.getQuantity()));
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;

                holder.txtProductName.setText(model.getPname());
                holder.txtSubTotal.setText("Sub Total : UGX " + (new DecimalFormat("#,###.00")).format(Integer.valueOf(subTotal)));


                totalView();

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new  CharSequence[]{
                                "Edit",
                                "Remove"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    Intent intent= new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }
                                if (i==1){
                                    cartListRef.child("UserView")
                                            .child(mAuth.getCurrentUser().getUid())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Item removed Successfully.",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this,CartActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });

                                    cartListRef.child("Admin View")
                                            .child(mAuth.getCurrentUser().getUid())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Please Add Items To Cart.",Toast.LENGTH_SHORT).show();
//                                                        Intent intent = new Intent(CartActivity.this,CartActivity.class);
//                                                        startActivity(intent);
//                                                        finish();
                                                    }
                                                }
                                            });

                                }
                            }
                        });
                        builder.show();

                    }
                });

            }


            @NotNull
            @Override
            public CartViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout1,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;


            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

//totalView();

    }

    private void totalView() {
               txtTotalAmount.setText("Total :   UGX " + (new DecimalFormat("#,###.00")).format(Integer.valueOf(overTotalPrice)));


    }




//    private void CheckOrderState() {
//        DatabaseReference ordersRef;
//        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
//                .child(mAuth.getCurrentUser().getUid());
//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    String shippingState = snapshot.child("State").getValue().toString();
//                    String userName = snapshot.child("firstname").getValue().toString();
//
//                    if (shippingState.equals("shipped")){
//                        txtTotalAmount.setText("Dear " + userName+ "\n Your Order has been Shipped successfully.");
//                        recyclerView.setVisibility(View.GONE);
//                        txtmsg1.setVisibility(View.VISIBLE);
//                        txtmsg1.setText("Congratulations,Your Order was shipped successfully,Soon To be verified");
//                        NextProcessBtn.setVisibility(View.GONE);
//
//                        Toast.makeText(CartActivity.this, "You can Purchase more Products",Toast.LENGTH_SHORT);
//
//
//                    }else  if(shippingState.equals("not shipped")){
//
//                        txtTotalAmount.setText("Shipping State = Not Shipped");
//                        recyclerView.setVisibility(View.GONE);
//                        txtmsg1.setVisibility(View.VISIBLE);
//                        NextProcessBtn.setVisibility(View.GONE);
//
//                        Toast.makeText(CartActivity.this, "You can Purchase more Products",Toast.LENGTH_SHORT);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//
//    }
}