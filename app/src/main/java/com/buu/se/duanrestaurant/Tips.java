package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 24/3/2558.
 */

public class Tips {

    private String date;
    private Double amount;


    public Tips() {
    }

    public Tips(String date, Double amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}


