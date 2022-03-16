package com.example.usersapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.usersapp.Categories.CategoryClips;
import com.example.usersapp.Categories.CategoryNotebooks;
import com.example.usersapp.Categories.CategoryPushPins;
import com.example.usersapp.Categories.CategorySets;
import com.example.usersapp.Categories.CategoryStapler;
import com.example.usersapp.Categories.CategoryTextbooks;
import com.example.usersapp.Categories.CategoryPencils;
import com.example.usersapp.Categories.Categorycalculators;
import com.example.usersapp.Categories.Productcategories;
import com.example.usersapp.Model.Products;
import com.example.usersapp.Model.Users;
import com.example.usersapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView searchList,pencils,calculators,notebooks;
    private FirebaseAuth mAuth;
    private boolean isMembersVisible= false;
    NavigationView navigationView;
    private int initialValue = 1;
    private String productID ="";
    private String type = "";
    private  String productPrice, productDescription, productName,imageUrl;
    private RecyclerView recyclerView,recyclerViewPencils,recyclerViewCalculators,recyclerViewNotebooks,recyclerViewSets,recyclerViewTextbooks;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutPencils;
    RecyclerView.LayoutManager layoutNotebooks;
    RecyclerView.LayoutManager layoutSets;
    RecyclerView.LayoutManager layoutCalculators;
    private ServerValue add;

    private Toolbar topBar;
   private  AppBarLayout topBar1;

    private ImageView viewCalculators,viewPencils,viewNotebooks,viewTextbooks,viewSets;
    private ImageView viewClips,viewPushpins,viewStaplers;

    private TextView seeAllProducts,seeAllCategories;
    private  TextView seeAllPencils,seeAllCalculators,seeAllNotebooks;


    private String categoryPencils="pencil", categoryCalculators="calculator",categoryNotebooks="notebook";

    SliderView sliderViewHome;
    int[] images = {
            R.drawable.z,
            R.drawable.x,
            R.drawable.y};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderViewHome = findViewById(R.id.imageSliderHome);

        SliderAdapterHome sliderAdapterHome = new SliderAdapterHome(images);
        sliderViewHome.setSliderAdapter(sliderAdapterHome);
        sliderViewHome.setIndicatorAnimation(IndicatorAnimationType.DROP);
        sliderViewHome.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderViewHome.startAutoCycle();


        productID = getIntent().getStringExtra("pid");
//        getProductDetails(productID);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("products");
        mAuth = FirebaseAuth.getInstance();
       // Hover.initialize(this);
        //    profileImageView = (ImageView) findViewById(R.id.user_profile_image);

        Paper.init(this);

        searchList = findViewById(R.id.search_list1);
        pencils = findViewById(R.id.category_pencils);
        calculators = findViewById(R.id.category_calculators);
        notebooks = findViewById(R.id.category_notebook);

        recyclerView = findViewById(R.id.search_list1);
        recyclerViewPencils = findViewById(R.id.category_pencils);
        recyclerViewCalculators = findViewById(R.id.category_calculators);
        recyclerViewNotebooks = findViewById(R.id.category_notebook);
       // recyclerViewSets = findViewById(R.id.category_sets);

        layoutManager = new GridLayoutManager(this,2);
        //       layoutManager = new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false);
        //     layoutManager = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
        //       layoutManager = new GridLayoutManager(this,4,GridLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        layoutPencils = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPencils.setLayoutManager(layoutPencils);

        layoutCalculators = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCalculators.setLayoutManager(layoutCalculators);

        layoutNotebooks = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewNotebooks.setLayoutManager(layoutNotebooks);

        loadPencilstoRecyclerView();
        loadCalculatorstoRecyclerView();
        loadNotebookstoRecyclerView();


        seeAllCategories = findViewById(R.id.see_all_categories);
        seeAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Productcategories.class);
                startActivity(intent);
            }
        });


        seeAllProducts = findViewById(R.id.see_all_products);
        seeAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllProductsActivity.class);
//                intent.putExtra("pid", model.getPid());
                startActivity(intent);
            }
        });

        seeAllPencils = findViewById(R.id.see_all_pencils);
        seeAllPencils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryPencils.class);
                startActivity(intent);
            }
        });

        seeAllCalculators = findViewById(R.id.see_all_calculators);
        seeAllCalculators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Categorycalculators.class);
                startActivity(intent);
            }
        });

        seeAllNotebooks = findViewById(R.id.see_all_notebooks);
        seeAllNotebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryNotebooks.class);
                startActivity(intent);
            }
        });


        viewPencils = findViewById(R.id.product_image_calculator);
        viewPencils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Categorycalculators.class);
                startActivity(intent);
            }
        });

        viewCalculators = findViewById(R.id.product_image_pencil);
        viewCalculators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryPencils.class);
                startActivity(intent);
            }
        });

        viewNotebooks = findViewById(R.id.product_image_notebooks);
        viewNotebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryNotebooks.class);
                startActivity(intent);
            }
        });

        viewTextbooks = findViewById(R.id.product_image_glue);
        viewTextbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryTextbooks.class);
                startActivity(intent);
            }
        });

        viewClips = findViewById(R.id.product_image_clips);
        viewClips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryClips.class);
                startActivity(intent);
            }
        });

        viewPushpins = findViewById(R.id.product_image_pins);
        viewPushpins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryPushPins.class);
                startActivity(intent);
            }
        });

        viewStaplers = findViewById(R.id.product_image_stapler);
        viewStaplers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryStapler.class);
                startActivity(intent);
            }
        });

        viewSets = findViewById(R.id.product_image_pens);
        viewSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategorySets.class);
                startActivity(intent);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("MASS");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               checkCart();
               
//                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
//                    startActivity(intent);


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    //    getUserInfo();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
  //     navigationView.getMenu().setGroupVisible(R.id.members_group,false);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        Uri photourl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        Picasso.get().load(String.valueOf(photourl)).fit().into(profileImageView);

        DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userInfoRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                   // String username = users.getFirstname();
                    userNameTextView.setText(users.getFirstname() + " " + users.getLastname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userNameTextView.setText(mAuth.getCurrentUser().getEmail());
         //   userNameTextView.setText(mAuth.getCurrentUser().getEmail());

      //  Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(profileImageView);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }



        private void checkCart() {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("UserView");
        productsRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(MainActivity.this, "Please add some items to your cart.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    private void loadCalculatorstoRecyclerView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        Query query  =reference.orderByChild("category").equalTo(categoryCalculators).limitToFirst(10);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterCalculators = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout3, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        calculators.setAdapter(adapterCalculators);
        adapterCalculators.startListening();

    }

    private void loadNotebookstoRecyclerView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        Query query  =reference.orderByChild("category").equalTo(categoryNotebooks).limitToFirst(10);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterCalculators = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layouthorizontal1, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        notebooks.setAdapter(adapterCalculators);
        adapterCalculators.startListening();

    }


    private void loadPencilstoRecyclerView() {
        UUID uuid = UUID.randomUUID();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        Query query  =reference.orderByChild("category").equalTo(categoryPencils).limitToFirst(10);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapterPencils = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });

                holder.tapBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String productID = model.getPid();
                        String prdctName = model.getPname();
                        int prdctPrice = model.getPrice();
                      //  String initQty = "1";
                        String imageUrl = model.getImage();
                        String sellerName1 = model.getSellerName();

                        String saveCurrentTime, saveCurrentDate;
                        Calendar callForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        saveCurrentDate = currentDate.format(callForDate.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentDate.format(callForDate.getTime());

                        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                        //final HashMap<String, object> cartMap = new HashMap<>();
                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("pid", productID);
                        cartMap.put("pname", prdctName);
                        cartMap.put("price", prdctPrice);
                        cartMap.put("date", saveCurrentDate);
                        cartMap.put("time", saveCurrentTime);
                        cartMap.put("image",imageUrl);
                        cartMap.put("quantity",add.increment(1));
                        cartMap.put("discount", "");
                        cartMap.put("sellerName",sellerName1);

                        cartListRef.child("UserView").child(mAuth.getCurrentUser().getUid()).child("products")
                                .child(productID).updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            cartListRef.child("Orders View").child(mAuth.getCurrentUser().getUid())
                                                    .child("products").child(productID)
                                                    .updateChildren(cartMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(MainActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();

//                                                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
//                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });


            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout1, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        pencils.setAdapter(adapterPencils);
        adapterPencils.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").limitToFirst(4),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>
                adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {


                holder.txtProductName.setText(model.getPname());
//                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(model.getPrice())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layoutgrid, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        switch (item.getItemId()){

            case R.id.Cart:
                checkCart();
//                Intent intent = new Intent(MainActivity.this, CartActivity.class);
//                startActivity(intent);
                return true;

            case R.id.app_bar_search:
                Intent intent = new Intent(MainActivity.this, NewSearchActivity.class);
                startActivity(intent);
//
//            case R.id.wishlist:
//                Intent intent1 = new Intent(MainActivity.this, WishListActivity.class);
//                startActivity(intent1);

            default:
                return super.onOptionsItemSelected(item);

        }


    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

            if (id == R.id.nav_orders)
        {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(intent);
        }


        else if (id == R.id.nav_wishlist)
        {

                Intent intent = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(intent);


        }
            else if (id == R.id.nav_privacy)
            {

                Intent intent = new Intent(MainActivity.this, Privacy.class);
                startActivity(intent);


            }

            else if (id == R.id.nav_terms)
            {

                Intent intent = new Intent(MainActivity.this, Terms.class);
                startActivity(intent);


            }

            else if (id == R.id.nav_feedback)
            {

                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);


            }

            else if (id == R.id.nav_reset)
            {

                Intent intent = new Intent(MainActivity.this, ResetPasswordInActivity.class);
                startActivity(intent);


            }

            else if (id == R.id.nav_share)
            {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubText = "JanzyStore - The One Stop Stationery Shop, Download it to check it out ";
                String shareBodyText = "https://drive.google.com/drive/folders/1_z5wvW02rjidCkBTHOgMQEBjZF1rWPKn?usp=sharing";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(shareIntent, "Share With"));


            }
        else if (id == R.id.nav_categories)
        {


                Intent intent = new Intent(MainActivity.this, Productcategories.class);
                startActivity(intent);


        }
        else if (id == R.id.nav_settings)
        {

                Intent intent = new Intent(MainActivity.this, ViewSettingsActivity.class);
                startActivity(intent);


        }
        else if (id == R.id.nav_logout)
        {

            Paper.book().destroy();

            FirebaseAuth.getInstance().signOut();


            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
