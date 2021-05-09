package com.example.sheeptracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "TrackHistory.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TRIP_TABLE = "Trip";
    private static final String TRIP_ID = "trip_id";
    private static final String TRIP_DATE_TIME = "trip_date_time";

    private static final String MAP_TABLE= "Map";
    private static final String MAP_ID = "map_id";
    private static final String MAP_NAME = "map_name";
    private static final String MAP_NW_LATITUDE = "map_nw_latitude";
    private static final String MAP_NW_LONGITUDE = "map_nw_longitude";
    private static final String MAP_SE_LATITUDE = "map_se_latitude";
    private static final String MAP_SE_LONGITUDE = "map_se_longitude";

    private static final String FOOTPRINT_TABLE = "Footprint";
    private static final String FOOTPRINT_ID = "footprint_id";
    private static final String FOOTPRINT_LATITUDE = "latitude";
    private static final String FOOTPRINT_LONGITUDE = "longitude";

    private static final String SHEEP_TABLE = "Sheep_entry";
    private static final String SHEEP_ID = "sheep_entry_id";
    private static final String SHEEP_LATITUDE= "latitude";
    private static final String SHEEP_LONGITUDE = "longitude";
    private static final String SHEEP_TOTAL_SPOTTED = "total_spotted";
    private static final String SHEEP_BLACKS = "blacks";
    private static final String SHEEP_WHITES = "whites";
    private static final String SHEEP_RED_TIES = "red_ties";
    private static final String SHEEP_YELLOW_TIES = "yellow_ties";
    private static final String SHEEP_GREEN_TIES = "green_ties";
    private static final String SHEEP_BLUE_TIES = "blue_ties";
    private static final String SHEEP_NO_TIES = "no_ties";
    private static final String SHEEP_RED_EAR = "red_ear";
    private static final String SHEEP_YELLOW_EAR = "yellow_ear";
    private static final String SHEEP_GREEN_EAR = "green_ear";


    private static final String SHEEP_SPOTTED_FROM = "Sheep_spotted_from";
    private static final String SPOTTED_FROM_ID = "location_id";
    private static final String SPOTTED_FROM_LATITUDE= "spotted_from_latitude";
    private static final String SPOTTED_FROM_LONGITUDE = "spotted_from_longitude";

    private static final String PREDATOR_TABLE = "Predator_location";
    private static final String PREDATOR_ID = "predator_id";
    private static final String PREDATOR_LATITUDE= "latitude";
    private static final String PREDATOR_LONGITUDE = "longitude";
    private static final String PREDATOR_TYPE = "type";

    private static final String DRIVE_TABLE = "Drive";
    private static final String DRIVE_ID = "drive_id";

    private static final String DEAD_SHEEP_TABLE = "Dead_sheep";
    private static final String DEAD_SHEEP_ID = "dead_sheep_id";
    private static final String DEAD_SHEEP_LATITUDE = "dead_sheep_latitude";
    private static final String DEAD_SHEEP_LONGITUDE = "dead_sheep_longitude";


    //for later
    private static final String SHEEP_TOTAL_CALCULATED = "total_calculated";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " +
                TRIP_TABLE + " (" +
                TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAP_ID + " INTEGER NOT NULL, " +
                TRIP_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                " FOREIGN KEY (" + MAP_ID + ") REFERENCES "+MAP_TABLE+"(" + MAP_ID + "));";

        String query2 = "CREATE TABLE " +
                MAP_TABLE + " (" +
                MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAP_NAME + " TEXT, " +
                MAP_NW_LATITUDE + " DOUBLE NOT NULL, " +
                MAP_NW_LONGITUDE + " DOUBLE NOT NULL, " +
                MAP_SE_LATITUDE + " DOUBLE NOT NULL, " +
                MAP_SE_LONGITUDE + " DOUBLE NOT NULL);";

        String query3 = "CREATE TABLE " +
                FOOTPRINT_TABLE + " (" +
                FOOTPRINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FOOTPRINT_LATITUDE + " DOUBLE NOT NULL, " +
                FOOTPRINT_LONGITUDE + " DOUBLE NOT NULL, " +
                TRIP_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TRIP_ID + ") REFERENCES "+TRIP_TABLE+"(" + TRIP_ID + "));";

        String query4 = "CREATE TABLE " +
                SHEEP_TABLE + " (" +
                SHEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SHEEP_LATITUDE + " DOUBLE NOT NULL, " +
                SHEEP_LONGITUDE + " DOUBLE NOT NULL, " +
                SHEEP_TOTAL_CALCULATED + " INTEGER, " +
                SHEEP_TOTAL_SPOTTED + " INTEGER, " +
                SHEEP_BLACKS + " INTEGER DEFAULT 0, " +
                SHEEP_WHITES + " INTEGER DEFAULT 0, " +
                SHEEP_RED_TIES + " INTEGER DEFAULT 0, " +
                SHEEP_YELLOW_TIES + " INTEGER DEFAULT 0, " +
                SHEEP_GREEN_TIES + " INTEGER DEFAULT 0, " +
                SHEEP_BLUE_TIES + " INTEGER DEFAULT 0, " +
                SHEEP_NO_TIES + " INTEGER DEFAULT 0, " +
                SHEEP_RED_EAR + " INTEGER DEFAULT 0, " +
                SHEEP_YELLOW_EAR + " INTEGER DEFAULT 0, " +
                SHEEP_GREEN_EAR + " INTEGER DEFAULT 0, " +
                TRIP_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TRIP_ID + ") REFERENCES "+TRIP_TABLE+"(" + TRIP_ID + "));";

        String query5 = "CREATE TABLE " +
                SHEEP_SPOTTED_FROM + " (" +
                SPOTTED_FROM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SHEEP_ID + " INTEGER NOT NULL, " +
                SPOTTED_FROM_LATITUDE + " INTEGER NOT NULL, " +
                SPOTTED_FROM_LONGITUDE + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + SHEEP_ID + ") REFERENCES "+SHEEP_TABLE+"(" + SHEEP_ID + "));";


        String query6 = "CREATE TABLE " +
                PREDATOR_TABLE + " (" +
                PREDATOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PREDATOR_TYPE + " TEXT NOT NULL, " +
                PREDATOR_LATITUDE + " INTEGER NOT NULL, " +
                PREDATOR_LONGITUDE + " INTEGER NOT NULL, " +
                TRIP_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TRIP_ID + ") REFERENCES "+TRIP_TABLE+"(" + TRIP_ID + "));";

        String query7 = "CREATE TABLE " +
                DRIVE_TABLE + " (" +
                DRIVE_ID + " TEXT PRIMARY KEY);";

        String query8 = "CREATE TABLE " +
                DEAD_SHEEP_TABLE + " (" +
                DEAD_SHEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEAD_SHEEP_LATITUDE + " INTEGER NOT NULL, " +
                DEAD_SHEEP_LONGITUDE + " INTEGER NOT NULL, " +
                TRIP_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TRIP_ID + ") REFERENCES "+TRIP_TABLE+"(" + TRIP_ID + "));";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        db.execSQL(query8);
        Log.d("sdad", "safgfsag");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.d("sdad", "safgfsag");
            db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MAP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + FOOTPRINT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SHEEP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SHEEP_SPOTTED_FROM);
            onCreate(db);
        }
    }


    public void addMap(String mapName, double NW_lat, double NW_lon, double SE_lat, double SE_lon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MAP_NAME, mapName);
        contentValues.put(MAP_NW_LATITUDE, NW_lat);
        contentValues.put(MAP_NW_LONGITUDE, NW_lon);
        contentValues.put(MAP_SE_LATITUDE, SE_lat);
        contentValues.put(MAP_SE_LONGITUDE, SE_lon);
        db.insert(MAP_TABLE, null, contentValues);
        db.close();

    }

    public void addTrip(int mapID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MAP_ID, mapID);
        db.insert(TRIP_TABLE, null, contentValues);
        db.close();
    }

    public void addFootprint(double latitude, double longitude, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FOOTPRINT_LATITUDE, latitude);
        contentValues.put(FOOTPRINT_LONGITUDE, longitude);
        contentValues.put(TRIP_ID, tripID);
        db.insert(FOOTPRINT_TABLE, null, contentValues);
        db.close();
    }

    public int addSheep(double latitude, double longitude, double spottedFromLat, double spottedFromLon, int totalSpotted, int blacks, int whites,
                         int[] tieColors, int[]earMarkColors, int tripID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHEEP_LATITUDE, latitude);
        contentValues.put(SHEEP_LONGITUDE, longitude);
        contentValues.put(SHEEP_TOTAL_SPOTTED, totalSpotted);
        contentValues.put(SHEEP_BLACKS, blacks);
        contentValues.put(SHEEP_WHITES, whites);
        contentValues.put(SHEEP_RED_TIES, tieColors[0]);
        contentValues.put(SHEEP_YELLOW_TIES, tieColors[1]);
        contentValues.put(SHEEP_GREEN_TIES, tieColors[2]);
        contentValues.put(SHEEP_BLUE_TIES, tieColors[3]);
        contentValues.put(SHEEP_NO_TIES, tieColors[4]);
        contentValues.put(SHEEP_RED_EAR, earMarkColors[0]);
        contentValues.put(SHEEP_YELLOW_EAR, earMarkColors[1]);
        contentValues.put(SHEEP_GREEN_EAR,earMarkColors[2]);
        contentValues.put(TRIP_ID, tripID);
        long insertedID = db.insert(SHEEP_TABLE, null, contentValues);
        db.close();
        int intID = (int)insertedID;
        addSpottedFrom(spottedFromLat, spottedFromLon, intID);
        Log.d("sdnioto", intID + ".");
        return intID;
    }

    public void updateSheep(double latitude, double longitude, int totalSpotted, int blacks, int whites,
                            int[] tieColors, int[]earMarkColors, int sheepID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(SHEEP_TOTAL_SPOTTED, totalSpotted);
            contentValues.put(SHEEP_BLACKS, blacks);
            contentValues.put(SHEEP_WHITES, whites);
            contentValues.put(SHEEP_RED_TIES, tieColors[0]);
            contentValues.put(SHEEP_YELLOW_TIES, tieColors[1]);
            contentValues.put(SHEEP_GREEN_TIES, tieColors[2]);
            contentValues.put(SHEEP_BLUE_TIES, tieColors[3]);
            contentValues.put(SHEEP_NO_TIES, tieColors[4]);
            contentValues.put(SHEEP_RED_EAR, earMarkColors[0]);
            contentValues.put(SHEEP_YELLOW_EAR, earMarkColors[1]);
            contentValues.put(SHEEP_GREEN_EAR, earMarkColors[2]);

            db.update(SHEEP_TABLE, contentValues, SHEEP_ID + " = ?", new String[]{Integer.toString(sheepID)});
            addSpottedFrom(latitude, longitude, sheepID);
            db.close();
        }catch (Exception e){
            Log.d("db", "Could not update db entry");
        }

    }

    public void addSpottedFrom(double latitude, double longitude, int sheep_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SPOTTED_FROM_LATITUDE, latitude);
        contentValues.put(SPOTTED_FROM_LONGITUDE, longitude);
        contentValues.put(SHEEP_ID, sheep_id);
        db.insert(SHEEP_SPOTTED_FROM, null, contentValues);
        db.close();
    }

    public void addPredator(double latitude, double longitude, String predatorType, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PREDATOR_LATITUDE, latitude);
        contentValues.put(PREDATOR_LONGITUDE, longitude);
        contentValues.put(TRIP_ID, tripID);
        contentValues.put(PREDATOR_TYPE, predatorType);
        db.insert(PREDATOR_TABLE, null, contentValues);
        db.close();
    }

    public void removeDriveRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DRIVE_TABLE);
    }

    public void addDrive(String driveID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Clear the table. The table should contain either one or zero records at all times.
        db.execSQL("delete from "+ DRIVE_TABLE);

        contentValues.put(DRIVE_ID, driveID);
        db.insert(DRIVE_TABLE, null, contentValues);
        db.close();
    }

    public void addDeadSheep(double latitude, double longitude, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DEAD_SHEEP_LATITUDE, latitude);
        contentValues.put(DEAD_SHEEP_LONGITUDE, longitude);
        contentValues.put(TRIP_ID, tripID);
        db.insert(DEAD_SHEEP_TABLE, null, contentValues);
        Log.d("HEIHEIH", "SADASD");
        db.close();
    }


    public String getDriveID(){
        String DriveID = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + DRIVE_ID + " FROM " + DRIVE_TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            DriveID = c.getString(c.getColumnIndex(DRIVE_ID));
        }
        return DriveID;
    }

    public int getMapID(String mapString) {
        int mapID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + MAP_ID + " FROM " + MAP_TABLE + " WHERE " + MAP_NAME + " = '" + mapString + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            mapID = c.getInt(c.getColumnIndex(MAP_ID));
        }
        return mapID;
    }

    public int getMapID(int tripID) {
        int mapID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + MAP_ID + " FROM " + TRIP_TABLE + " WHERE " + TRIP_ID + " = '" + tripID + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            mapID = c.getInt(c.getColumnIndex(MAP_ID));
        }
        return mapID;
    }

    public String getMapName(int mapID) {
        String mapName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + MAP_NAME + " FROM " + MAP_TABLE + " WHERE " + MAP_ID + " = '" + mapID + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            mapName = c.getString(c.getColumnIndex(MAP_NAME));
        }
        return mapName;
    }


    public int getLastTripID() {
        int tripID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + TRIP_ID + " FROM " + TRIP_TABLE + " order by " + TRIP_ID + " DESC limit 1";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            tripID = c.getInt(c.getColumnIndex(TRIP_ID));
        }
        return tripID;
    }

    public Cursor readTripData() {
        String query = "SELECT * FROM " + TRIP_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }
        return c;
    }

    public Cursor readFootstepData(int tripID) {
        String query = "SELECT * FROM " + FOOTPRINT_TABLE + " WHERE " + TRIP_ID + " = '" + tripID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }

        return c;
    }

    public Cursor readSheepData(int sheepID) {
        String query = "SELECT * FROM " + SHEEP_TABLE + " WHERE " + SHEEP_ID + " = '" + sheepID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }

        return c;
    }


    public Cursor readSheepLatLonTrip(int tripID) {
        String query = "SELECT " + SHEEP_ID + "," + SHEEP_LATITUDE + "," + SHEEP_LONGITUDE + " FROM " + SHEEP_TABLE + " WHERE " + TRIP_ID + " = '" + tripID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }

        return c;
    }

    public Cursor readSpottedFromLatLon(int sheepID) {
        String query = "SELECT " + SPOTTED_FROM_LATITUDE + "," + SPOTTED_FROM_LONGITUDE + " FROM " + SHEEP_SPOTTED_FROM + " WHERE " + SHEEP_ID + " = '" + sheepID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }

        return c;
    }

    public Cursor readPredatorLatLons(int tripID) {
        String query = "SELECT " + PREDATOR_LATITUDE + "," + PREDATOR_LONGITUDE + " FROM " + PREDATOR_TABLE + " WHERE " + TRIP_ID + " = '" + tripID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }

        return c;
    }

    public Cursor readMapData(int mapID) {
        String query = "SELECT * FROM " + MAP_TABLE + " WHERE " + MAP_ID + " = '" + mapID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }
        return c;
    }

    public Cursor readDeadSheepData(int tripID) {
        String query = "SELECT * FROM " + DEAD_SHEEP_TABLE + " WHERE " + TRIP_ID + " = '" + tripID + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null) {
            c= db.rawQuery(query, null);
        }
        return c;
    }
}
