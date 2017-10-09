package com.pearson.autobahn.trackersdk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * TrackerEvent
 * It will used to send the tracking data based on interaction type: Event, Activity.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class TrackEvent {
    public JSONObject sdkParams;
    public TrackerConstants constants;
    public TrackerAPI request;

    public TrackEvent(JSONObject params) throws JSONException {
        sdkParams = params;
        constants = new TrackerConstants();
        request = new TrackerAPI(params);
    }

    public void track(String eventUrl, JSONObject eventData, JSONObject eventConfig) throws JSONException, IOException {
        Log.i("SDK: ", "Send Event");
        // Variables
        JSONObject payload = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject eventPayload = new JSONObject();
        JSONArray eventArray = new JSONArray();
        String messageVersion = "latest", messageNamespace = null;

        // Event Data
        if (!eventData.has("messageTypeCode")) {
            throw new Error("messageTypeCode is required property");
        }

        // Formation of Tracking Data
        // Message Version
        if (eventConfig.has("messageVersion")) {
            messageVersion = String.valueOf(eventConfig.get("messageVersion"));
        } else if (sdkParams.has("messageVersion")) {
            messageVersion = String.valueOf(sdkParams.get("messageVersion"));
        }

        // Message Namespace
        if (eventConfig.has("namespace")) {
            messageNamespace = String.valueOf(eventConfig.get("namespace"));
        } else if (sdkParams.has("namespace")) {
            messageNamespace = String.valueOf(sdkParams.get("namespace"));
        }

        data.put("messageTypeCode", eventData.get("messageTypeCode").toString());
        data.put("messageVersion", messageVersion);
        data.put("namespace", messageNamespace);
        data.put("actionType", "create");
        data.put("payload", eventData);

        // Tracking Event
        if (eventConfig.has("originatingSystemCode")) {
            eventPayload.put("originatingSystemCode", String.valueOf(eventConfig.get("originatingSystemCode")));
        } else if (sdkParams.has("originatingSystemCode")) {
            eventPayload.put("originatingSystemCode", String.valueOf(sdkParams.get("originatingSystemCode")));
        } else {
            eventPayload.put("originatingSystemCode", constants.originatingSystemCode);
        }
        eventArray.put(data);
        eventPayload.put(eventUrl, eventArray);

        request.send(eventUrl, eventPayload);
    }
}
