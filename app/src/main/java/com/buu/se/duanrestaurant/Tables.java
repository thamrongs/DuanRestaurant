package com.buu.se.duanrestaurant;

/**
 * Created by thamrongs on 3/31/15 AD.
 */
public class Tables {
    private int id;
    private int status;

    public Tables() {
    }

    public Tables(int id, int status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
