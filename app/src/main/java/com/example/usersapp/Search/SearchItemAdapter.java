package com.example.usersapp.Search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usersapp.Model.Produ;
import com.example.usersapp.Products.ProductDetailsActivity;
import com.example.usersapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

// creating a variable for array list and context.
private ArrayList<Produ> productsModalArrayList;
private Context context;

// creating a constructor for our variables.
public SearchItemAdapter(ArrayList<Produ> productsModalArrayList, Context context) {
        this.productsModalArrayList = productsModalArrayList;
        this.context = context;
        }

// method for filtering our recyclerview items.
public void filterList(ArrayList<Produ> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
    productsModalArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
        }

@NonNull
@Override
public SearchItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull SearchItemAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
    Produ modal = productsModalArrayList.get(position);
        holder.productName.setText(modal.getPname());
    Picasso.get().load(modal.getImage()).into(holder.productImage);
        holder.productPrice.setText("UGX " + (new DecimalFormat("#,###")).format(Integer.valueOf(modal.getPrice())));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
        intent.putExtra("pid", modal.getPid());
        view.getContext().startActivity(intent);

    }
});

        }

@Override
public int getItemCount() {
        // returning the size of array list.
        return productsModalArrayList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    // creating variables for our views.
    private TextView productName, productPrice;
    private ImageView productImage;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        // initializing our views with their ids.
        productName = itemView.findViewById(R.id.product_name);
        productImage = itemView.findViewById(R.id.product_image);
        productPrice = itemView.findViewById(R.id.product_price);
    }
}
}