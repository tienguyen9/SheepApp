package com.example.sheeptracker;


import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

public class Constants {
    static final int LOCATION_SERVICE_ID = 175;
    static final String START_LOCATION_SERVICE = "startLocationService";
    static final String STOP_LOCATION_SERVICE = "stopLocationService";
    static final int DEFAULT_UPDATE_INTERVAL = 25;
    static final int FAST_UPDATE_INTERTVAL = 20;
    static final int REQUEST_ACTIVITY_CODE = 100;
    static final int REQUEST_LOCATION_CODE = 101;
    static final int REQUEST_READ_FILE_CODE = 102;
    static final String[] earMarkColors = {"Red", "Blue", "Green", "Yellow"};

    //Runs javascript without callback
    public static void runJavascript(Context context, WebView webView, String jsCode){
        try {
            webView.evaluateJavascript("javascript: " + jsCode, null);
        } catch (Exception e) {
            Toast.makeText(context, "Javascript thread is not finished, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    //works for both latitude and longitude. NW_latitude or NW_longitude always have to be the first parameter
    public static double getCenterCoordinate(double nw, double se) {
        double difference = nw - se;
        double center = nw - difference;
        return center;
    }
}
