package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserRegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText firstnameInput,lastnameInput,phoneInput,emailInput,passwordInput,addressInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private TextView UserloginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        UserloginLink = (TextView)findViewById(R.id.login_link);

        registerBtn = (Button)findViewById(R.id.seller_register_btn);

        firstnameInput= findViewById(R.id.seller_f_name);
        lastnameInput= findViewById(R.id.seller_l_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();



        UserloginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }


        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String f_name = firstnameInput.getText().toString();
        String l_name = lastnameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();


        if(!f_name.equals("") && (!l_name.equals("")) && (!phone.equals("")) && (!email.equals("")) && (!password.equals(""))){

            loadingBar.setTitle(("Creating User Account"));
            loadingBar.setMessage("Please wait while we check your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                String uid = mAuth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",uid);
                                sellerMap.put("Phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("firstname",f_name);
                                sellerMap.put("lastname",l_name);


                                rootRef.child("Users").child(uid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                loadingBar.dismiss();
                                                Toast.makeText(UserRegisterActivity.this, "You are Registered Successfully , Please Login",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);

                                                startActivity(intent);

                                            }
                                        });

                            }
                        }
                    });


        }else {
            Toast.makeText(UserRegisterActivity.this, "Please Complete Registration",Toast.LENGTH_SHORT).show();
        }
    }
}