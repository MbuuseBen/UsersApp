package com.example.usersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.usersapp.Model.Orders;
import com.example.usersapp.Orders.OrderDetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class OrdersActivity extends AppCompatActivity {


    private RecyclerView ordersList;
    private DatabaseReference orderRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mAuth = FirebaseAuth.getInstance();
        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(mAuth.getCurrentUser().getUid());

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options=
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(orderRef, Orders.class)
                        .build();
        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull OrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull @NotNull Orders model) {



                        holder.userFirstName.setText("Name : "+model.getFirstname()+ " "+ model.getLastname());
                        holder.userPhoneNumber.setText("Phone : "+model.getPhone());
                      //  holder.userTotalPrice.setText("Total Amount : "+ (new DecimalFormat("#,###")).format(Integer.valueOf(model.getTotalAmount())));
                        holder.userDateTime.setText("Placed on : "+model.getDate() + "at " +model.getTime());
                       // holder.userShippingAddress.setText("Address : "+model.getAddress());
                        holder.orderId.setText("Order ID :#"+model.getOrderid());
                     //   holder.State.setText("Order State : "+model.getState());
                      //  holder.specialText.setText("Special Text : "+model.getSpecialText());


                        holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              //  String uID = getRef(position).getKey();

                                Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
                                intent.putExtra("orderID", model.getOrderid());
                                startActivity(intent);
                            }
                        });

//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                CharSequence options[] = new CharSequence[]{
//                                        "Yes",
//                                        "No"
//                                };
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
//                                builder.setTitle("Have You Shipped these ordered products?");
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        if(i==0){
//
//                                            String uID = getRef(position).getKey();
//                                            RemoveOrder(uID);
//                                        }else {
//                                            finish();
//                                        }
//                                    }
//                                });
//
//                                builder.show();
//                            }
//                        });

                    }

                    @NonNull
                    @NotNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout1,parent,false);
                        return  new OrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public  static  class OrdersViewHolder extends  RecyclerView.ViewHolder{

        public TextView userFirstName, userPhoneNumber, userTotalPrice, userDateTime,userShippingAddress,orderId,State,specialText;
        public Button ShowOrdersBtn;



        public  OrdersViewHolder(View itemView){
            super((itemView));

            userFirstName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress= itemView.findViewById(R.id.order_address_city);
            ShowOrdersBtn = itemView.findViewById(R.id.show_order_products_btn);
            orderId= itemView.findViewById(R.id.order_id);
            State=itemView.findViewById(R.id.order_state);
            specialText = itemView.findViewById(R.id.order_user_specialText);


        }

    }

//    private void RemoveOrder(String uID) {
//
//        orderRef.child(uID).removeValue();
//    }

}