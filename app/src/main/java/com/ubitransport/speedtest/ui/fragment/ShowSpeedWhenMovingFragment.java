package com.ubitransport.speedtest.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubitransport.speedtest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSpeedWhenMovingFragment extends Fragment {


    public ShowSpeedWhenMovingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_speed_when_moving, container, false);
    }

}
