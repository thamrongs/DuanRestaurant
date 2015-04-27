package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by thamrongs on 3/4/15 AD.
 */
public class Bill extends Activity implements View.OnClickListener {

    ArrayList<Bills> menusList;
    final int MYACTIVITY_REQUEST_CODE = 101;
    BillAdapter adapter;
    private Button btn_checkbill;
    ProgressDialog prgDialog;
    String tabid, ip;
    public String url;
    int usr_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_order);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent intent = getIntent();
        tabid = intent.getStringExtra("id");

        btn_checkbill = (Button) findViewById(R.id.btn_checkbill);
        btn_checkbill.setOnClickListener(this);

        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        ip = authen.getString("ip", "10.51.4.106");
        menusList = new ArrayList<Bills>();
        RequestParams paramss = new RequestParams();
        paramss.put("tabid", tabid);

        url = "http://" + ip + "/resman/index.php/order/get?tabid=" + tabid;
        invokeWS(paramss);
    }
    @Override
    public void onClick(View v) {
        String url = "";
        RequestParams params = null;
        switch (v.getId()) {
            case R.id.btn_checkbill:
                String urll = "http://" + ip + "/resman/index.php/checkbill/check";

                RequestParams paramss = new RequestParams();
                paramss.put("tabid", tabid);
                paramss.put("usrid", usr_id);

                checkbillWS(urll, paramss);
                break;
        }
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void checkbillWS(String url, RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        SharedPreferences authen = getSharedPreferences("persondata", MODE_PRIVATE);
        String api_key = authen.getString("apikey", "");
        client.addHeader("Authorization", api_key);
        client.post(url,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                prgDialog.dismiss();
                try {
                    if(response.getInt("status") == 1){
                        Toast.makeText(getApplicationContext(), "Check Bill Complete!", Toast.LENGTH_LONG).show();
                    }else if(response.getInt("status") == -1){
                        Toast.makeText(getApplicationContext(), "Check Bill Incomplete!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something wrong Please call Admin!", Toast.LENGTH_LONG).show();
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
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

    public void invokeWS(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url ,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(response.getJSONObject("data").getInt("status") == 1){
                        new JSONAsyncTask().execute(url);

                        SharedPreferences persondata = getSharedPreferences("persondata", Context.MODE_PRIVATE);
                        usr_id = persondata.getInt("userid", 0);

                        ListView listview = (ListView) findViewById(R.id.listSumOrder);
                        adapter = new BillAdapter(getApplicationContext(), R.layout.order_list, menusList);

                        listview.setAdapter(adapter);

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long id) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplicationContext(), menusList.get(position).getId(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else if(response.getJSONObject("data").getInt("status") == -1){
                        Toast.makeText(getApplicationContext(), "Get Tips Incomplete!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something wrong Please call Admin!", Toast.LENGTH_LONG).show();
                    }
                    setResult(Activity.RESULT_OK);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);

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

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Bill.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONObject("data").getJSONArray("orderlist");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Bills bill = new Bills();
                        Log.d("TESTNAME", object.getString("name"));
                        bill.setId(object.getInt("id"));
                        bill.setName(object.getString("name"));
                        bill.setImg(object.getString("img"));
                        bill.setAmount(object.getInt("amount"));
                        bill.setPrice(object.getDouble("price"));

                        menusList.add(bill);
                    }
                    return true;
                }

                //------------------>>

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();

        }
    }

}