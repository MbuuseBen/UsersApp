package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usersapp.Model.Products;
import com.example.usersapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewSettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private TextView fName,lName,Phone, Email,Address,  closeTextBtn, BackHomeButton,editDetails;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_settings);

        mAuth = FirebaseAuth.getInstance();
        profileImageView = findViewById(R.id.profilePic);
        fName = findViewById(R.id.settings_first_name);
        lName= findViewById(R.id.settings_last_name);
        Phone = findViewById(R.id.settings_phone_number);
        Address = findViewById(R.id.settings_address);
        Email = findViewById(R.id.settings_email_address);
        editDetails = findViewById(R.id.edit_settings_btn);
        closeTextBtn= findViewById(R.id.close_settings_btn);
//        BackHomeButton = findViewById(R.id.home_page_btn);


        getUserInfo();


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }



    private void getUserInfo() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);

                    fName.setText("First Name : " +user.getFirstname());
                    lName.setText("Last Name : "+ user.getLastname());
                    Phone.setText("Phone : "+user.getPhone());
                    Email.setText("Email : "+user.getEmail());
                    Address.setText(user.getAddress());
                    Picasso.get().load(user.getImage()).into(profileImageView);


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}