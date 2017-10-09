package com.pearson.autobahn.trackersdk;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * TrackerSDK
 * It will captures user's interactions and send it to Autobahn Data Ecosystem.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */


public class TrackerAPI {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public JSONObject sdkParams;
    public TrackerConstants constants;

    public TrackerAPI(JSONObject params) throws JSONException {
        sdkParams = params;
        constants = new TrackerConstants();
    }

    public void send(String eventUrl, JSONObject payload) throws IOException, JSONException {
        Log.i("Events: ", eventUrl);
        Log.i("Events: ", payload.toString());
        JSONObject data = new JSONObject();
        String apiUrl = constants.autobahnUrl.getString("production");
        // Send Data to Receiver
        try {
            if (sdkParams.has("environment")) {
                apiUrl = constants.autobahnUrl.getString(String.valueOf(sdkParams.get("environment"))) + "/" + eventUrl;
                Log.i("SDK: ", apiUrl);
            }
            data.put("data", payload);
            OkHttpClient client = new OkHttpClient();
            RequestBody reqBody = RequestBody.create(JSON, data.toString());
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(reqBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("PETRACKER-TRACKING-ID", sdkParams.getString("trackingID"))
                    .build();

            Log.i("SDK:", "Sending Request");
            client
                    .newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.i("SDK: ", ":Response SDK:");
                            if (!response.isSuccessful()) {
                                Log.i("SDK: Response", response.body().string());
                                throw new IOException("Unexpected code " + response);
                            }
                            Log.i("SDK: Response", response.body().string());
                        }
                    });
        } catch (Exception e) {
            Log.i("SDK:", "Sending Error Exception");
            e.printStackTrace();
        }
    }
}
