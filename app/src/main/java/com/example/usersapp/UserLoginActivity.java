package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class UserLoginActivity extends AppCompatActivity {

    private Button loginUserBtn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private TextView UserRegisterLink;
    private FirebaseAuth mAuth;
    boolean isEmailValid, isPasswordValid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mAuth = FirebaseAuth.getInstance();

        UserRegisterLink = (TextView)findViewById(R.id.register_link);
        loginUserBtn = (Button)findViewById(R.id.user_login_btn);
        emailInput = findViewById(R.id.user_login_email);
        passwordInput = findViewById(R.id.user_login_password);
        loadingBar = new ProgressDialog(this);


        UserRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }


        });


        loginUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetValidation();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void SetValidation() {
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

        // Check for a valid password.
        if (passwordInput.getText().toString().isEmpty()) {
            passwordInput.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (passwordInput.getText().length() < 10) {
            passwordInput.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid) {
            loginSeller();

        }

    }


    private void loginSeller() {

        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if(!email.equals("") && (!password.equals(""))){
            loadingBar.setTitle(("Logging In"));
            loadingBar.setMessage("Please wait while we verify your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                loadingBar.dismiss();
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();



                            }
                        }
                    });




        }else {
            Toast.makeText(UserLoginActivity.this, "Please Complete The Login Form", Toast.LENGTH_SHORT).show();
        }

    }
}