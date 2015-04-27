package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailReserved extends Activity implements View.OnClickListener {

    private Button btn_time, btn_sit, btn_cancel;
    final int MYACTIVITY_REQUEST_CODE = 101;
    private TextView txv_name, txv_tel, txv_amount, txv_tableId;
    ProgressDialog prgDialog;
    String tabid, ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_reserve_table);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent intent = getIntent();
        tabid = intent.getStringExtra("id");
        txv_tableId = (TextView) findViewById(R.id.txv_tableId);
        txv_tableId.setText(tabid);

        btn_time = (Button) findViewById(R.id.imageButton3);
        btn_sit = (Button) findViewById(R.id.btn_sit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_time.setOnClickListener(this);
        btn_sit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        txv_name = (TextView) findViewById(R.id.txv_name);
        txv_tel = (TextView) findViewById(R.id.txv_tel);
        txv_amount = (TextView) findViewById(R.id.txv_amount);

        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        ip = authen.getString("ip", "10.51.4.106");
        String url = "http://" + ip + "/resman/index.php/table/reserveddetail";

        RequestParams params = new RequestParams();
        params.put("tabid", tabid);
        invokeWS(url, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_reserved, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String url = "";
        RequestParams params = null;
        Intent data;
        switch (v.getId()) {
            case R.id.btn_sit:
                url = "http://" + ip + "/resman/index.php/table/sitbyreserve";
                params = new RequestParams();
                params.put("tabid", tabid);
                reserve(url, params);

                data = new Intent(this, Order.class);
                data.putExtra("id", String.valueOf(tabid));
                startActivityForResult(data, MYACTIVITY_REQUEST_CODE);
                break;
            case R.id.btn_cancel:
                url = "http://" + ip + "/resman/index.php/table/cancel";
                params = new RequestParams();
                params.put("tabid", tabid);
                reserve(url, params);
                break;
            case R.id.imageButton3:
                break;
        }
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
        SharedPreferences authen = getSharedPreferences("persondata", MODE_PRIVATE);
        String api_key = authen.getString("apikey", "");
        client.addHeader("Authorization", api_key);
        client.setConnectTimeout(5000);
        client.post(url,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                try {
                    if(response.getJSONObject("reserveddata").getInt("status") == 1) {
                        btn_time.setText(response.getJSONObject("reserveddata").getString("rsvtime"));
                        txv_name.setText(response.getJSONObject("reserveddata").getString("name"));
                        txv_tel.setText(response.getJSONObject("reserveddata").getString("tel"));
                        txv_amount.setText(response.getJSONObject("reserveddata").getString("amountperson"));
//                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
//                        navigatetoHomeActivity(response.getJSONObject("data"));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

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
                    Toast.makeText(getApplicationContext(), "Cannot connect to server. \nPlease make sure IP are correct.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void reserve(String url, RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(5000);
        client.post(url,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                prgDialog.dismiss();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

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
                    Toast.makeText(getApplicationContext(), "Cannot connect to server. \nPlease make sure IP are correct.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
