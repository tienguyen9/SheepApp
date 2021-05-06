package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class TrackingHistoryActivity extends AppCompatActivity {
    int tripID;
    DatabaseHelper databaseHelper;
    ArrayList<double[]> footprints, sheepMarkers, predatorMarkers;
    WebView webView;
    double NW_lat, NW_lon, SE_lat, SE_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_history);

        Intent intent = getIntent();
        tripID = intent.getIntExtra("tripID", 0);

        databaseHelper = new DatabaseHelper(this);
        footprints = new ArrayList<>();
        sheepMarkers = new ArrayList<>();
        predatorMarkers = new ArrayList<>();

        fillFootprintList();
        fillSheepMarkerList();
        fillPredatorMarkerList();
        fillMapLatLngs();

        final double centerLat = Constants.getCenterCoordinate(NW_lat, SE_lat);
        final double centerLon = Constants.getCenterCoordinate(NW_lon, SE_lon);
        int mapID = databaseHelper.getMapID(tripID);
        final String mapName = databaseHelper.getMapName(mapID);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/map.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Constants.runJavascript(getApplicationContext(), webView, "loadOfflineMap('"+ mapName +"', " + centerLat + ", " + centerLon + ", 12, 15)");
                int i = 0;
                for (double[] f : footprints) {
                    String pointID = "p" + i;
                    Constants.runJavascript(getApplicationContext(), webView, pointID + "  = addPoint(" + f[0] + ", " + f[1] + ")");
                    if (i>0) {
                        int previous = i-1;
                        String previousPointID = "p" + previous;
                        Constants.runJavascript(getApplicationContext(), webView, "drawLineBetweenPoints(" + previousPointID  + ", " + pointID + ")");
                    }
                    i++;
                }

                for (double[] s : sheepMarkers) {
                    Log.d("asdsd", s[0] + " " + s[1] + " " + s[2]);
                    Constants.runJavascript(getApplicationContext(), webView, "registerSheepPointLatLng(" + s[1] + ", " + s[2] + ")");
                    int sheepID = (int) Math.round(s[0]);
                    Cursor c = databaseHelper.readSpottedFromLatLon(sheepID);
                    while (c.moveToNext()) {
                        Constants.runJavascript(getApplicationContext(), webView, "drawLineSheepPositionLatLng("
                                + s[1] + ", " + s[2] + ", " + c.getDouble(0) + ", " + c.getDouble(1) + ")");
                    }
                }

                for (double[] p : predatorMarkers) {
                    Log.d("asdsd", p[0] + " " + p[1]);
                    Constants.runJavascript(getApplicationContext(), webView, "registerPredatorPointLatLng(" + p[0] + ", " + p[1] + ")");
                }

            }
        });


    }

    private void fillFootprintList() {
        Cursor c = databaseHelper.readFootstepData(tripID);
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "No footprints registered", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                double[] latlon = {c.getDouble(1), c.getDouble(2)};
                footprints.add(latlon);
            }
        }
    }

    private void fillSheepMarkerList() {
        Cursor c = databaseHelper.readSheepLatLonTrip(tripID);
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "No sheep registered", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                //id, lat, lon
                double[] sheepMarker = {c.getDouble(0), c.getDouble(1), c.getDouble(2)};
                sheepMarkers.add(sheepMarker);
            }
        }
    }

    private void fillPredatorMarkerList() {
        Cursor c = databaseHelper.readPredatorLatLons();
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "No predators registered", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                //lat, lon
                double[] sheepMarker = {c.getDouble(0), c.getDouble(1)};
                predatorMarkers.add(sheepMarker);
            }
        }
    }

    //extracting corners to find the center of the map
    private void fillMapLatLngs() {
        Cursor c = databaseHelper.readMapData(tripID);
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "No map registered", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                NW_lat = c.getDouble(2);
                NW_lon = c.getDouble(3);
                SE_lat = c.getDouble(4);
                SE_lon = c.getDouble(5);
            }
        }
    }

}
