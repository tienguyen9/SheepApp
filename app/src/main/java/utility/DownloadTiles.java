package utility;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DownloadTiles {

    int[] zoomLevels = {12,13,14,15};


//https://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=norges_grunnkart&zoom=11&x=1081&y=545
    public int[] geographicToTile(double latitude, double longitude, int zoom){
        double latRad = Math.toRadians(latitude);
        double n = Math.pow(2.0, zoom);
        double tan = Math.tan(latRad);
        double asinh = Math.log(tan + Math.sqrt(tan*tan + 1.0));
        double xTile = Math.round((longitude+180.0)/ 360.0 *n);
        double yTile = Math.round((1.0 - asinh / Math.PI) / 2.0 * n);
        int[] tile = {(int) xTile, (int) yTile};
        return tile;
    }

    //End cooridnates has to be south east of the start coordinates
    //TODO: Enter folder name as input later
    public void saveMap(Context context, String folder, double startLatGeo, double startLonGeo, double endLatGeo, double endLonGeo) throws Exception{
        for (int z : zoomLevels){
            int[] startTileCoordinates = geographicToTile(startLatGeo, startLonGeo, z);
            int[] endTileCoordinates = geographicToTile(endLatGeo, endLonGeo, z);

            int startLatTile = startTileCoordinates[0];
            int startLonTile = startTileCoordinates[1];
            int endLatTile = endTileCoordinates[0];
            int endLonTile = endTileCoordinates[1];


            for (int x = startLatTile; x<=endLatTile; x++){
                for (int y = startLonTile; y<=endLonTile; y++){
                    saveTile(context, folder, z, x, y);
                }
            }
        }

        System.out.println("Download complete");
    }



    public void saveTile(Context context, String folder, int z, int x, int y) throws Exception {
        boolean tileSaved = false;
        int retries = 0;
        int maxRetries = 10;
        while (!tileSaved && retries < maxRetries){
            try {
                String url = "https://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps" + "?layers=norges_grunnkart&zoom=" + z + "&x=" + x + "&y=" + y;
                DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Tile");
                request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                //System.out.println("RESPONSE:" + conn.getResponseCode());
                try{
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "/SheepTracker/" + folder + "/tile_" + "z=" + z + "_x=" + x + "_y=" + y + ".png");
                    downloadmanager.enqueue(request);
                    tileSaved = true;
                    Log.d("saved", "saved tile: " + "/tile_" + "z=" + z + "_x=" + x + "_y=" + y + ".png");
                } catch (Exception e) {
                    System.out.println("queue failed");
                    Log.d("saved", "failed");
                }

            } catch (Exception e) {
                System.out.println("Connection failed");
                Log.d("saved", "failed");
            }{

            }
            retries++;
        }


    }
}
