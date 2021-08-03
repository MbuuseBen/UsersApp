package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersapp.Model.CartList;
import com.example.usersapp.Model.CartTotal;
import com.example.usersapp.Model.Products;
import com.example.usersapp.Model.Users;
import com.example.usersapp.Prevalent.Prevalent;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText, addressEditText, cityEditText;
    private TextView f_Name,lName,userphone,useraddress,useremail ,editDetailsBtn,viewTotal;
    private String first_Name,last_Name,userPhone,userAddress,userEmail;
    private String cartItems;

    private String totalAmount = "",address="addresss";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

     //   totalAmount = getIntent().getStringExtra("Total Price");


        mAuth = FirebaseAuth.getInstance();

        Button confirmOrderBtn = (Button) findViewById(R.id.place_order_btn);
        editDetailsBtn = findViewById(R.id.address_change);

        viewSum();
//        CheckAddress();
        viewUserDetails();
        getOrderDetails();
        viewTotal = (TextView) findViewById(R.id.total_price);
      //  getCartItems();

        f_Name = findViewById(R.id.first_name);
        lName = findViewById(R.id.last_name);
        userphone = findViewById(R.id.phone_number);
        useraddress = findViewById(R.id.address);
        useremail = findViewById(R.id.email_address);

        editDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });



        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrder();
            }
        });

//
//        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckAddress();
//            }
//        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    private void viewSum() {
        DatabaseReference CartTotal = FirebaseDatabase.getInstance().getReference().child("Cart List");
                CartTotal.child("UserView").child(mAuth.getCurrentUser().getUid()).child("CartTotal")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CartTotal cart = dataSnapshot.getValue(CartTotal.class);

                    viewTotal.setText("Total :   UGX " + (new DecimalFormat("#,###.00")).format(Integer.valueOf(cart.getTotal())));

                  //  Picasso.get().load(products.getImage()).into(productImage1);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void getOrderDetails() {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        productsRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        first_Name = user.getFirstname();
                        last_Name = user.getLastname();
                        userPhone = user.getPhone();
                        userAddress = user.getAddress();
                        userEmail = user.getEmail();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    private void getCartItems() {
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("UserView").child(mAuth.getCurrentUser().getUid()).child("Products");

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                CartList cart = snapshot.getValue(CartList.class);
                cartItems = cart.toString();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }




    private void viewUserDetails() {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                        String firstname = dataSnapshot.child("firstname").getValue().toString();
                        String lastname = dataSnapshot.child("lastname").getValue().toString();
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        f_Name.setText(firstname);
                        lName.setText(lastname);
                        userphone.setText(phone);
                        useraddress.setText(address);
                        useremail.setText(email);
                    }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void ConfirmOrder() {

        final String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());


        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(mAuth.getCurrentUser().getUid());


        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("firstname",first_Name);
        ordersMap.put("lastname",last_Name);
        ordersMap.put("phone",userPhone);
        ordersMap.put("address",userAddress);
        ordersMap.put("email",userEmail);
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("State", "not Shipped");
      //  ordersMap.put("Items",cartItems);


        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("UserView")
                        .child(mAuth.getCurrentUser().getUid())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Your Final Order has been placed successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent =  new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}