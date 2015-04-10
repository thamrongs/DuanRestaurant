package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 24/3/2558.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TipAdapter extends ArrayAdapter<Tips> {
    ArrayList<Tips> tipList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public TipAdapter(Context context, int resource, ArrayList<Tips> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        tipList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.date = (TextView) v.findViewById(R.id.date);
            holder.amount = (TextView) v.findViewById(R.id.amount);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.date.setText(tipList.get(position).getDate());
        holder.amount.setText(String.valueOf(tipList.get(position).getAmount()));

        return v;

    }

    static class ViewHolder {
        public TextView date;
        public TextView amount;

    }
}