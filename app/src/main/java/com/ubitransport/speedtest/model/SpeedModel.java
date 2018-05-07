package com.ubitransport.speedtest.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ubitransport.speedtest.BR;


public class SpeedModel extends BaseObservable {

    private String speed;
    private String averageSpeed;

@Bindable
    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
        notifyPropertyChanged(BR.speed);
    }

    @Bindable
    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
        notifyPropertyChanged(BR.averageSpeed);
    }
}
