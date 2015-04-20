package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class TableFragment extends Fragment {
    ArrayList<Tables> tablesList;
    TableAdapter adapter;
    final int MYACTIVITY_REQUEST_CODE = 101;

    Fragment fr;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TableFragment newInstance(int sectionNumber) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        SharedPreferences authen = getActivity().getSharedPreferences("authen", getActivity().MODE_PRIVATE);
        String ip = authen.getString("ip", "10.51.4.106");

        tablesList = new ArrayList<Tables>();
        new JSONAsyncTask().execute("http://" + ip + "/resman/index.php/table/get");

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        adapter = new TableAdapter(getActivity().getApplicationContext(), R.layout.table_list_c, tablesList);
        gridview.setAdapter(adapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {

                Intent data;
                switch (tablesList.get(position).getStatus()) {
                    case 0:
                        data = new Intent(getActivity(), ReserveTable.class);
                        data.putExtra("id", String.valueOf(tablesList.get(position).getId()));
                        startActivityForResult(data, MYACTIVITY_REQUEST_CODE);
                        break;
                    case 1:
                        data = null;
                        data = new Intent(getActivity(), DetailReserved.class);
                        data.putExtra("id", String.valueOf(tablesList.get(position).getId()));
                        startActivityForResult(data, MYACTIVITY_REQUEST_CODE);
                        break;
                    case 2:
                        data = null;
                        data = new Intent(getActivity(), Order.class);
                        data.putExtra("id", String.valueOf(tablesList.get(position).getId()));
                        startActivityForResult(data, MYACTIVITY_REQUEST_CODE);
                        break;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == MYACTIVITY_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
            SharedPreferences authen = getActivity().getSharedPreferences("authen", getActivity().MODE_PRIVATE);
            String ip = authen.getString("ip", "10.51.4.106");

            tablesList = new ArrayList<Tables>();
            new JSONAsyncTask().execute("http://" + ip + "/resman/index.php/table/get");

            GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
            adapter = new TableAdapter(getActivity().getApplicationContext(), R.layout.table_list_c, tablesList);
            gridview.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
                    JSONArray jarray = jsono.getJSONArray("tables");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Tables table = new Tables();

                        table.setId(object.getInt("id"));
                        table.setStatus(object.getInt("status"));

                        tablesList.add(table);
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