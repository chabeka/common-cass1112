package com.docubase.dfce.toolkit.client.administration;

import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class BaseAdministrationClientSessionScopeTest extends
	AbstractDFCEToolkitClientTest {

    @Test
    public void testCreateBase() {
	for (int i = 0; i < 4; i++) {
	    ServiceProvider.getBaseAdministrationService().getAllBases();
	}
	Authentication.refreshSession();
	for (int i = 0; i < 4; i++) {
	    ServiceProvider.getBaseAdministrationService().getAllBases();
	}
    }
}
