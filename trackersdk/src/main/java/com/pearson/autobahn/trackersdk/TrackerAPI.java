package com.pearson.autobahn.trackersdk;

import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * TrackerSDK
 * It will captures user's interactions and send it to Autobahn Data Ecosystem.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */


public class TrackerAPI {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public JSONObject sdkParams;
    public TrackerConstants constants;

    public TrackerAPI(JSONObject params) throws JSONException {
        sdkParams = params;
        constants = new TrackerConstants();
    }

    public Request getRequest(String eventUrl, String data) throws IOException, JSONException {
        String apiUrl = constants.autobahnUrl.getString("production");
        // Send Data to Receiver
        if (sdkParams.has("environment")) {
            apiUrl = constants.autobahnUrl.getString(String.valueOf(sdkParams.get("environment"))) + "/" + eventUrl;
        }

        // Strict Mode Enabling
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        RequestBody reqBody = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(reqBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("PETRACKER-TRACKING-ID", sdkParams.getString("trackingID"))
                .addHeader("Origin", "http://autobahn.pearson.com:3000")
                .build();

        return request;
    }
}
