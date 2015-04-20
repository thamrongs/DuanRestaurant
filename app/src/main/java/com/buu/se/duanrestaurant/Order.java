package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

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

        ListView listview = (ListView) findViewById(R.id.list);
        adapter = new MenuAdapter(getApplicationContext(), R.layout.order_list_big, menusList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), menusList.get(position).getId(), Toast.LENGTH_LONG).show();
            }
        });

        RequestParams params = new RequestParams();
        params.put("tabid", tabid);
    }
    @Override
    public void onClick(View v) {
        String url = "";
        RequestParams params = null;
        switch (v.getId()) {
            case R.id.btn_sumorder:
//                url = "http://" + ip + "/resman/index.php/table/sitbyreserve";
//                params = new RequestParams();
//                params.put("tabid", tabid);
//                reserve(url, params);
                break;
            case R.id.btn_submit:
//                url = "http://" + ip + "/resman/index.php/table/cancel";
//                params = new RequestParams();
//                params.put("tabid", tabid);
//                reserve(url, params);
                break;
        }
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