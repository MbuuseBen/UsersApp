package com.example.usersapp.Model;

public class Produ
{
    private String pname,image;
    private int price;

    public Produ()
    {

    }

    public Produ(String pname, String image, int price) {
        this.pname = pname;
        this.image = image;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
