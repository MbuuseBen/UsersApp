package com.example.usersapp;

import static android.content.ContentValues.TAG;
//import static com.nsiimbi.easypay.Request.EP_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersapp.Model.CartList;
import com.example.usersapp.Model.CartTotal;
import com.example.usersapp.Model.Users;
//import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
//import com.flutterwave.raveandroid.RavePayManager;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
//port okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//import com.hover.sdk.actions.HoverAction;
//import com.hover.sdk.api.Hover;
//import com.hover.sdk.api.HoverParameters;
//import com.hover.sdk.permissions.PermissionActivity;
//import com.nsiimbi.easypay.Request;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText, addressEditText, cityEditText,specialText;
    private TextView f_Name,lName,userphone,useraddress,useremail ,editDetailsBtn,viewTotal;
    private String emailAddress,first_Name,last_Name,userPhone,userAddress,userEmail;
    private String cartItems;
    private int productTotal;
    private String productRandomKey;

    private String address="address";

    private int totalAmount;
    private FirebaseAuth mAuth;
    private String cartTotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        cartTotal = getIntent().getStringExtra("TotalAmount");

        //int finalAmount = Integer.valueOf(cartTotal);
        mAuth = FirebaseAuth.getInstance();

        Button confirmOrderBtn = (Button) findViewById(R.id.place_order_btn);
        editDetailsBtn = findViewById(R.id.address_change);
        specialText = findViewById(R.id.special_txt);
       // viewSum(cartTotal);
//        CheckAddress();
        viewUserDetails();
        getOrderDetails();
        obtaindetail();
   //     getDetails();
        viewTotal = (TextView) findViewById(R.id.total_amt);
        //   getCartItems();
       // viewTotal.setText(cartTotal);

        totalAmount = getIntent().getIntExtra("TotalAmount",0);

        viewTotal.setText("Total :   UGX " + (new DecimalFormat("#,###.00")).format(Integer.valueOf(totalAmount)));

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
               // ConfirmOrder();
               // addTotaltoCart();
                    makePayment();


            }
        });



//        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
//        mToolbar.setTitle("Payments");
//        setSupportActionBar(mToolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Payments");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    private void addTotaltoCart() {
        final DatabaseReference preOrderRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("UserView").child(mAuth.getCurrentUser().getUid());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("total", cartTotal);

        preOrderRef.child("CartTotal")
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        //  Toast.makeText(CartActivity.this, "Total Added Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,ConfirmFinalOrderActivity.class);
                        intent.putExtra("TotalAmount", cartTotal);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void obtaindetail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
           // String name = user.getDisplayName();
             emailAddress = user.getEmail();
          //  Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
          //  boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
         //   String uid = user.getUid();
        }
    }

    @SuppressLint("SetTextI18n")
    private void viewSum(String cartTotal) {
//        DatabaseReference CartTotal = FirebaseDatabase.getInstance().getReference().child("Cart List");
//        CartTotal.child("UserView").child(mAuth.getCurrentUser().getUid()).child("CartTotal")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            CartTotal cart = dataSnapshot.getValue(CartTotal.class);
//
//                            viewTotal.setText("Total :   UGX " + (new DecimalFormat("#,###.00")).format(Integer.valueOf(cartTotal)));
//
//                            productTotal = cart.getTotal();
//                            //  Picasso.get().load(products.getImage()).into(productImage1);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                    }
//                });

        viewTotal.setText(String.valueOf(cartTotal));
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

    private void makePayment() {

        UUID uuid = UUID.randomUUID();

        new RaveUiManager(ConfirmFinalOrderActivity.this)
                .setAmount(Double.valueOf(totalAmount))
                .setEmail(emailAddress)
                .setCountry("UG")
                .setCurrency("UGX")
                .setfName(first_Name)
                .setlName(last_Name)
                .setNarration("Purchase of JanzyStore Goods")
                .setPublicKey("FLWPUBK_TEST-4fe17da3cf824a6c097c8c2fccc54899-X")
                .setEncryptionKey("FLWSECK_TEST6cbcb474d3f7")
                .setTxRef(uuid.toString())
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
               // .acceptMpesaPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .onStagingEnv(true)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "PAYMENT SUCCESSFUL " , Toast.LENGTH_LONG).show();
                ConfirmOrder();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED ", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void viewUserDetails() {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    dataSnapshot.getChildrenCount();

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

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap<String, Object> products = new HashMap<>();
                    products.putAll((HashMap) task.getResult().getValue());
                    AddOrderInfo(products);
                } else {
                    Toast.makeText(ConfirmFinalOrderActivity.this,"It dint work",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AddOrderInfo(HashMap products) {

        final String specialTxt = specialText.getText().toString();

        final String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());


      //  productRandomKey = saveCurrentDate + saveCurrentTime;
        UUID uuid = UUID.randomUUID();

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid));


        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",productTotal);
        ordersMap.put("firstname",first_Name);
        ordersMap.put("lastname",last_Name);
        ordersMap.put("phone",userPhone);
        ordersMap.put("address",userAddress);
        ordersMap.put("email",userEmail);
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("State", "confirmed");
        ordersMap.put("products",cartItems);
        ordersMap.put("orderid", String.valueOf(uuid));
        ordersMap.put("specialText", specialTxt);
        ordersMap.put("products", products);


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

                                    Intent intent =  new Intent(ConfirmFinalOrderActivity.this, FinishOrderActivity.class);
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