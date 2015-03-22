package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

    private String username, password, ip, fullurl;
    private EditText user, pass, input;
    private  AlertDialog.Builder alertDlg;
    private AlertDialog alert;
    ProgressDialog prgDialog;
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
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
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
        if(isOnline()) {
            Intent data = new Intent(LoginActivity.this, MainActivity.class);
            username = user.getText().toString();
            password = pass.getText().toString();

            if (!username.matches("") && !password.matches("")) {
                RequestParams params = new RequestParams();
                params.put("user", username);
                params.put("pass", password);
                fullurl = "http://" + ip + "/resman/index.php/authen/login";
                Toast.makeText(getApplicationContext(), fullurl, Toast.LENGTH_LONG).show();
                invokeWS(fullurl, params);


            } else {
                Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Network is Disconnect", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(String url, RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                try {
                    if(response.getJSONObject("data").getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        navigatetoHomeActivity(response.getJSONObject("data"));
                    }else {
                        Toast.makeText(getApplicationContext(), response.getJSONObject("data").getString("errormsg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);

                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void navigatetoHomeActivity(JSONObject obj) {
        Intent data = new Intent(LoginActivity.this, MainActivity.class);
        try {
            data.putExtra("userid", obj.getInt("userid"));
            data.putExtra("fname", obj.getString("fname"));
            data.putExtra("lname", obj.getString("lname"));
            data.putExtra("tel", obj.getString("tel"));
            data.putExtra("email", obj.getString("email"));
            data.putExtra("picurl", obj.getString("picurl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(data);
    }
}
