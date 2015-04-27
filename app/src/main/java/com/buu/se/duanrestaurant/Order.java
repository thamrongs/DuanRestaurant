package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
public class Order extends Activity implements View.OnClickListener {

    ArrayList<Menus> menusList;
    final int MYACTIVITY_REQUEST_CODE = 101;
    MenuAdapter adapter;
    private Button btn_sumorder, btn_submit;
    ProgressDialog prgDialog;
    String tabid, ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent intent = getIntent();
        tabid = intent.getStringExtra("id");

        btn_sumorder = (Button) findViewById(R.id.btn_sumorder);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_sumorder.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        ip = authen.getString("ip", "10.51.4.106");
        menusList = new ArrayList<Menus>();
        new JSONAsyncTask().execute("http://" + ip + "/resman/index.php/listmenu/get");

        final ListView listview = (ListView) findViewById(R.id.list);
        adapter = new MenuAdapter(getApplicationContext(), R.layout.order_list_big, menusList);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Click Tip " + position + " Complete!", Toast.LENGTH_LONG).show();
            }
        } );

    }
    @Override
    public void onClick(View v) {
        String url = "";
        RequestParams params = null;
        switch (v.getId()) {
            case R.id.btn_sumorder:
                Intent data = null;
                data = new Intent(this, Bill.class);
                data.putExtra("id", tabid);
                startActivityForResult(data, MYACTIVITY_REQUEST_CODE);
                break;
            case R.id.btn_submit:
                ListView listview = (ListView) findViewById(R.id.list);
                int count = listview.getAdapter().getCount();
                String orderlist = "";

                int aaaa;
                int idddd;
                for (int i = 0; i < count; i++) {
                    aaaa = menusList.get(i).getAmount();
                    idddd = menusList.get(i).getId();
                    if(aaaa != 0) {
                        if(i != count-1) {
                            orderlist += "orders[" + String.valueOf(idddd) + "]=" + String.valueOf(aaaa) + "&";
                        } else {
                            orderlist += "orders[" + String.valueOf(idddd) + "]=" + String.valueOf(aaaa);
                        }
                    }
                }

                String urll = "http://" + ip + "/resman/index.php/order/set";


                RequestParams paramss = new RequestParams();
                paramss.put("tabid", tabid);
                paramss.put("orderlist", orderlist);

                invokeWS(urll, paramss);

                Intent dataa = null;
                dataa = new Intent(this, Bill.class);
                dataa.putExtra("id", tabid);
                startActivityForResult(dataa, MYACTIVITY_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == MYACTIVITY_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
            setResult(Activity.RESULT_OK);
            finish();
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
        client.post(url,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                prgDialog.dismiss();
                try {
                    if(response.getJSONObject("data").getInt("status") == 1){
                        Toast.makeText(getApplicationContext(), "Order Complete!", Toast.LENGTH_LONG).show();
                    }else if(response.getInt("status") == -1){
                        Toast.makeText(getApplicationContext(), "Order Incomplete!", Toast.LENGTH_LONG).show();
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


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Order.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                SharedPreferences authen = getSharedPreferences("persondata",MODE_PRIVATE);
                String api_key = authen.getString("apikey", "");
                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                httppost.addHeader("Authorization", api_key);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("menus");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Menus menu = new Menus();

                        menu.setId(object.getInt("id"));
                        menu.setName(object.getString("name"));
                        menu.setImg(object.getString("img"));
                        menu.setPrice(object.getDouble("price"));

                        menusList.add(menu);
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