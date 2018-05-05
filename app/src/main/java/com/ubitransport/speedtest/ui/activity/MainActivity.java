package com.ubitransport.speedtest.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ubitransport.speedtest.R;
import com.ubitransport.speedtest.ui.fragment.ShowAverageSpeedFragment;
import com.ubitransport.speedtest.ui.fragment.ShowSpeedWhenMovingFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_LOCATION = 120;
    private ActionBar actionBar;
    private ShowAverageSpeedFragment showAverageSpeedFragment;
    private ShowSpeedWhenMovingFragment showSpeedWhenMovingFragment;
    private CustomLocationListener customLocationListener = new CustomLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAverageSpeedFragment = new ShowAverageSpeedFragment();
        showSpeedWhenMovingFragment = new ShowSpeedWhenMovingFragment();

        initializeFirstScreen();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        }
        else{
            initializeLocationManager();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_LOCATION){

            if (grantResults.length < 2
                    || grantResults[0] == PackageManager.PERMISSION_DENIED
                    || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                showAverageSpeedFragment.setAverageSpeedMessage(getString(R.string.no_location_access));
            }
            else{
                initializeLocationManager();
            }
        }
    }


    private void initializeFirstScreen() {

            //We first show the average speed page with a simple message
            if (getSupportActionBar() != null) {
                actionBar = getSupportActionBar();
                changePageTitle(R.string.title_when_not_moving);
            }
            changeFragment(showAverageSpeedFragment);
            showAverageSpeedFragment.setAverageSpeedMessage(getString(R.string.still_no_average));

    }

    @SuppressLint("MissingPermission")
    private void initializeLocationManager(){
        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,          // 1-second interval.
                    5,             // 5 meters.
                    customLocationListener);

            final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                // Build an alert dialog here that requests that the user enable
                // the location services, then when the user clicks the "OK" button,
                // call enableLocationSettings()
            }
        }
    }

    /***
     * change the page title (when changing fragment)
     * @param title the string resource corresponding to the title
     */
    private void changePageTitle(int title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            showAverageSpeedFragment.setAverageSpeedMessage("vitesse trouvée : " + location.getSpeed());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
