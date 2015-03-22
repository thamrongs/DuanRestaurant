package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

    private String username, password, ip;
    private EditText user, pass, input;
    private  AlertDialog.Builder alertDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input = new EditText(this);
        input.setId(R.id.ip);
        initip();

        user = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
    }

    private void initip() {
        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        ip = authen.getString("ip", "10.51.4.106");
    }

    public void setip() {
        ip = input.getText().toString();
        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        SharedPreferences.Editor editor = authen.edit();
        editor.putString("ip", ip);
        editor.commit();
        return;
    }

    public void onClickSetIp(View v){
        alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Please Input IP Server")
                .setTitle("IP Setting")
                .setCancelable(false);
        input.setText(ip);
        alertDlg.setView(input);

        alertDlg.setPositiveButton("Connect",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        LoginActivity.this.setip();
//                        dialog.cancel();
                    }
                });
        alertDlg.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDlg.create();
        //alert.show();
        alertDlg = null;
    }

    public void createAlert() {
        alertDlg = null;
        alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Please Input IP Server")
                .setTitle("IP Setting")
                .setCancelable(false);


        alertDlg.setView(input);

    }

    public void onClickLogin(View v){
        Intent data = new Intent(LoginActivity.this, MainActivity.class);
        username = user.getText().toString();
        password = pass.getText().toString();

        if(!username.matches("") && !password.matches("")) {

        }else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
        data.putExtra("userid","000001");
        startActivity(data);
    }
}
