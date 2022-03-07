package com.example.usersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.usersapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class FeedbackActivity extends AppCompatActivity {
    private EditText Subject, Message;
    private Button submitBtn;
    private FirebaseAuth mAuth;
    private String first_Name,last_Name,userPhone,userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        mAuth = FirebaseAuth.getInstance();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        mToolbar.setTitle("FeedBack");
        setSupportActionBar(mToolbar);

        Subject = findViewById(R.id.user_feedback_subject);
        Message = findViewById(R.id.user_feedback_message);
        submitBtn = findViewById(R.id.user_submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });

        getUserDetails();

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void getUserDetails() {
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
                        userEmail = user.getEmail();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void submitFeedback() {
        final String subject = Subject.getText().toString();
        final String message = Message.getText().toString();

        final String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        UUID uuid = UUID.randomUUID();

        final DatabaseReference replyRef = FirebaseDatabase.getInstance().getReference().child("Feedback")
                .child(String.valueOf(uuid));

        HashMap<String, Object> feedbackMap = new HashMap<>();
        feedbackMap.put("firstname",first_Name);
        feedbackMap.put("lastname",last_Name);
        feedbackMap.put("phone",userPhone);
        feedbackMap.put("email",userEmail);
        feedbackMap.put("userId",mAuth.getCurrentUser().getUid());
        feedbackMap.put("date",saveCurrentDate);
        feedbackMap.put("time",saveCurrentTime);
        feedbackMap.put("queryid", String.valueOf(uuid));
        feedbackMap.put("subject",subject);
        feedbackMap.put("message",message);

        replyRef.updateChildren(feedbackMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Thanks for the feedback", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }
}