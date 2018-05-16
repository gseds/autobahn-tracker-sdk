package com.pearson.autobahn.trackersdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestConfig {
    public String trackingId;
    public JSONObject trackingConfig;
    public JSONObject eventData, eventConfig, apiData;

    public TestConfig() throws JSONException {
        // Tracking ID
        trackingId = "AB-1847";

        // Tracking Configuration
        trackingConfig = new JSONObject();
        trackingConfig.put("environment", "development");
        trackingConfig.put("offlineSupport", false);
        trackingConfig.put("appOrigin", "http://autobahn.pearson.com:3000");
        trackingConfig.put("namespace", "gsetracker");
        trackingConfig.put("messageVersion", "v4");

        // Event Data
        eventData = new JSONObject();
        eventData.put("messageTypeCode", "pageview");
        eventData.put("url", "DemoUrl");
        eventData.put("userID", "User ID");
        eventData.put("useragent", "User Agent");

        // Event Config
        eventConfig = new JSONObject();
        eventConfig.put("namespace", "gsetracker");
        eventConfig.put("messageVersion", "v4");

        // API Data
        apiData = new JSONObject();

        // Event Data
        JSONObject payload = new JSONObject();
        payload.put("messageTypeCode", "pageview");
        payload.put("messageVersion", "v4");
        payload.put("namespace", "gsetracker");
        payload.put("actionType", "create");
        payload.put("payload", eventData);

        JSONArray eventArray = new JSONArray();
        eventArray.put(payload);

        JSONObject apiPayload = new JSONObject();
        apiPayload.put("originatingSystemCode", "GSETrackerBDD");
        apiPayload.put("events", eventArray);

        apiData.put("data", apiPayload);
    }
}
