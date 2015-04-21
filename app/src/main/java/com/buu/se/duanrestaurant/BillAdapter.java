package com.buu.se.duanrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sarin on 21/4/2558.
 */
public class BillAdapter {
    ArrayList<Bills> billList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Integer num;
    TextView nnn;

    public BillAdapter(Context context, int resource, ArrayList<Bills> objects) {
//        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        billList = objects;
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // convert view = design
//        View v = convertView;
//        if (v == null) {
//            holder = new ViewHolder();
//            v = vi.inflate(Resource, null);
//            holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
//            holder.menuName = (TextView) v.findViewById(R.id.menuName);
//            holder.menuPrice = (TextView) v.findViewById(R.id.menuPrice);
//            holder.btn_add = (Button) v.findViewById(R.id.btn_add);
//            holder.btn_reduce = (Button) v.findViewById(R.id.btn_reduce);
//            holder.number = (TextView) v.findViewById(R.id.txv_number);
//            v.setTag(holder);
//        } else {
//            holder = (ViewHolder) v.getTag();
//        }
//        holder.imageview.setImageResource(R.mipmap.ic_launcher);
//        new DownloadImageTask(holder.imageview).execute(billList.get(position).getImg());
//        holder.menuName.setText(billList.get(position).getName());
//        holder.menuPrice.setText("ราคา " + billList.get(position).getPrice() + " บาท");
//
//        return v;
//
//    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView menuName;
        public TextView menuId;
        public TextView menuPrice;
        public Button btn_add;
        public Button btn_reduce;
        public TextView number;

    }
}
