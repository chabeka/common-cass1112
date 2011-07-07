package com.docubase.dfce.toolkit.client.administration;

import net.docubase.toolkit.service.Authentication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class AccessControlClientTest extends AbstractDFCEToolkitClientTest {
    @Before
    public void before() {
	if (!Authentication.isServerUp()) {
	    Authentication.openSession(USER, PASSWORD, URL);
	}
    }

    @Test
    public void testIsUp() {
	boolean sessionActive = Authentication.isServerUp();
	Assert.assertTrue(sessionActive);
    }

    @Test
    public void testIsUpClosedSession() {
	Authentication.closeSession();
	boolean sessionActive = Authentication.isServerUp();
	Assert.assertFalse(sessionActive);
    }

    @Test
    public void testIsSessionActive() throws SecurityException,
	    IllegalArgumentException {
	boolean sessionActive = Authentication.isSessionActive();
	Assert.assertTrue(sessionActive);
    }
}
