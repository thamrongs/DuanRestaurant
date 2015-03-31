package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
}