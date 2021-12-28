package com.example.usersapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.usersapp.Interface.ItemClickListner;
import com.example.usersapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice,txtProductQuantity, txtTotal,txtSubTotal,txtSellerName;
    private ItemClickListner itemClickListner;
    public ImageView imageView,deleteBtn1;

    public CartViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.cart_product_image);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtSubTotal=itemView.findViewById(R.id.cart_product_subtotal);
        deleteBtn1=(ImageView) itemView.findViewById(R.id.deleteBtn1);
       // txtSellerName = itemView.findViewById(R.id.seller_name);
        txtSellerName = itemView.findViewById(R.id.seller_name);




    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

}

