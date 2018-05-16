package com.pearson.autobahn.trackersdk;

import android.content.Context;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * OfflineTest
 * It used to test the Offline Funcationality.
 */

public class OfflineTest {
    public Offline offline;
    public TestConfig config;

    @Before
    public void loadConfig() throws JSONException {
        config = new TestConfig();

        Context mMockContext = mock(Context.class);
        offline = new Offline(mMockContext);
    }

    @Test
    public void sdkOffline() {
        try {
            assertNotNull(offline);
            offline.insertData("activities", config.apiData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Offline is not working properly");
        }
    }

}
