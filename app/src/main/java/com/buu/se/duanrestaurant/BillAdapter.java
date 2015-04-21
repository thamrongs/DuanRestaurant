package com.buu.se.duanrestaurant;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BillAdapter extends ArrayAdapter<Bills> {
    ArrayList<Bills> menuList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public BillAdapter(Context context, int resource, ArrayList<Bills> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        menuList = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
            holder.menuName = (TextView) v.findViewById(R.id.menuName);
            holder.menuPrice = (TextView) v.findViewById(R.id.menuPrice);
            holder.number = (TextView) v.findViewById(R.id.txv_number);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.mipmap.ic_launcher);
        new DownloadImageTask(holder.imageview).execute(menuList.get(position).getImg());
        holder.menuName.setText(menuList.get(position).getName());
        holder.number.setText(String.valueOf(menuList.get(position).getAmount()));
        holder.menuPrice.setText("ราคา " + menuList.get(position).getPrice() + " บาท");

        return v;

    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView menuName;
        public TextView menuPrice;
        public TextView number;

    }
}
