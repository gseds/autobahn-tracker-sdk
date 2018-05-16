package com.pearson.autobahn.trackersdk;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Autobahn Tracker
 * It have the Specs for validating the Tracker Features
 *
 * @author GSE Data & Services - gsesoftwareservices@pearson.com
 */

public class PETrackerTest {
    String invalidTrackingId;
    String trackingId;
    JSONObject trackerConfig;


    @Before
    public void loadConfig() throws JSONException {
        // Tracker ID
        invalidTrackingId = "";
        trackingId = "AB-1847";

        trackerConfig = new JSONObject();
        trackerConfig.put("offlineSupport", false);
        trackerConfig.put("environment", "development");
    }

    @Test
    public void isPETrackerExisted() {
        try {
            Class.forName("com.pearson.autobahn.trackersdk.PETracker");
        } catch (ClassNotFoundException e) {
            fail("Unable to found the class PETracker");
        }
    }

    @Test
    public void invalidTrackingID() throws Exception {
        try {
            AutobahnTracker trackerObject = PETracker.init(invalidTrackingId);
            fail("Expect Error Message when passing an empty trackingID");
        } catch (Throwable e) {
            assertThat(e.getMessage(), containsString("Tracker SDK requires valid TrackingID"));
        }
    }

    @Test
    public void invalidTrackingIDandConfig() throws Exception {
        try {
            JSONObject config = new JSONObject();
            AutobahnTracker trackerObject = PETracker.init(invalidTrackingId, config);
            fail("Expect Error Message when passing an empty trackingID");
        } catch (Throwable e) {
            assertThat(e.getMessage(), containsString("Tracker SDK requires valid TrackingID"));
        }
    }

    @Test
    public void invalidTrackingIDandContext() throws Exception {
        try {
            Context mMockContext = mock(Context.class);
            JSONObject config = new JSONObject();
            AutobahnTracker trackerObject = PETracker.init(invalidTrackingId, config, mMockContext);
            fail("Expect Error Message when passing an empty trackingID");
        } catch (Throwable e) {
            assertThat(e.getMessage(), containsString("Tracker SDK requires valid TrackingID"));
        }
    }

    @Test
    public void validTrackingID() throws Exception {
        try {
            AutobahnTracker trackerObject = PETracker.init(trackingId);
            assertEquals(trackerObject instanceof AutobahnTracker, true);
            assertNotNull(trackerObject);
        } catch (Throwable e) {
            fail("Expect: Return AutobahnTracker Object while passing valid trackingID");
        }
    }

    @Test
    public void validTrackingIDandConfig() throws Exception {
        try {
            AutobahnTracker trackerObject = PETracker.init(trackingId, trackerConfig);
            assertEquals(trackerObject instanceof AutobahnTracker, true);
            assertNotNull(trackerObject);
        } catch (Throwable e) {
            fail("Expect: Return AutobahnTracker Object while passing valid trackingID");
        }
    }

    @Test
    public void validTrackingIDandContext() throws JSONException {
        try {
            // Mock Values
            Context mMockContext = mock(Context.class);
            AutobahnTracker trackerObject = PETracker.init(trackingId, trackerConfig, mMockContext);
            assertEquals(trackerObject instanceof AutobahnTracker, true);
            assertNotNull(trackerObject);
        } catch (Throwable e) {
            fail("Expect: Return AutobahnTracker Object while passing valid trackingID");
        }
    }
}
