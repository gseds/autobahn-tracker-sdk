package com.pearson.autobahn.trackersdk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * TrackerSDK
 * It will captures user's interactions and send it to Autobahn Data Ecosystem.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */


public class TrackerConstants {
    // Constants
    public String originatingSystemCode;
    public String environment;
    public Boolean offlineSupport;
    public Boolean debugMode;
    public Boolean schemaValidation;
    public JSONObject autobahnUrl;
    public int offlineRecordCount;
    public int offlineInterval;

    public TrackerConstants() throws JSONException {
        // Setting Default Values
        originatingSystemCode = "AutobahnTrackerSDK";
        environment = "production";
        offlineSupport = true;
        schemaValidation = false;
        debugMode = true;

        // Autobahn URLS
        autobahnUrl = new JSONObject();
        autobahnUrl.put("development", "https://devapi.english.com");
        autobahnUrl.put("test", "https://testapi.english.com");
        autobahnUrl.put("stage", "https://stageapi.english.com");
        autobahnUrl.put("production", "https://api.english.com");

        // Offline
        offlineInterval = 10000; // 10 Seconds
        offlineRecordCount = 30; // 30 Records
    }
}
