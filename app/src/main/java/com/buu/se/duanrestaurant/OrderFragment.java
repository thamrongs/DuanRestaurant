package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
public class OrderFragment extends Fragment {

    ArrayList<Menus> menusList;

    MenuAdapter adapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static OrderFragment newInstance(int sectionNumber) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        SharedPreferences authen = getActivity().getSharedPreferences("authen", getActivity().MODE_PRIVATE);
        String ip = authen.getString("ip", "10.51.4.106");

        menusList = new ArrayList<Menus>();
        new JSONAsyncTask().execute("http://" + ip + "/resman/index.php/listmenu/get");

        ListView listview = (ListView) rootView.findViewById(R.id.list);
        adapter = new MenuAdapter(getActivity().getApplicationContext(), R.layout.order_list_big, menusList);

        listview.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void add() {

    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                SharedPreferences authen = getActivity().getSharedPreferences("persondata", getActivity().MODE_PRIVATE);
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
//            if(result == false)
//                Toast.makeText(con, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }


}