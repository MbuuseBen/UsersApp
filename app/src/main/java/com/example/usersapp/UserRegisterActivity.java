package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;

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
                SetValidation();

            }
        });
    }


    public void SetValidation() {
        // Check for a valid name.
        if (firstnameInput.getText().toString().isEmpty()) {
            firstnameInput.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }

        if (lastnameInput.getText().toString().isEmpty()) {
            lastnameInput.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }

        // Check for a valid email address.
        if (emailInput.getText().toString().isEmpty()) {
            emailInput.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()) {
            emailInput.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid phone number.
        if (phoneInput.getText().toString().isEmpty()) {
            phoneInput.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else  {
            isPhoneValid = true;
        }

        // Check for a valid password.
        if (passwordInput.getText().toString().isEmpty()) {
            passwordInput.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (passwordInput.getText().length() < 6) {
            passwordInput.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid) {
            registerUser();
           // Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
        }

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

                           // Toast.makeText(UserRegisterActivity.this, "Error Signing you up ", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(UserRegisterActivity.this, "Authentication failed. Email already exists",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserRegisterActivity.this, UserRegisterActivity.class);

                                startActivity(intent);

                                finish();
                            } else {
                                //Do something here
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
                                                Toast.makeText(UserRegisterActivity.this, "Registered Successfully",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);

                                                startActivity(intent);

                                                finish();

                                            }
                                        });

                            }
                        }
                    });
//                            if(task.isSuccessful()){
//
//                            }
//                        }
//                    });
//
//
//        }else {
//            Toast.makeText(UserRegisterActivity.this, "Please Complete Registration",Toast.LENGTH_SHORT).show();
       }
    }
}