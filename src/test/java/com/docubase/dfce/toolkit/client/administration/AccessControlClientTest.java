package com.docubase.dfce.toolkit.client.administration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;

public class AccessControlClientTest extends AbstractTestBase {
    @Before
    public void before() {
	if (!serviceProvider.isSessionActive()) {
	    serviceProvider
		    .connect(AbstractTestBase.ADM_LOGIN,
			    AbstractTestBase.ADM_PASSWORD,
			    AbstractTestBase.SERVICE_URL);
	}
    }

    @Test
    public void testIsUp() {
	boolean serverUp = serviceProvider.isServerUp();
	Assert.assertTrue(serverUp);
    }

    @Test
    public void testIsUpClosedSession() {
	serviceProvider.disconnect();
	boolean sessionActive = serviceProvider.isServerUp();
	Assert.assertFalse(sessionActive);
    }

    @Test
    public void testIsSessionActive() throws SecurityException,
	    IllegalArgumentException {
	boolean sessionActive = serviceProvider.isSessionActive();
	Assert.assertTrue(sessionActive);
    }
}
