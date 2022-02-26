package com.example.usersapp.Model;

public class Produ
{
    private String pid,pname,price,image;
   // private int price;

    public Produ()
    {

    }

    public Produ(String pid, String pname, String price, String image) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.image = image;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
