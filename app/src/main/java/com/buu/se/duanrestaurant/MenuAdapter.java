package com.buu.se.duanrestaurant;

/**
 * Created by Sarin on 24/3/2558.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<Menus> {
    ArrayList<Menus> menuList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Integer num;
    TextView nnn;

    public MenuAdapter(Context context, int resource, ArrayList<Menus> objects) {
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
            holder.btn_add = (Button) v.findViewById(R.id.btn_add);
            holder.btn_reduce = (Button) v.findViewById(R.id.btn_reduce);
            holder.number = (TextView) v.findViewById(R.id.txv_number);
            holder.menuId = (TextView) v.findViewById(R.id.menuid);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.mipmap.ic_launcher);
        new DownloadImageTask(holder.imageview).execute(menuList.get(position).getImg());
        holder.menuName.setText(menuList.get(position).getName());
        holder.menuPrice.setText("ราคา " + menuList.get(position).getPrice() + " บาท");

        int menuid = menuList.get(position).getId();
        holder.menuId.setText(String.valueOf(menuid));
        holder.menuId.setVisibility(View.GONE);

//        nnn = (TextView) v.findViewById(R.id.txv_number);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = (View)v.getParent();
                nnn = (TextView) view.findViewById(R.id.txv_number);
                //num = Integer.parseInt(nnn.getText().toString());
                num = menuList.get(position).getAmount();
                num = num + 1;
                nnn.setText(num.toString());
                menuList.get(position).setAmount(num);
            }
        });

        holder.btn_reduce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = (View)v.getParent();
                nnn = (TextView) view.findViewById(R.id.txv_number);
                num = Integer.parseInt(nnn.getText().toString());

                if(num > 0) {
                    num = num - 1;
                }
                nnn.setText(num.toString());
                menuList.get(position).setAmount(num);
            }
        });

        return v;

    }

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