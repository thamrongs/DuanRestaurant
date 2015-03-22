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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

    private String username, password, ip;
    private EditText user, pass, input;
    private  AlertDialog.Builder alertDlg;
    private AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input = new EditText(this);
        input.setId(R.id.ip);
        initip();

        user = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        createAlert();
    }

    private void initip() {
        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        ip = authen.getString("ip", "10.51.4.106");
    }

    public boolean setip() {
        String ipp = input.getText().toString();

        Pattern IP_ADDRESS
                = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(ipp);

        if (matcher.matches()) {
            ip = ipp;
            SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
            SharedPreferences.Editor editor = authen.edit();
            editor.putString("ip", ip);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public void onClickSetIp(View v) {
        alert.show();
    }

    public void createAlert() {
        alertDlg = null;
        alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Please Input IP Server")
                .setTitle("IP Setting")
                .setCancelable(false);

        input.setText(ip);
        alertDlg.setView(input);

        alertDlg.setPositiveButton("Connect",
            new DialogInterface.OnClickListener() {
                private boolean ippattern;
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ippattern = LoginActivity.this.setip();
                    if(ippattern) {
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "IP Address are Incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        );

        alertDlg.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }
        );

        alert = null;
        alert = alertDlg.create();
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
