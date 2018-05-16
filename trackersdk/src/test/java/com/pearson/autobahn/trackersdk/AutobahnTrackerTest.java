package com.pearson.autobahn.trackersdk;

import android.content.Context;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Autobahn Tracker
 * It have the Specs for validating the Tracker Features
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class AutobahnTrackerTest {
    // variables
    public AutobahnTracker trackerObject;
    public TestConfig config;

    @Before
    public void loadConfig() throws IOException, JSONException {
        config = new TestConfig();
        trackerObject = PETracker.init(config.trackingId, config.trackingConfig);
    }

    @Test
    public void isAutobahnTrackerExists() throws Exception {
        try {
            Class.forName("com.pearson.autobahn.trackersdk.AutobahnTracker");
            assertNotNull(trackerObject);
        } catch (ClassNotFoundException e) {
            fail("Unable to load AutobahnTracker class");
        }
    }

    @Test
    public void sendEventData() {
        try {
            trackerObject.sendEvent(config.eventData);
        } catch (Exception e) {
            fail("Unable to send Event data.");
        }
    }

    @Test
    public void sendEventDataWithConfig() {
        try {
            trackerObject.sendEvent(config.eventData, config.eventConfig);
        } catch (Exception e) {
            fail("Unable to send Event Data with Config");
        }
    }

    @Test
    public void sendActivityData() {
        try {
            trackerObject.sendActivity(config.eventData);
        } catch (Exception e) {
            fail("Unable to send Event data.");
        }
    }

    @Test
    public void sendActivityDataWithConfig() {
        try {
            trackerObject.sendActivity(config.eventData, config.eventConfig);
        } catch (Exception e) {
            fail("Unable to send Event Data");
        }
    }

    @Test
    public void initSdkWithAppContext() {
        try {
            Context mMockContext = mock(Context.class);
            trackerObject = PETracker.init(config.trackingId, config.trackingConfig, mMockContext);
            assertNotNull(trackerObject);
        } catch (Exception e) {
            fail("Unable to initialize SDK with App Context");
        }
    }
}
