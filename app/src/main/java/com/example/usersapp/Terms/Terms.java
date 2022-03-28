package com.example.usersapp.Terms;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.usersapp.R;

public class Terms extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        web =(WebView)findViewById(R.id.webView);
        web.loadUrl("file:///android_asset/terms.html");


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
        mToolbar.setTitle("Terms n Conditions");
        setSupportActionBar(mToolbar);



        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}