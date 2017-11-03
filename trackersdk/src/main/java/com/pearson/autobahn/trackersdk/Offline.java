package com.pearson.autobahn.trackersdk;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Offline
 * It will stores user's interactions and send it to Autobahn Data Ecosystem.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class Offline extends SQLiteOpenHelper {
    // Variables
    public static final String DATABASE_NAME = "AutobahnTrackerSDK";
    public static final int DATABASE_VERSION = 1;

    public static final String EVENTS_TABLE_NAME = "absdk_events";
    public static final String ACTIVITIES_TABLE_NAME = "absdk_activities";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";
    public int RECORD_COUNT;

    public SQLiteDatabase database;
    public Context appContext;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    OkHttpClient client;
    private TrackerConstants constants;

    public Offline(Context context) throws JSONException {
        // Calling Baseclase
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // Configurations
        appContext = context;
        sharedPreferences = context.getSharedPreferences("AutobahnTrackerSP", 0);
        editor = sharedPreferences.edit();
        constants = new TrackerConstants();
        RECORD_COUNT = constants.offlineRecordCount;
        client = new OkHttpClient();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATA + " VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ACTIVITIES_TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATA + " VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + EVENTS_TABLE_NAME);
        db.execSQL("DROP TABLE " + ACTIVITIES_TABLE_NAME);
        onCreate(db);
    }

    /**
     * insertData
     * Storing the data in SQLite Tables
     *
     * @param eventType
     * @param data
     */
    public void insertData(String eventType, String data) {
        String tableName = null;
        if (eventType.equalsIgnoreCase("activities")) {
            tableName = ACTIVITIES_TABLE_NAME;
        } else if (eventType.equalsIgnoreCase("events")) {
            tableName = EVENTS_TABLE_NAME;
        }
        if (!tableName.isEmpty()) {
            database = this.getReadableDatabase();
            ContentValues inputs = new ContentValues();
            inputs.put(COLUMN_DATA, data);
            database.insert(tableName, null, inputs);
            database.close();
        }
    }

    /**
     * isConnected
     * It will check the device is connected in internet or not.
     *
     * @return Boolean
     */
    public Boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void processData(JSONObject sdkParams, String tableName) throws JSONException, IOException {
        if (this.isConnected()) {
            // Open database connection
            database = this.getReadableDatabase();

            // Variables
            int lastRow = 0;
            JSONArray offlineData = new JSONArray();

            // Selecting the data from the offline storage
            Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " LIMIT " + RECORD_COUNT, null);
            if (cursor.getCount() != 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    JSONObject eventData = new JSONObject(cursor.getString(1));
                    lastRow = Integer.parseInt(cursor.getString(0));
                    offlineData.put(eventData);
                }
                cursor.close();

                // Sending data to Receiver API
                TrackerAPI requestBuilder = new TrackerAPI(sdkParams);
                try {
                    String eventType = null;
                    if (tableName.indexOf("event") > -1) {
                        eventType = "events";
                    } else {
                        eventType = "activities";
                    }
                    Request request = requestBuilder.getRequest(eventType, offlineData.toString());
                    Response response = client.newCall(request).execute();
                    Log.i("Offline: ", tableName);
                    Log.i("Offline: ", response.body().string());
                    if (response.code() == 200) {
                        // Clear the tracked offline data
                        database.execSQL("DELETE FROM " + tableName + " WHERE id <=" + lastRow);

                        // Next Iteration
                        cursor = database.rawQuery("SELECT id FROM " + tableName + " LIMIT " + RECORD_COUNT, null);
                        if (cursor.getCount() > 0) {
                            cursor.close();
                            database.close();
                            this.processData(sdkParams, tableName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
