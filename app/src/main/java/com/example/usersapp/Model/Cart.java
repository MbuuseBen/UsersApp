package com.example.usersapp.Model;

public class Cart {
    public String pid, pname, image,discount,seller;
    public int price, quantity;



    public Cart() {
    }

    public Cart(String pid, String pname, String image, String discount, int price, int quantity,String seller) {
        this.pid = pid;
        this.pname = pname;
        this.image = image;
        this.discount = discount;
        this.price = price;
        this.seller = seller;
        this.quantity = quantity;

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}