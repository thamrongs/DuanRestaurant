package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 21/4/2558.
 */
public class Bills {
    private int menuid;
    private int amount;

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
}
