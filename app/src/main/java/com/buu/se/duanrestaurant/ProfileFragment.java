package com.buu.se.duanrestaurant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by thamrongs on 3/4/15 AD.
 */
public class ProfileFragment extends Fragment {
    private String fullname, personid;
    private ImageView profilepic;
    private TextView name, tel, email, id;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);

        SharedPreferences persondata = this.getActivity().getSharedPreferences("persondata", Context.MODE_PRIVATE);
        name = (TextView) rootView.findViewById(R.id.txv_name);
        tel = (TextView) rootView.findViewById(R.id.txv_tel);
        email = (TextView) rootView.findViewById(R.id.txv_email);
        id = (TextView) rootView.findViewById(R.id.txt_id);
        profilepic = (ImageView) rootView.findViewById(R.id.imv_profile);

        fullname = persondata.getString("fname", "") + " " + persondata.getString("lname", "");
        name.setText(fullname);
        tel.setText(persondata.getString("tel", ""));
        email.setText(persondata.getString("email", ""));
        Integer personidu = persondata.getInt("userid", 0);
        personid = String.format("%06d", personidu);
        id.setText(personid);

        String picurl = persondata.getString("picurl", "");
        profilepic.setImageResource(R.mipmap.ic_launcher);
        new DownloadImageTask(profilepic).execute(picurl);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}