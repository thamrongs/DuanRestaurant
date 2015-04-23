package com.buu.se.duanrestaurant;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by thamrongs on 3/31/15 AD.
 */

public class TableAdapter extends ArrayAdapter<Tables> {
    ArrayList<Tables> tableList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public TableAdapter(Context context, int resource, ArrayList<Tables> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        tableList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.imageTable);
            holder.tableNumber = (TextView) v.findViewById(R.id.tableNumber);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.tableNumber.setText(tableList.get(position).getId()+"");

        switch (tableList.get(position).getStatus()){
            case 0: holder.imageview.setImageResource(R.drawable.table_blank);
                break;
            case 1: holder.imageview.setImageResource(R.drawable.table_reserved);
                break;
            case 2: holder.imageview.setImageResource(R.drawable.table_eating);
                break;
        }

        return v;

    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView tableNumber;

    }
}