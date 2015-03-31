package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by thamrongs on 3/9/15 AD.
 */
public class ReserveTable extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_table);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(id);
        Log.d("ID", id+"");

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
        }

    } // time picker
    public void showTimePickerDialog(View v) {

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}