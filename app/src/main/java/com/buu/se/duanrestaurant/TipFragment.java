package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thamrongs on 3/4/15 AD.
 */

public class TipFragment extends Fragment implements View.OnClickListener {
    ArrayList<Tips> tipList;
    TipAdapter      adapter;
    public String   FromDate = "";
    public String   ToDate   = "";
    private int     usr_id;
    public String   url;
    SharedPreferences persondata;
    RequestParams params = new RequestParams();
    DialogFragment  fromFragment = new fromDatePickerFragment();
    DialogFragment  toFragment   = new toDatePickerFragment();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TipFragment newInstance(int sectionNumber) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        persondata = getActivity().getSharedPreferences("persondata", Context.MODE_PRIVATE);
        usr_id = persondata.getInt("userid", 0);

        View rootView = inflater.inflate(R.layout.fragment_tip, container, false);
        invokeWS(params);

        Button fromdate = (Button) rootView.findViewById(R.id.fromdate);
        Button todate   = (Button) rootView.findViewById(R.id.todate);
        fromdate.setOnClickListener(this);
        todate.setOnClickListener(this);

        return rootView;
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
                SharedPreferences authen = getActivity().getSharedPreferences("persondata", getActivity().MODE_PRIVATE);
                String api_key = authen.getString("apikey", "");
                httppost.addHeader("Authorization", api_key);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("tips");
                    NumberFormat numberFormat = new DecimalFormat(".00");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Tips tip = new Tips();

                        tip.setDate(((object.getString("date").equals("")) ?
                                "รวม" : object.getString("date")));

                        tip.setAmount(Double.parseDouble(numberFormat.format(object.getDouble("amount"))));

                        tipList.add(tip);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fromdate:
                fromFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.todate:
                toFragment.show(getFragmentManager(), "datePicker");
                break;
        }

    }

    public void invokeWS(RequestParams params){
        params.put("usr_id", usr_id);
        SharedPreferences authen = getActivity().getSharedPreferences("authen", getActivity().MODE_PRIVATE);
        String ip = authen.getString("ip", "10.51.4.106");
        url = "http://" + ip + "/resman/index.php/tips/get?usr_id=" + usr_id + "&fromdate=" + FromDate + "&todate=" + ToDate;
        AsyncHttpClient client = new AsyncHttpClient();
        SharedPreferences persondata = getActivity().getSharedPreferences("persondata", getActivity().MODE_PRIVATE);
        String api_key = persondata.getString("apikey", "");
        client.addHeader("Authorization", api_key);
        client.post(url ,params ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(response.getInt("status") == 1){
                        tipList = new ArrayList<Tips>();
                        new JSONAsyncTask().execute(url);
                        ListView listview = (ListView) getActivity().findViewById(R.id.list);
                        adapter = new TipAdapter(getActivity().getApplicationContext(), R.layout.tip_list, tipList);
                        listview.setAdapter(adapter);

                        TextView sumamount = (TextView) getActivity().findViewById(R.id.sumamount);
                        if(response.getDouble("sumtip") == 0) {
                            sumamount.setText("0");
                        }else {
                            sumamount.setText(String.valueOf(response.getDouble("sumtip")));
                        }
                    }else if(response.getInt("status") == -1){
                        Toast.makeText(getActivity().getApplicationContext(), "Get Tips Incomplete!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Something wrong Please call Admin!", Toast.LENGTH_LONG).show();
                    }
                    getActivity().setResult(Activity.RESULT_OK);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);

                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class fromDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Button fromdate = (Button) getActivity().findViewById(R.id.fromdate);
            FromDate = year+"-"+month+"-"+day;
            fromdate.setText(year+"-"+month+"-"+day);
            params.put("fromdate", year+"-"+month+"-"+day);
            invokeWS(params);
        }
    }

    public class toDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Button todate = (Button) getActivity().findViewById(R.id.todate);
            ToDate = year+"-"+month+"-"+day;
            todate.setText(year+"-"+month+"-"+day);
            params.put("todate", year+"-"+month+"-"+day);
            invokeWS(params);
        }
    }
}
