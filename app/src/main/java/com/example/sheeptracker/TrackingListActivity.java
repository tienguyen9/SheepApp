package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import utility.TripRVAdapter;

public class TrackingListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseHelper databaseHelper;
    ArrayList<String> tripIDs, tripMaps, tripDateTimes;
    TripRVAdapter tripRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_list);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.rc_trackingllist);
        tripIDs = new ArrayList<>();
        tripMaps = new ArrayList<>();
        tripDateTimes = new ArrayList<>();

        fillArrayLists();

        tripRVAdapter = new TripRVAdapter(this, tripIDs, tripMaps, tripDateTimes);
        recyclerView.setAdapter(tripRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void fillArrayLists() {
        Cursor c = databaseHelper.readTripData();
        c.moveToLast();
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "No trips registered", Toast.LENGTH_SHORT).show();
        } else {
            //to include the latest index
            tripIDs.add(c.getString(0));
            tripMaps.add(databaseHelper.getMapName(Integer.parseInt(c.getString(1))));
            tripDateTimes.add(c.getString(2));
            while (c.moveToPrevious()) {
                tripIDs.add(c.getString(0));
                tripMaps.add(databaseHelper.getMapName(Integer.parseInt(c.getString(1))));
                tripDateTimes.add(c.getString(2));
            }
        }
    }
}
