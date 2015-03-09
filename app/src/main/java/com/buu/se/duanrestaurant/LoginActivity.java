package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void onClickSetIp(View v){
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

        alertDlg.setMessage("ใส่ IP Server ที่ต้องการเชื่อมต่อ")
                .setTitle("ตั้งค่า IP")
                .setCancelable(false);
        final EditText input = new EditText(this);
        alertDlg.setView(input);

        alertDlg.setPositiveButton("ยืนยันการเชื่อมต่อ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDlg.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.show();

    }

    public void onClickLogin(View v){
        Intent data = new Intent(LoginActivity.this, MainActivity.class);
        data.putExtra("userid","000001");
        startActivity(data);
    }
}
