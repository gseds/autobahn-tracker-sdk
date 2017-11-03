package com.pearson.autobahn.trackersdk;

import android.content.Context;

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

    public AutobahnTracker(JSONObject params) throws JSONException, IOException {
        tracker = new TrackEvent(params, null);
    }

    public AutobahnTracker(JSONObject params, Context appContext) throws JSONException, IOException {
        tracker = new TrackEvent(params, appContext);
    }

    /**
     * sendEvent
     * It is used to track event type parameters.
     *
     * @return {void}
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
     *
     * @return {void}
     */
    public void sendActivity(JSONObject payload) throws JSONException, IOException {
        JSONObject config = new JSONObject();
        tracker.track("activities", payload, config);
    }

    public void sendActivity(JSONObject payload, JSONObject config) throws JSONException, IOException {
        tracker.track("activities", payload, config);
    }
}
