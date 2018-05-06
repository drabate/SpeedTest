package com.ubitransport.speedtest.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ubitransport.speedtest.R;
import com.ubitransport.speedtest.ui.fragment.ShowAverageSpeedFragment;
import com.ubitransport.speedtest.ui.fragment.ShowSpeedWhenMovingFragment;
import com.ubitransport.speedtest.utils.MathUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_LOCATION = 120;
    private ActionBar actionBar;
    private ShowAverageSpeedFragment showAverageSpeedFragment;
    private ShowSpeedWhenMovingFragment showSpeedWhenMovingFragment;
    private CustomLocationCallback customLocationCallback = new CustomLocationCallback();
    private FusedLocationProviderClient fusedLocationClient;

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
        } else {
            initializeLocationManager();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {

            if (grantResults.length < 2
                    || grantResults[0] == PackageManager.PERMISSION_DENIED
                    || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                showAverageSpeedFragment.setAverageSpeedMessage(getString(R.string.no_location_access));
            } else {
                initializeLocationManager();
            }
        }
    }


    private void initializeFirstScreen() {

        //We first show the average speed page with a simple message
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
        }
        changeFragment(showAverageSpeedFragment, R.string.title_when_not_moving);
        showAverageSpeedFragment.setAverageSpeedMessage(getString(R.string.still_no_average));

    }

    @SuppressLint("MissingPermission")
    private void initializeLocationManager() {

        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                displayPromptToEnableGPS();
            }
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                customLocationCallback,
                null);

    }

    public void displayPromptToEnableGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = getString(R.string.message_enable_gps);

        builder.setMessage(message)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int id) {
                                startActivity(new Intent(action));
                                dialogInterface.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        });
        builder.create().show();
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

    /***
     * Change the page
     * @param fragment the fragment used
     * @param title the title of the activity to change
     */
    private void changeFragment(Fragment fragment, int title) {
        changePageTitle(title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private class CustomLocationCallback extends LocationCallback {

        List<Long> speedsList = new ArrayList<>();
        private Location oldLocation;

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            long speed = 0;
            //If we already have a location, we calculate the speed
            if (oldLocation != null) {
                double timeDifference = (double) (locationResult.getLastLocation().getTime() - oldLocation.getTime());
                double distance = (double) oldLocation.distanceTo(locationResult.getLastLocation());
                speed = Math.round(((distance / (double) 1000) / (timeDifference / (double) 3600000)));
            }

            oldLocation = locationResult.getLastLocation();

            Log.i("Debug", "vitesse précise " + speed);
            if (speed > 0) {
                changeFragment(showSpeedWhenMovingFragment, R.string.title_when_moving);
                showSpeedWhenMovingFragment.setSpeedMessage(
                        String.format(getString(R.string.format_speed), String.valueOf(speed)));
                speedsList.add(speed);
            } else {
                changeFragment(showAverageSpeedFragment, R.string.title_when_not_moving);
                long averageSpeed = MathUtils.calculateAverage(speedsList);
                if (averageSpeed > 0) {
                    showAverageSpeedFragment.setAverageSpeedMessage(
                            String.format(getString(R.string.format_speed),
                                    String.valueOf(averageSpeed)));
                }
                speedsList = new ArrayList<>();
            }
        }

    }

}
