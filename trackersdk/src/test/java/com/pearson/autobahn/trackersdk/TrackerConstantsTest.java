package com.pearson.autobahn.trackersdk;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TrackerConstantsTest
 * It will validate the constants used in Tracker SDK
 *
 * @author GSE Data & Services
 *
 */

public class TrackerConstantsTest {
    public TrackerConstants constants;

    @Before
    public void loadConfig() throws JSONException {
        constants = new TrackerConstants();
    }

    @Test
    public void TrackerConstants() {
        // Basic configuration
        assertEquals(constants.environment, "production");
        assertEquals(constants.debugMode, false);
        assertEquals(constants.schemaValidation, false);
        assertEquals(constants.offlineSupport, true);
        assertEquals(constants.originatingSystemCode, "AutobahnTrackerSDK");
        assertEquals(constants.offlineRecordCount, 30);
        assertEquals(constants.offlineInterval, 10000);

        // Environmental URLs
        assertNotNull(constants.autobahnUrl);
        assertEquals(constants.autobahnUrl.has("dev"), true);
        assertEquals(constants.autobahnUrl.has("test"), true);
        assertEquals(constants.autobahnUrl.has("stg"), true);
        assertEquals(constants.autobahnUrl.has("prf"), true);
        assertEquals(constants.autobahnUrl.has("prd"), true);
    }
}
