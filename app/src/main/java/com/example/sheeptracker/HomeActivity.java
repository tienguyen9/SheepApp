package com.example.sheeptracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class HomeActivity extends AppCompatActivity {

    Button btnOnline, btnOffline, btnTest, btnDrive;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO REMOVE DELETE
        //this.deleteDatabase("TrackHistory.db");
        databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkLocationPermission();


        btnOnline = (Button)findViewById(R.id.btn_online);
        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, OnlineMapActivity.class));
            }
        });

        btnOffline = (Button)findViewById(R.id.btn_offline);
        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, OfflineListActivity.class));
            }
        });
        btnTest = (Button)findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TrackingListActivity.class));
            }
        });
        btnDrive = (Button)findViewById(R.id.btn_drive);
        btnDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DriveActivity.class));
            }
        });

    }

    public void checkLocationPermission() {
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean hasFilePermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        Log.d("perm", hasFilePermission + "____" + hasLocationPermission);

        if (!hasLocationPermission || !hasFilePermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_LOCATION_CODE);
        }
    }

}
