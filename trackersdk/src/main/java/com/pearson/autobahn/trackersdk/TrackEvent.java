package com.pearson.autobahn.trackersdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

/**
 * TrackerEvent
 * It will used to send the tracking data based on interaction type: Event, Activity.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class TrackEvent {
    public JSONObject sdkParams;
    public TrackerConstants constants;

    public Offline offline;
    public Timer offlineTimer;
    OkHttpClient client;
    private HttpRequest request;
    private SharedPreferences sharedPreferences;
    private Boolean offlineEnabled;
    private Boolean sendViaSocket;

    /**
     * Constructor
     *
     * @param params  SDK Params
     * @param context Application Context
     * @throws JSONException
     * @throws IOException
     */
    public TrackEvent(JSONObject params, Context context) throws JSONException, IOException {
        // Default values
        sdkParams = params;
        constants = new TrackerConstants();
        request = new HttpRequest(params);
        offlineEnabled = false;
        sendViaSocket = false;

        // Receiver Access Point
        if ((Boolean) sdkParams.has("enableSocket")) {
            sendViaSocket = (Boolean) sdkParams.get("enableSocket");
        }

        // Offline Processing
        if (context != null) {
            if ((Boolean) sdkParams.has("offlineSupport")) {
                offlineEnabled = (Boolean) sdkParams.get("offlineSupport");
            } else {
                offlineEnabled = constants.offlineSupport;
            }

            if (offlineEnabled) {
                offline = new Offline(context);
                sharedPreferences = context.getSharedPreferences("AutobahnTrackerSP", 0);
                this.checkDeviceStatus();
            }
        }
    }

    /**
     * checkDeviceStatus
     * It checks the device is connected in Internet or Not.
     *
     * @throws IOException
     * @throws JSONException
     */
    public void checkDeviceStatus() throws IOException, JSONException {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (offline.isConnected()) {
                    try {
                        offline.processData(sdkParams, offline.EVENTS_TABLE_NAME);
                        offline.processData(sdkParams, offline.ACTIVITIES_TABLE_NAME);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, constants.offlineInterval);

    }

    /**
     * track
     * It will track the user's interactions, send it to Receiver API.
     *
     * @param eventUrl
     * @param eventData
     * @param eventConfig
     * @throws JSONException
     * @throws IOException
     */
    public void track(String eventUrl, JSONObject eventData, JSONObject eventConfig) throws JSONException, IOException {
        // Variables
        JSONObject data = new JSONObject();
        JSONObject eventPayload = new JSONObject();
        JSONObject apiData = new JSONObject();
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

        apiData.put("data", eventPayload);

        if (offlineEnabled) {
            Log.i("SDK:", "--: Offline :--");
            offline.insertData(eventUrl, apiData.toString());
        } else {
            try {
                if (sendViaSocket) {
                    String socketEvent = "petracker-" + eventUrl;
                    Socket socket = request.getSocket();
                    socket.emit(socketEvent, apiData, new Ack() {
                        @Override
                        public void call(Object... args) {
                            Log.i("SDK Response:", "Data: " + args[0].toString());
                        }
                    });
                } else {
                    request.sendResetAPI(eventUrl, apiData.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
