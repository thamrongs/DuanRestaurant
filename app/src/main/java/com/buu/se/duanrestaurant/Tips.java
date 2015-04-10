package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 24/3/2558.
 */

public class Tips {

    private String date;
    private int amount;


    public Tips() {
    }

    public Tips(String date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}


