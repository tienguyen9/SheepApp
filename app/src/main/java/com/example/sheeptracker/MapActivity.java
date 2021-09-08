package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import utility.Constants;
import utility.LocationService;

public abstract class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationService();
    }


    public void startLocationService() {
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (hasLocationPermission) {
            Intent intent = new Intent(this, LocationService.class);
            intent.setAction(Constants.START_LOCATION_SERVICE);
            startService(intent);


        } else {
            Log.d("MAN", Manifest.permission.FOREGROUND_SERVICE);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


    }

    public void stopLocationService() {
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (hasLocationPermission) {

            Intent intent = new Intent(this, LocationService.class);
            intent.setAction(Constants.STOP_LOCATION_SERVICE);
            startService(intent);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }


}