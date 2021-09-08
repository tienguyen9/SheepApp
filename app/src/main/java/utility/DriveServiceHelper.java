package utility;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import utility.DatabaseHelper;

public class DriveServiceHelper {
    private final Executor  executor = Executors.newSingleThreadExecutor();
    private Drive driveService;

    public DriveServiceHelper(Drive driveService) {
        this.driveService = driveService;
    }

    public Task<String> uploadDBtoDrive(String filePath, DatabaseHelper databaseHelper) {
        return Tasks.call(executor, () -> {
            File fileMetaData = new File();
            fileMetaData.setName("TrackHistory.db");

            java.io.File fileContent = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/db", fileContent);
            File myFile = new File();
            String fileID = databaseHelper.getDriveID();
            if(fileID != null) {
                try{
                    myFile = driveService.files().update(fileID, myFile, mediaContent).execute();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try{
                    myFile = driveService.files().create(fileMetaData, mediaContent).execute();
                    fileID = myFile.getId();
                    databaseHelper.addDrive(fileID);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (myFile == null){
                throw new IOException("sda");
            }
            Log.d("FILEID", myFile.getId() + "___");
            return fileID;
        });
    }

}
