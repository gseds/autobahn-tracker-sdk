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
        offlineSupport = false;
        schemaValidation = false;
        debugMode = true;

        // Autobahn URLS
        autobahnUrl = new JSONObject();
        autobahnUrl.put("development", "https://devapi.english.com/autobahn/collect");
        autobahnUrl.put("test", "https://testapi.english.com/autobahn/collect");
        autobahnUrl.put("stage", "https://stageapi.english.com/autobahn/collect");
        autobahnUrl.put("production", "https://api.english.com/autobahn/collect");

        // Offline
        offlineInterval = 60000;
        offlineRecordCount = 4;
    }
}
