package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by thamrongs on 3/9/15 AD.
 */
public class ReserveTable extends Activity implements View.OnClickListener {

    private Button btn_add, btn_reduce, btn_reserve, btn_sit;
    private EditText edt_num, edt_name, edt_tel;
    private TextView txv_tableId;
    private int tab_id, usr_id;
    private String time_reserve;
    private ProgressDialog prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_table);

        initailVal();
    }

    public void initailVal() {
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        btn_sit = (Button) findViewById(R.id.btn_sit);

        btn_add.setOnClickListener(this);
        btn_reduce.setOnClickListener(this);
        btn_reserve.setOnClickListener(this);
        btn_sit.setOnClickListener(this);

        edt_num = (EditText) findViewById(R.id.edt_num);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_tel = (EditText) findViewById(R.id.edt_tel);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        txv_tableId = (TextView) findViewById(R.id.txv_tableId);
        txv_tableId.setText(id);
        tab_id = Integer.parseInt(id);

        SharedPreferences persondata = getSharedPreferences("persondata", Context.MODE_PRIVATE);
        usr_id = persondata.getInt("userid", 0);

        if (prgDialog == null || !prgDialog.isShowing()) {
            // Instantiate Progress Dialog object
            prgDialog = new ProgressDialog(this);
            // Set Progress Dialog Text
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
        }
    }

    private void add() {
        int num = Integer.parseInt(edt_num.getText().toString());
        num += 1;
        edt_num.setText(String.valueOf(num));
    }

    private void reduce() {
        int num = Integer.parseInt(edt_num.getText().toString());
        if(num > 1) {
            num -= 1;
            edt_num.setText(String.valueOf(num));
        }
    }

    private void reserve() {
        Button tButton = (Button) findViewById(R.id.imageButton3);
        String sss = tButton.getText().toString();
        if(sss.equals("เวลาจอง") || edt_name.getText().toString().matches("") || edt_tel.getText().toString().matches("") ) {
            Toast.makeText(getApplicationContext(), "Please Fill Form!", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
            String ip = authen.getString("ip", "10.51.4.106");
            String url = "http://" + ip + "/resman/index.php/table/reserve";

            RequestParams params = new RequestParams();
            params.put("tabid", tab_id);
            params.put("name", edt_name.getText().toString());
            params.put("tel", edt_tel.getText().toString());
            params.put("amountperson", edt_num.getText().toString());
            params.put("usrid", usr_id);
            params.put("rsvtime", time_reserve);

            invokeWS(url, params);
        }
    }

    private void sit() {
        SharedPreferences authen = getSharedPreferences("authen", MODE_PRIVATE);
        String ip = authen.getString("ip", "10.51.4.106");
        String url = "http://" + ip + "/resman/index.php/table/sit";

        RequestParams params = new RequestParams();
        params.put("tabid", tab_id);
        params.put("name", edt_name.getText().toString());
        params.put("tel", edt_tel.getText().toString());
        params.put("amountperson", edt_num.getText().toString());
        params.put("usrid", usr_id);

        invokeWS(url, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_reduce:
                reduce();
                break;
            case R.id.btn_reserve:
                reserve();
                break;
            case R.id.btn_sit:
                sit();
                break;
        }
    }

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //MainActivity.this.SetButton(view,hourOfDay,minute);
            Button tButton = (Button) findViewById(R.id.imageButton3);
            String m = "",h = "";
            if(minute < 10){
                m = "0"+minute;
            }else{
                m = minute+"";
            }
            if(hourOfDay < 10){
                h = "0"+hourOfDay;
            }else{
                h = hourOfDay+"";
            }
            tButton.setText(h + ":" + m);
            time_reserve = h + ":" + m + ":00";
        }

    } // time picker
    public void showTimePickerDialog(View v) {

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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
                prgDialog.dismiss();
                try {
                    if(response.getInt("status") == 1){
                        Toast.makeText(getApplicationContext(), "Reserve/Sit Table Complete!", Toast.LENGTH_LONG).show();
                    }else if(response.getInt("status") == -1){
                        Toast.makeText(getApplicationContext(), "Reserve/Sit Table Incomplete!", Toast.LENGTH_LONG).show();
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
}