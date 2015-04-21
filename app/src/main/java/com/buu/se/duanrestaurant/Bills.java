package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 21/4/2558.
 */
public class Bills {
    private int id;
    private int menuid;
    private int amount;
    private double price;
    private String name;
    private String img;

    public Bills() {

    }

    public Bills(int menuid, int amount) {
        this.menuid = menuid;
        this.amount = amount;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
