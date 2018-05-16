package com.pearson.autobahn.trackersdk;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import io.socket.client.Ack;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * RequestTest
 * <p>
 * Sending data to Autobahn System
 */
public class RequestTest {
    public TestConfig config;
    JSONObject trackerConfig;
    HttpRequest request;

    @Before
    public void loadConfig() throws JSONException {
        config = new TestConfig();

        // Configurations
        trackerConfig = config.trackingConfig;
        trackerConfig.put("trackingID", config.trackingId);

        request = new HttpRequest(trackerConfig);
    }

    @Test
    public void sockets() throws JSONException {
        Socket socket = request.getSocket();

        try {
            // Must be an Object
            assertNotNull(socket);
            socket.emit("petracker-events", config.apiData, new Ack() {
                @Override
                public void call(Object... args) {
                    assertNotNull(args[0].toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to send data via socket");
        }
    }

    @Test
    public void restAPI() {
        try {
            request.sendRestAPI("events", config.apiData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to send data via REST API");
        }
    }

    @Test
    public void RestAPIObject() throws IOException, JSONException {
        try {
            Request requestObject = request.getRequest("events", config.apiData.toString());
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(requestObject).execute();
            assertEquals(response.code(), 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to send data via REST API Object");
        }
    }

}
