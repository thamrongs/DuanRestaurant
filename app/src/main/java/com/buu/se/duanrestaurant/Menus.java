package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 24/3/2558.
 */

public class Menus {

    private int id;
    private String name;
    private String img;
    private double price;

    public Menus() {
    }

    public Menus(int id, String name, String img, double price) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


