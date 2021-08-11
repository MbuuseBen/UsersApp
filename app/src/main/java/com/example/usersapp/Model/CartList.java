package com.example.usersapp.Model;

public class CartList {
    public String products;

    public CartList() {
    }

    public CartList(String products) {
        this.products = products;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
