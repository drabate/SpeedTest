package com.ubitransport.speedtest.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubitransport.speedtest.R;
import com.ubitransport.speedtest.databinding.FragmentShowAverageSpeedBinding;
import com.ubitransport.speedtest.model.SpeedModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowAverageSpeedFragment extends Fragment {

    private SpeedModel speedModel = new SpeedModel();

    public ShowAverageSpeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentShowAverageSpeedBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_show_average_speed, container, false);
        binding.setSpeedModel(speedModel);
        return binding.getRoot();
    }

    /***
     * set the message displayed in the average speed page
     * @param speed the message with ou without the average speed
     */
    public void setAverageSpeedMessage(String speed) {
        speedModel.setAverageSpeed(speed);
    }

}
