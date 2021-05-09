package com.example.sheeptracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;


public class OfflineMapActivity extends MapActivity {

    WebView webView;
    TextView tv_lat, tv_lon;
    Button btn_trackPath, btn_sheepMarker, btn_predatorMarker, btn_deadSheep;
    ArrayList<double[]> footprintPoints = new ArrayList<>();
    ArrayList<String> footprintLabels = new ArrayList<>();

    String folder;
    int[] sheepNumbers;
    int[] tieNumbers;
    boolean[] earMarks;
    int existing;
    double latitude;
    double longitude;
    boolean trackingPath;
    boolean showingSheepMarker;
    boolean showingPredatorMarker;
    boolean initialLoad;

    double latSheep;
    double lonSheep;
    private int tripID;
    private int mapID;
    boolean tripRegisteredInDB;

    private DatabaseHelper databaseHelper;
    double NW_lat, NW_lon, SE_lat, SE_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);

        Intent intent = getIntent();
        folder = intent.getStringExtra("folder");
        NW_lat = intent.getDoubleExtra("NW_lat", 0.0);
        NW_lon = intent.getDoubleExtra("NW_lon", 0.0);
        SE_lat = intent.getDoubleExtra("SE_lat", 0.0);
        SE_lon = intent.getDoubleExtra("SE_lon", 0.0);

        Log.d("SFR", NW_lat + " " + NW_lon + " " + SE_lat + " " + SE_lon);

        tripRegisteredInDB = false;
        //TODO remove delete later
        //getApplicationContext().deleteDatabase("TrackHistory.db");


        Toast.makeText(this, folder, Toast.LENGTH_LONG).show();
        startLocationService();


        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        btn_trackPath = findViewById(R.id.btn_trackPath);
        btn_sheepMarker = findViewById(R.id.btn_sheepMarker);
        btn_sheepMarker.setVisibility(View.GONE);
        btn_predatorMarker = findViewById(R.id.btn_predatorMarker);
        btn_predatorMarker.setVisibility((View.GONE));
        btn_deadSheep = findViewById(R.id.btn_deadSheep);
        btn_deadSheep.setVisibility((View.GONE));
        trackingPath = false;
        showingSheepMarker = false;
        initialLoad = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Location-broadcast"));

        final double centerLat = Constants.getCenterCoordinate(NW_lat, SE_lat);
        final double centerLon = Constants.getCenterCoordinate(NW_lon, SE_lon);


        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        btn_trackPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackingPath){
                    pauseTrackPath();
                }else{
                    resumeTrackPath();
                }
            }
        });

        btn_sheepMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(showingSheepMarker){
                    Intent registerIntent = new Intent(getApplicationContext(), RegisterSheepActivity.class);
                    registerIntent.putExtra("existing", 0);
                    startActivityForResult(registerIntent, Constants.SHEEP_REQUEST_ACTIVITY_CODE);
                    Log.d("click", "D");
                } else if (showingPredatorMarker) {
                    hidePredatorMarker();
                    Log.d("click", "E");
                }
                else{
                    showSheepMarker();
                    Log.d("click", "F");
                }

            }
        });

        btn_deadSheep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDeadSheep();
            }
        });

        btn_predatorMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingPredatorMarker) {
                    Intent registerIntent = new Intent(getApplicationContext(), RegisterPredatorActivity.class);
                    startActivityForResult(registerIntent, Constants.PREDATOR_REQUEST_ACTIVITY_CODE);
                    Log.d("click", "A");
                } else if(showingSheepMarker) {
                    hideSheepMarker();
                    Log.d("click", "B");
                }
                else {
                    showPredatorMarker();
                    Log.d("click", "C");
                }

            }
        });


        webView.loadUrl("file:///android_asset/map.html");
        webView.addJavascriptInterface(new JavaScriptInterface(this), "AndroidFunction");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Constants.runJavascript(getApplicationContext(), webView, "loadOfflineMap('"+ folder +"', " + centerLat + ", " + centerLon + ", 12, 15)");
            }
        });


    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void editSheepRegister(int sheepID){
            int[] existingSheepData = checkExistingSheep(sheepID);
            Intent registerIntent = new Intent(getApplicationContext(), RegisterSheepActivity.class);
            Log.d("asfd", existingSheepData[0] + " ");
            registerIntent.putExtra("total_spotted", existingSheepData[0]);
            registerIntent.putExtra("blacks", existingSheepData[1]);
            registerIntent.putExtra("whites", existingSheepData[2]);
            registerIntent.putExtra("red_ties", existingSheepData[3]);
            registerIntent.putExtra("yellow_ties", existingSheepData[4]);
            registerIntent.putExtra("green_ties", existingSheepData[5]);
            registerIntent.putExtra("blue_ties", existingSheepData[6]);
            registerIntent.putExtra("no_ties", existingSheepData[7]);
            registerIntent.putExtra("red_ear", existingSheepData[8]);
            registerIntent.putExtra("yellow_ear", existingSheepData[9]);
            registerIntent.putExtra("green_ear", existingSheepData[10]);
            registerIntent.putExtra("existing", 1);
            registerIntent.putExtra("sheepID", sheepID);
            startActivityForResult(registerIntent, Constants.SHEEP_REQUEST_ACTIVITY_CODE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SHEEP_REQUEST_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                sheepNumbers = data.getIntArrayExtra("SheepNumbers");
                tieNumbers = data.getIntArrayExtra("TieNumbers");
                earMarks = data.getBooleanArrayExtra("EarMarks");
                existing = data.getIntExtra("Existing",0);
                int registerSheepID = data.getIntExtra("SheepID", -1);
                if(registerSheepID == -1) {
                    registerSheep();
                    Log.d("regg", "r");
                } else {
                    updateSheep(registerSheepID);
                    Log.d("update", "u");
                }

                hideSheepMarker();
            }
        } else if (requestCode == Constants.PREDATOR_REQUEST_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String predatorType = data.getStringExtra("PredatorType");
                registerPredator(predatorType);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("Result", "canceled");
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Default value set to 0.0
            latitude = intent.getDoubleExtra("latitude", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);
            tv_lat.setText(String.valueOf(latitude));
            tv_lon.setText(String.valueOf(longitude));

            if (initialLoad) {
                Constants.runJavascript(getApplicationContext(), webView, "loadOfflineMap('"+ folder +"', " + latitude + ", " + longitude + ", 12, 14)");
                initialLoad = false;
            }
            trackPath();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // This registers mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Location-broadcast"));
    }


    private static boolean notAproxEqual(double a, double b){
        return Math.abs(a-b) > 1e-4;
    }


    private boolean addFootprintPoint(){
        double[] coordinates = {latitude, longitude};
        if (footprintPoints.size() > 0) {
            double[] lastPoint = footprintPoints.get(footprintPoints.size() - 1);
            if (notAproxEqual(lastPoint[0], latitude) && notAproxEqual(lastPoint[1], longitude)) {
                footprintPoints.add(coordinates);
                String pointID = "point" + footprintPoints.size();
                footprintLabels.add(pointID);
                Constants.runJavascript(this, webView, pointID + "  = addPoint(" + latitude + ", " + longitude + ")");
                databaseHelper.addFootprint(latitude, longitude, tripID);
                Log.d("SDFDSF", "heiheiheihei");
                return true;
            }
        }else{
            footprintPoints.add(coordinates);
            String pointID = "point" + footprintPoints.size();
            footprintLabels.add(pointID);
            Constants.runJavascript(this, webView, pointID + "  = addFirstPoint(" + latitude + ", " + longitude + ")");
            databaseHelper.addFootprint(latitude, longitude, tripID);
            Log.d("SDFDSF", "heihei");
            return true;
        }

        return false;
    }

    private void drawLine(String p1, String p2){
        if(footprintLabels.size() > 1){
            Constants.runJavascript(this, webView, "drawLineBetweenPoints(" +p1 + ", " + p2 + ")");
        }
    }


    private void trackPath(){
        if(trackingPath){
            boolean newPoint;
            String p1;
            String p2;
            newPoint = addFootprintPoint();
            if (newPoint){
                if (footprintLabels.size() > 1){
                    p1 = footprintLabels.get(footprintLabels.size() -1);
                    p2 = footprintLabels.get(footprintLabels.size() -2);
                    drawLine(p1, p2);
                }
            }
        }
    }


    private void resumeTrackPath(){
        if (!tripRegisteredInDB) {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            databaseHelper.close();
            mapID = databaseHelper.getMapID(folder);
            databaseHelper.addTrip(mapID);
            tripID = databaseHelper.getLastTripID();
            Log.d("trip", Integer.toString(tripID) + ": TRIP ID");
            tripRegisteredInDB = true;
        }
        btn_sheepMarker.setVisibility(View.VISIBLE);
        btn_predatorMarker.setVisibility(View.VISIBLE);
        addFootprintPoint();
        Toast.makeText(this, "Started tracking path", Toast.LENGTH_LONG).show();

        trackingPath = true;
        btn_trackPath.setText("Stop tracking");
    }


    private void pauseTrackPath(){
        trackingPath = false;
        btn_trackPath.setText("Resume tracking");
    }

    private void showSheepMarker() {
        showingSheepMarker = true;
        if (latitude != 0.0 && longitude != 0.0){
            Constants.runJavascript(this, webView, "moveSheepMarker(" + latitude + ", " +  longitude + ")");
        }else{
            Toast.makeText(this, "Wait for location service", Toast.LENGTH_LONG).show();
        }
        btn_deadSheep.setVisibility(View.VISIBLE);
        btn_sheepMarker.setText("Confirm sheep location");
        btn_predatorMarker.setText("Cancel Sheep");
    }


    private void hideSheepMarker() {
        showingSheepMarker = false;
        Constants.runJavascript(this, webView, "hideSheepMarker()");
        btn_deadSheep.setVisibility(View.GONE);
        btn_sheepMarker.setText("Register Sheep");
        btn_predatorMarker.setText("Register Predator");
    }

    private void showPredatorMarker() {
        showingPredatorMarker = true;
        if (latitude != 0.0 && longitude != 0.0){
            Constants.runJavascript(this, webView, "movePredatorMarker(" + latitude + ", " +  longitude + ")");
        }else{
            Toast.makeText(this, "Wait for location service", Toast.LENGTH_LONG).show();
        }
        btn_predatorMarker.setText("Confirm predator location");
        btn_sheepMarker.setText("Cancel Predator");
    }

    private void hidePredatorMarker() {
        showingPredatorMarker = false;
        Constants.runJavascript(this, webView, "hidePredatorMarker()");
        btn_predatorMarker.setText("Register Predator");
        btn_sheepMarker.setText("Register Sheep");
    }

    private void registerPredator(String predatorType) {
        webView.evaluateJavascript("javascript:getPredatorMarkerPos()",new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String lat_lon_string) {
                //recieves latitude and longitude as a String with the following format from JS: "63.3923_46.2134"
                String[] parts = lat_lon_string.split("_");

                double latPredator = Double.parseDouble(parts[0].substring(1));
                double lonPredator = Double.parseDouble(parts[1].substring(0, parts[1].length()-1));

                Constants.runJavascript(getApplicationContext(), webView, "registerPredatorPointMarker()");
                databaseHelper.addPredator(latPredator, lonPredator, predatorType, tripID);
                hidePredatorMarker();

            }
        });
        Log.d("RedPRED", "avvb"+predatorType);
    }

    private void registerDeadSheep() {
        webView.evaluateJavascript("javascript:getSheepMarkerPos()",new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String lat_lon_string) {
                //recieves latitude and longitude as a String with the following format from JS: "63.3923_46.2134"
                String[] parts = lat_lon_string.split("_");

                double latDeadSheep = Double.parseDouble(parts[0].substring(1));
                double lonDeadSheep = Double.parseDouble(parts[1].substring(0, parts[1].length()-1));

                Constants.runJavascript(getApplicationContext(), webView, "registerDeadPointMarker()");
                databaseHelper.addDeadSheep(latDeadSheep, lonDeadSheep, tripID);
                hideSheepMarker();

            }
        });
    }

    private int registerSheep() {
        final int[] sheepID = new int[1];
        webView.evaluateJavascript("javascript:getSheepMarkerPos()",new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String lat_lon_string) {
                //recieves latitude and longitude as a String with the following format from JS: "63.3923_46.2134"
                String[] parts = lat_lon_string.split("_");

                latSheep = Double.parseDouble(parts[0].substring(1));
                lonSheep = Double.parseDouble(parts[1].substring(0, parts[1].length()-1));

                int totalNumber = sheepNumbers[0];
                int blackNumber  = sheepNumbers[1];
                int whiteNumber = sheepNumbers[2];
                int redEarMarkState;
                int yellowEarMarkState;
                int greenEarMarkState;
                if (earMarks[0]) {
                    redEarMarkState = 1;
                } else {
                    redEarMarkState = 0;
                }
                if (earMarks[1]) {
                    yellowEarMarkState = 1;
                } else {
                    yellowEarMarkState = 0;
                }

                if (earMarks[2]) {
                    greenEarMarkState = 1;
                } else {
                    greenEarMarkState = 0;
                }
                int[] earMarkStates = {redEarMarkState, yellowEarMarkState, greenEarMarkState};

                sheepID[0] = databaseHelper.addSheep(latSheep, lonSheep, latitude, longitude, totalNumber, blackNumber, whiteNumber, tieNumbers, earMarkStates, tripID);
                Constants.runJavascript(getApplicationContext(), webView, "registerSheepPointMarker(" + sheepID[0] + ")");
                Constants.runJavascript(getApplicationContext(), webView, "drawLineSheepPositionPoint("
                        + footprintLabels.get(footprintLabels.size()-1) + ", " + latSheep + ", " +  lonSheep + ")");
                hideSheepMarker();

            }
        });
        return sheepID[0];
    }

    public void updateSheep(final int sheepID) {
        int totalNumber = sheepNumbers[0];
        int blackNumber  = sheepNumbers[1];
        int whiteNumber = sheepNumbers[2];
        //0 is false, 1 is true
        int redEarMarkState;
        int yellowEarMarkState;
        int greenEarMarkState;
        if (earMarks[0]) {
            redEarMarkState = 1;
        } else {
            redEarMarkState = 0;
        }
        if (earMarks[1]) {
            yellowEarMarkState = 1;
        } else {
            yellowEarMarkState = 0;
        }

        if (earMarks[2]) {
            greenEarMarkState = 1;
        } else {
            greenEarMarkState = 0;
        }
        int[] earMarkStates = {redEarMarkState, yellowEarMarkState, greenEarMarkState};
        Cursor c = databaseHelper.readSheepData(sheepID);
        double markerLat = 0.0;
        double markerLon = 0.0;
        while (c.moveToNext()) {
            markerLat = c.getDouble(1);
            markerLon = c.getDouble(2);
        }
        databaseHelper.updateSheep(latitude, longitude, totalNumber, blackNumber, whiteNumber, tieNumbers, earMarkStates, sheepID);
        Constants.runJavascript(getApplicationContext(), webView, "drawLineSheepPositionLatLng("
                + markerLat+ ", " + markerLon + ", " + latitude + ", " +  longitude + ")");
    }


    private int[] checkExistingSheep(int sheepID) {
        Cursor c = databaseHelper.readSheepData(sheepID);
        int[] sheepData = new int[0];
        if (c.getCount() == 0 ) {
            Toast.makeText(this, "new sheep registering", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                sheepData= new int[]{c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getInt(9), c.getInt(10), c.getInt(11), c.getInt(12), c.getInt(13), c.getInt(14), c.getInt(15)};
            }
        }
        return sheepData;
    }


}
