package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import utility.MapRVAdapter;

public class OfflineListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_list);
        try {
            initListItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initListItems() throws IOException {
        File downloadFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/Documents/SheepTracker");
        Log.d("asdasd", Environment.getExternalStorageDirectory().getPath() + "/Documents/SheepTracker");
        String[] folderList = downloadFolder.list();
        if (folderList != null) {
            ArrayList<String> folders = new ArrayList<String>();
            for (String t : folderList){
                folders.add(t);
            }
            initRecyclerView(folders);
        }else{
            Toast.makeText(this, "No maps have been downloaded yet", Toast.LENGTH_LONG).show();
        }

    }

    private void initRecyclerView(ArrayList<String> folders){
        RecyclerView recyclerView = findViewById(R.id.rv_offlines);
        MapRVAdapter adapter = new MapRVAdapter(folders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
