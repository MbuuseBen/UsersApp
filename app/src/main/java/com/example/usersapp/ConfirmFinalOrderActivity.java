package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText, addressEditText, cityEditText;
    private TextView f_Name,lName,userphone,useraddress,useremail ,editDetailsBtn;
    private String first_Name,last_Name,userPhone,userAddress,userEmail;


    private String totalAmount = "";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");


        mAuth = FirebaseAuth.getInstance();

        Button confirmOrderBtn = (Button) findViewById(R.id.place_order_btn);
        editDetailsBtn = findViewById(R.id.address_change);


        viewUserDetails();
        getOrderDetails();

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

//    private void Check() {
//        if(TextUtils.isEmpty(nameEditText.getText().toString())){
//            Toast.makeText(this, "Please Provide your Full Name ", Toast.LENGTH_SHORT).show();
//
//        }
//        else  if(TextUtils.isEmpty(phoneEditText.getText().toString())){
//            Toast.makeText(this, "Please Provide your phone Number ", Toast.LENGTH_SHORT).show();
//
//        }
//        else  if(TextUtils.isEmpty(addressEditText.getText().toString())){
//            Toast.makeText(this, "Please Provide your Address ", Toast.LENGTH_SHORT).show();
//
//        }
//        else  if(TextUtils.isEmpty(cityEditText.getText().toString())){
//            Toast.makeText(this, "Please Provide your City", Toast.LENGTH_SHORT).show();
//
//        }
//        else{
//            ConfirmOrder();
//        }
//    }

    private void ConfirmOrder() {

        final String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());


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
                                    Toast.makeText(ConfirmFinalOrderActivity.this, "your Final Order has been placed successfully", Toast.LENGTH_SHORT).show();

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