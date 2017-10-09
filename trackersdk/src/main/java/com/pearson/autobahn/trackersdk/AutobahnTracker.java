package com.pearson.autobahn.trackersdk;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * TrackerSDK
 * It will captures user's interactions and send it to Autobahn Data Ecosystem.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class AutobahnTracker {
    public TrackEvent tracker;

    public AutobahnTracker(JSONObject params) throws JSONException {
        Log.i("SDK: ", "Events Handling ");
        tracker = new TrackEvent(params);
    }

    /**
     * sendEvent
     * It is used to track event type parameters.
     */
    public void sendEvent(JSONObject payload) throws JSONException, IOException {
        JSONObject config = new JSONObject();
        tracker.track("events", payload, config);
    }

    public void sendEvent(JSONObject payload, JSONObject config) throws JSONException, IOException {
        tracker.track("events", payload, config);
    }

    /**
     * sendActivity
     * It is used to track event type parameters.
     */
    public void sendActivity(JSONObject payload) throws JSONException, IOException {
        JSONObject config = new JSONObject();
        tracker.track("activities", payload, config);
    }

    public void sendActivity(JSONObject payload, JSONObject config) throws JSONException, IOException {
        tracker.track("activities", payload, config);
    }
}
