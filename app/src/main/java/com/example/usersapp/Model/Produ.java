package com.example.usersapp.Model;

public class Produ
{
    private String pid,pname,image;
    private int price;

    public Produ()
    {

    }

    public Produ(String pid, String pname, String image, int price) {
        this.pid = pid;
        this.pname = pname;
        this.image = image;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
