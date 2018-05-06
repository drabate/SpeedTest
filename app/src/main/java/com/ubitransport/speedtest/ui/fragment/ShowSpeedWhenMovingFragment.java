package com.ubitransport.speedtest.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubitransport.speedtest.R;
import com.ubitransport.speedtest.databinding.FragmentShowAverageSpeedBinding;
import com.ubitransport.speedtest.databinding.FragmentShowSpeedWhenMovingBinding;
import com.ubitransport.speedtest.model.SpeedModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSpeedWhenMovingFragment extends Fragment {

    private SpeedModel speedModel = new SpeedModel();

    public ShowSpeedWhenMovingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShowSpeedWhenMovingBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_show_speed_when_moving, container, false);
        binding.setSpeedModel(speedModel);
        return binding.getRoot();
    }

    /***
     * set the message displayed in the speed page
     * @param speed the message with ou without the speed
     */
    public void setSpeedMessage(String speed) {
        speedModel.setSpeed(speed);
    }

}
