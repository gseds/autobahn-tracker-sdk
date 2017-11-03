package com.pearson.autobahn.autobahn_tracker_sdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pearson.autobahn.trackersdk.AutobahnTracker;
import com.pearson.autobahn.trackersdk.PETracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    AutobahnTracker trackerObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("SDK: ", "AB:SDK");
        JSONObject config = new JSONObject();

        try {
            // SDK Configuration
            config.put("environment", "development");
            config.put("originatingSystemCode", "AutobahnDemoApplication");
//            config.put("offlineSupport", true);
            trackerObject = PETracker.init("AB-RL-145827-1", config);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEvent(View view) {
        JSONObject payload = new JSONObject();
        JSONObject eventConfig = new JSONObject();

        try {
            // Event Payload
            payload.put("messageTypeCode", "pageview");
            payload.put("url", "DemoUrl");
            payload.put("userID", "User ID");
            payload.put("useragent", "User Agent");

            // Event Configuration
            eventConfig.put("namespace", "gsetracker");
            eventConfig.put("messageVersion", "v4");
            trackerObject.sendEvent(payload, eventConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendActivity(View view) {
        JSONObject payload = new JSONObject();
        JSONObject eventConfig = new JSONObject();

        try {
            // Event Payload
            payload.put("messageTypeCode", "pageview");
            payload.put("url", "DemoUrl");
            payload.put("userID", "User ID");
            payload.put("useragent", "User Agent");

            // Event Configuration
            eventConfig.put("namespace", "gsetracker");
            eventConfig.put("messageVersion", "v4");
            trackerObject.sendActivity(payload, eventConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
