package com.example.sheeptracker;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import utility.Constants;
import utility.DatabaseHelper;
import utility.DownloadTiles;
import utility.MapTitleDialog;


public class OnlineMapActivity extends MapActivity implements MapTitleDialog.MapTitleDialogListener{

    WebView webView;
    TextView tv_lat, tv_lon;
    Button btn_updateLocation, btn_selectArea, btn_cancelSelect;
    boolean initialLoad;
    boolean showingMapRectangle;

    double latitude;
    double longitude;

    //Northwest and southeast of map area to download
    double NW_lat;
    double NW_lon;
    double SE_lat;
    double SE_lon;

    String mapRectangleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_map);
        startLocationService();
        initialLoad = true;

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);

        btn_selectArea = findViewById(R.id.btn_selectArea);
        btn_cancelSelect = findViewById(R.id.btn_cancelSelect);
        btn_updateLocation = findViewById(R.id.btn_updateLocation);
        showingMapRectangle = false;
        btn_selectArea.setText("Select area to download");
        btn_cancelSelect.setVisibility(View.GONE);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        btn_updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentLocation();
            }
        });

        btn_selectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingMapRectangle){
                    getRectangleNorthWest();
                    getRectangleSouthEast();
                    try {
                        openDialog();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not update, try again", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showMapRectangle();
                }
            }
        });

        btn_cancelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMapRectangle();
            }
        });

        webView.loadUrl("file:///android_asset/map.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Constants.runJavascript(getApplicationContext(), webView, "loadOnlineMap(" + latitude + ", " + longitude + ")");
                Constants.runJavascript(getApplicationContext(), webView, "showCurrentPosition(" + latitude + ", " + longitude + ")");
            }
        });

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Default value set to 0.0
            latitude = intent.getDoubleExtra("latitude", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);
            tv_lat.setText(String.valueOf(latitude));
            tv_lon.setText(String.valueOf(longitude));

            //Run these scripts again since they might fail in onCreate. This ensures that the map will be loaded regardless.
            if (initialLoad){
                Constants.runJavascript(getApplicationContext(), webView, "loadOnlineMap(" + latitude + ", " + longitude + ")");
                Constants.runJavascript(getApplicationContext(), webView, "showCurrentPosition(" + latitude + ", " + longitude + ")");
                initialLoad = false;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // This registers mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Location-broadcast"));
    }

    private void updateCurrentLocation() {
        Constants.runJavascript(this, webView, "showCurrentPosition(" + latitude + ", " + longitude + ")");
    }



    private void hideMapRectangle() {
        showingMapRectangle = false;
        btn_selectArea.setText("Select area to download");
        btn_cancelSelect.setVisibility(View.GONE);
        Constants.runJavascript(this, webView, "hideMapRectangle()");
    }


    private void showMapRectangle() {
        showingMapRectangle = true;
        btn_selectArea.setText("Download rectangle area");
        btn_cancelSelect.setVisibility(View.VISIBLE);
        Constants.runJavascript(this, webView, "chooseMapRectangle()");
    }

    public void getRectangleNorthWest(){
        webView.evaluateJavascript("javascript:GetNorthWestCorner()",new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String NW_lat_lon_string) {
                //The string comes from JS with quotation marks at the start and at the end, to trim off with substring
                String[] NW_lat_lon = NW_lat_lon_string.split("_");
                NW_lat = Double.parseDouble(NW_lat_lon[0].substring(1));
                NW_lon = Double.parseDouble(NW_lat_lon[1].substring(0, NW_lat_lon[1].length() - 1));

            }
        });
    }

    private void getRectangleSouthEast() {
        webView.evaluateJavascript("javascript:GetSouthEastCorner()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String SE_lat_lon_string) {
                String[] SE_lat_lon = SE_lat_lon_string.split("_");
                SE_lat = Double.parseDouble(SE_lat_lon[0].substring(1));
                SE_lon = Double.parseDouble(SE_lat_lon[1].substring(0, SE_lat_lon[1].length() - 1));
            }
        });
    }

    public class MyRunnable implements Runnable {

        public void run(){
            Log.d("Generic","Running in the Thread " +
                    Thread.currentThread().getId());

            DownloadTiles downloadTiles = new DownloadTiles();
            try {
                stopLocationService();
                downloadTiles.saveMap(getApplicationContext(), mapRectangleTitle.toLowerCase(), NW_lat, NW_lon, SE_lat, SE_lon);
                //TODO remove delete later
                //getApplicationContext().deleteDatabase("TrackHistory.db");
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.addMap(mapRectangleTitle.toLowerCase(), NW_lat, NW_lon, SE_lat, SE_lon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openDialog() {
        MapTitleDialog exampleDialog = new MapTitleDialog();
        exampleDialog.show(getSupportFragmentManager(), "Map Title Dialog");
    }


    @Override
    public void handleTitle(String mapTitle) {
        mapRectangleTitle = mapTitle;
        Thread thread = new Thread(new MyRunnable());
        thread.start();
    }


}
