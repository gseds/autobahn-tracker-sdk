package com.pearson.autobahn.trackersdk;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HttpRequest
 * It will used to send the tracking data based on connection type: REST Api, Socket.
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */


public class HttpRequest {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public JSONObject sdkParams;
    public TrackerConstants constants;
    OkHttpClient client;

    HttpRequest(JSONObject params) throws JSONException {
        sdkParams = params;
        constants = new TrackerConstants();
        client = new OkHttpClient();
    }

    public Socket getSocket() {
        final Socket socket;
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = false;
            opts.reconnection = false;
            opts.path = "/autobahn/socket";
            String appHost = constants.autobahnUrl.getString("production");
            // Send Data to Receiver
            if (sdkParams.has("environment")) {
                appHost = constants.autobahnUrl.getString(String.valueOf(sdkParams.get("environment")));
            }
            socket = IO.socket(appHost, opts);
            socket.io()
                    .on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Transport transport = (Transport) args[0];

                            /** Modify Request Headers **/
                            transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {
                                    Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                                    try {
                                        headers.put("PETRACKER-TRACKING-ID", Arrays.asList(sdkParams.getString("trackingID")));
                                        if (sdkParams.has("appOrigin")) {
                                            headers.put("Origin", Arrays.asList(sdkParams.getString("appOrigin")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
            socket.connect();

            socket.on("connect_error", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("SDK: ", "Socket: " + args[0].toString());
                    socket.disconnect();
                }
            });

            socket.on(socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("SDK: ", "Socket connected with Receiver");
                }
            });

            return socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendRestAPI(String eventUrl, String data) throws IOException, JSONException {
        String apiUrl = constants.autobahnUrl.getString("production") + "/autobahn/collect/";
        String appOrigin = null;
        // Send Data to Receiver
        if (sdkParams.has("environment")) {
            apiUrl = constants.autobahnUrl.getString(String.valueOf(sdkParams.get("environment"))) + "/autobahn/collect/" + eventUrl;
        }

        // Strict Mode Enabling
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        RequestBody reqBody = RequestBody.create(JSON, data);
        if (sdkParams.has("appOrigin")) {
            appOrigin = sdkParams.getString("appOrigin");
        }
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(reqBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("PETRACKER-TRACKING-ID", sdkParams.getString("trackingID"))
                .addHeader("Origin", appOrigin)
                .build();

        Response response = client.newCall(request).execute();
        Log.i("Response", response.body().string());
    }

    public Request getRequest(String eventUrl, String data) throws IOException, JSONException {
        String apiUrl = constants.autobahnUrl.getString("production") + "/autobahn/collect/";
        String appOrigin = null;
        // Send Data to Receiver
        if (sdkParams.has("environment")) {
            apiUrl = constants.autobahnUrl.getString(String.valueOf(sdkParams.get("environment"))) + "/autobahn/collect/" + eventUrl;
        }

        // Strict Mode Enabling
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (sdkParams.has("appOrigin")) {
            appOrigin = sdkParams.getString("appOrigin");
        }

        RequestBody reqBody = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(reqBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("PETRACKER-TRACKING-ID", sdkParams.getString("trackingID"))
                .addHeader("Origin", appOrigin)
                .build();

        return request;
    }
}
