package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.SearchView;
import android.widget.Toast;

import com.example.usersapp.Model.Produ;
import com.example.usersapp.Model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewSearchActivity extends AppCompatActivity {
    // creating variables for
    // our ui components.
    private RecyclerView searchview;
    //private DatabaseReference reference;
    private DatabaseReference reference;
    // variable for our adapter
    // class and array list
    private SearchItemAdapter adapter;
    private ArrayList<Produ> productsModalArrayList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);

        // initializing our variables.
        searchview = findViewById(R.id.search_list);

        // calling method to
        // build recycler view.
        buildRecyclerView();

        //getProducts();

        reference = FirebaseDatabase.getInstance().getReference().child("products");
        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Search for Products here ");
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Produ> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Produ item : productsModalArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getPname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        productsModalArrayList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    String pname = (String) ds.child("pname").getValue();
                    String pId = ds.child("pid").getValue().toString();

                   // String image = ds.child("pname").getValue().toString();
                    String imageUrl = (String) ds.child("image").getValue();
                    Long price = (Long) ds.child("price").getValue();
                    Produ product = new Produ(pId,String.valueOf(pname),String.valueOf(price),imageUrl);
                    productsModalArrayList.add(product);
                }

                // initializing our adapter class.
                adapter = new SearchItemAdapter(productsModalArrayList, NewSearchActivity.this);

                // adding layout manager to our recycler view.
                LinearLayoutManager manager = new LinearLayoutManager(NewSearchActivity.this);
                searchview.setHasFixedSize(true);

                // setting layout manager
                // to our recycler view.
                searchview.setLayoutManager(manager);

                // setting adapter to
                // our recycler view.
                searchview.setAdapter(adapter);
//                if ((snapshot.exists())) {
//                    Produ products = snapshot.getValue(Produ.class);
//                    //itemID = products.getPid();
//                   String productName = products.getPname();
//                    int productPrice = products.getPrice();
//                    //  String productDescription = products.getDescription();
//                    // String imageUrl = products.getImage();
//
//                    productsModalArrayList = new ArrayList<>();
//


//
//                    if(products!=null){
//
//                      //  productsModalArrayList.add(products);
//                        productsModalArrayList.add(new Produ(productName));
//                    }
//                    // below line is to add data to our array list.
//                    productsModalArrayList.add(new Products(productName));
            //    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // below line is to add data to our array list.
        //productsModalArrayList.add(new Products(productName));
//       productsModalArrayList.add(new Products("JAVA", "JAVA Self Paced Course"));
//        productsModalArrayList.add(new Products("C++", "C++ Self Paced Course"));
//        productsModalArrayList.add(new Products("Python", "Python Self Paced Course"));
//        productsModalArrayList.add(new Products("Fork CPP", "Fork CPP Self Paced Course"));

//        // initializing our adapter class.
//        adapter = new SearchItemAdapter(productsModalArrayList, NewSearchActivity.this);
//
//        // adding layout manager to our recycler view.
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        searchview.setHasFixedSize(true);
//
//        // setting layout manager
//        // to our recycler view.
//        searchview.setLayoutManager(manager);
//
//        // setting adapter to
//        // our recycler view.
//        searchview.setAdapter(adapter);
    }
}