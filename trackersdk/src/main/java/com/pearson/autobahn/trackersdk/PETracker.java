package com.pearson.autobahn.trackersdk;

import android.content.Context;
import android.text.TextUtils;
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

public class PETracker {

    /**
     * Init
     * It will initialize the Tracker SDK with TrackingID and Tracking Configurations.
     *
     * @return AutobahnTracker
     */
    public static AutobahnTracker init(String trackingID) throws JSONException, IOException {
        Log.i("SDK", "Tracker SDK Intialized");
        // Variables
        JSONObject config = new JSONObject();

        // Checking the TrackingID is empty or not
        if (TextUtils.isEmpty(trackingID)) {
            throw new Error("Tracker SDK requires valid TrackingID");
        }
        config.put("trackingID", trackingID);

        // Returns the Tracker Object to Track events
        AutobahnTracker tracker = new AutobahnTracker(config);
        return tracker;
    }

    /**
     * Init
     * It will initialize the Tracker SDK with TrackingID and Tracking Configurations.
     *
     * @return AutobahnTracker
     */
    public static AutobahnTracker init(String trackingID, JSONObject config) throws JSONException, IOException {
        // Checking the TrackingID is empty or not
        if (TextUtils.isEmpty(trackingID)) {
            throw new Error("Tracker SDK requires valid TrackingID");
        }
        config.put("trackingID", trackingID);

        // Returns the Tracker Object to Track events
        AutobahnTracker tracker = new AutobahnTracker(config);
        return tracker;
    }

    /**
     * Init
     * It will initialize the Tracker SDK with TrackingID and Tracking Configurations.
     * It also supports Offline Storage
     *
     * @return AutobahnTracker
     */
    public static AutobahnTracker init(String trackingID, JSONObject config, Context context) throws JSONException, IOException {
        // Checking the TrackingID is empty or not
        if (TextUtils.isEmpty(trackingID)) {
            throw new Error("Tracker SDK requires valid TrackingID");
        }
        config.put("trackingID", trackingID);

        // Returns the Tracker Object to Track events
        AutobahnTracker tracker = new AutobahnTracker(config, context);
        return tracker;
    }
}
