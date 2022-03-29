package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

import java.util.HashMap;

public class UserLoginActivity extends AppCompatActivity {

    private Button loginUserBtn;
    private AppCompatButton googleSigninBtn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private TextView UserRegisterLink,PasswordResetLink;
    private FirebaseAuth mAuth;
    boolean isEmailValid, isPasswordValid;
    private GoogleSignInClient mGoogleSignInClient;
    //private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1;


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
        PasswordResetLink = (TextView)findViewById(R.id.forgot_password_link);
        googleSigninBtn = (AppCompatButton) findViewById(R.id.signInWithGoogle);
        googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        createRequest();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In


// Build a GoogleSignInClient with the options specified by gso.
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
//        SignInButton signInButton = findViewById(R.id.signInWithGoogle);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);



        UserRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }


        });


        PasswordResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserLoginActivity.this, ResetPasswordActivity.class);
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

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("231068025606-2j4u9rh1i472n2agact5puvigntt9mfm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            String uid = mAuth.getCurrentUser().getUid();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", mAuth.getCurrentUser().getDisplayName());
                            hashMap.put("email", mAuth.getCurrentUser().getEmail());
                            hashMap.put("uid", uid);

                            rootRef.child("Users").child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UserLoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn(){

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    // [END signin]


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