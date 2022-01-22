package com.example.usersapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Privacy extends AppCompatActivity {
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        web =(WebView)findViewById(R.id.webView);
        web.loadUrl("file:///android_asset/privacy.html");


//        Button privacyPolicyButton = (Button) this.findViewById(R.id.privacyPolicyButton);
//        privacyPolicyButton.setTextSize(18);
//        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getApplicationContext(),
//                        Privacy.class);
//                startActivity(intent);
//            }
//        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        mToolbar.setTitle("Privacy Policy");
        setSupportActionBar(mToolbar);


        // Get a support ActionBar corresponding to this toolbar


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}