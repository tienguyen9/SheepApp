package com.example.sheeptracker;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor  executor = Executors.newSingleThreadExecutor();
    private Drive driveService;

    public DriveServiceHelper(Drive driveService) {
        this.driveService = driveService;
    }

    public Task<String> createFilePDF(String filePath) {
        return Tasks.call(executor, () -> {

            File fileMetaData = new File();
            fileMetaData.setName("myFile");

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/pdf", file);
            File myFile = null;

            try{
                myFile = driveService.files().create(fileMetaData, mediaContent).execute();
            }catch (Exception e) {
                e.printStackTrace();
            }

            if (myFile == null){
                throw new IOException("sda");
            }
            return myFile.getId();
        });
    }

}
