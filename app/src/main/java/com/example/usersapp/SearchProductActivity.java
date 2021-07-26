package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.usersapp.Model.Products;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SearchProductActivity extends AppCompatActivity {

//    private ImageView SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

//        inputText = findViewById(R.id.search_product_name);
   //     SearchBtn = findViewById(R.id.search);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));


        mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        mToolbar.setTitle("Search");
        setSupportActionBar(mToolbar);

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
//

//        SearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SearchInput = inputText.getText().toString();
//                onStart();
//            }
//        });

//        topAppBar.setOnMenuItemClickListener{
//             menuItem ->
//                     when (menuItem.itemId){
//                 R.id.search ->{
//                    SearchInput = inputText.getText().toString();
//                    true
//                }
//            }
 //       }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.top_app_bar1,menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default



        return  true;
    }

//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//       switch (item.getItemId()){
//          case  R.id.homeAsUp:
//              NavUtils.navigateUpFromSameTask(this);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.app_bar_search)
        {
            SearchInput = inputText.getText().toString();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        SearchProductActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = UGX " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}