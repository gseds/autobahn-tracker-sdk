package com.pearson.autobahn.autobahn_tracker_sdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

        Log.i("SDK: ", " -- Tracker SDK --");
        JSONObject config = new JSONObject();
        JSONObject payload = new JSONObject();
        JSONObject eventConfig = new JSONObject();

        try {
            // SDK Configuration
            config.put("environment", "test");
            config.put("originatingSystemCode", "AutobahnDemoApplication");
            config.put("namespace", "gsetracker");

            // Event Payload
            payload.put("messageTypeCode", "pageview");
            payload.put("url", "DemoUrl");
            payload.put("userID", "User ID");
            payload.put("useragent", "User Agent");

            // Event Config
            eventConfig.put("namespace", "gsetracker");
            eventConfig.put("messageVersion", "v4");
            trackerObject = PETracker.init("PE-APP-1847-2", config);
            trackerObject.sendEvent(payload, eventConfig);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
