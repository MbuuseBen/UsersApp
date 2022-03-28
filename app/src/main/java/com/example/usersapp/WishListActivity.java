package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.usersapp.Model.Cart;
import com.example.usersapp.Products.ProductDetailsActivity;
import com.example.usersapp.ViewHolder.CartViewHolder;
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

public class WishListActivity extends AppCompatActivity {
    private RecyclerView recyclerView,recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        mToolbar.setTitle("Wish List");
        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.cart_list1);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(mAuth.getCurrentUser().getUid());
        productsRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("WishList");
                    FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions
                            .Builder<Cart>()
                            .setQuery(cartListRef.child(mAuth.getCurrentUser().getUid()).child("products"), Cart.class)
                            .build();

                    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                            = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NotNull CartViewHolder holder, int position, @NotNull Cart model) {

                            holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                            Picasso.get().load(model.getImage()).into(holder.imageView);
                            holder.txtProductName.setText(model.getPname());
                            holder.txtSellerName.setText("Seller : " +model.getSellerName());

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }


                            });

                            holder.deleteBtn1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CharSequence options[] = new CharSequence[]{
                                            "Remove"

                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(WishListActivity.this);
                                    builder.setTitle("Options:");

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0) {
                                                cartListRef.child(mAuth.getCurrentUser().getUid())
                                                        .child("products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(WishListActivity.this, "Item removed Successfully from Wishlist.", Toast.LENGTH_SHORT).show();

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
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout3, parent, false);
                            CartViewHolder holder = new CartViewHolder(view);
                            return holder;


                        }
                    };

                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

                } else {
                    Toast.makeText(WishListActivity.this, "Nothing to show Please add some items to your wishlist.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WishListActivity.this, MainActivity.class);
                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}