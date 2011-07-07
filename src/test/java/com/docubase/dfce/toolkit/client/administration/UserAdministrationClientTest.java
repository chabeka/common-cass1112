package com.docubase.dfce.toolkit.client.administration;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.UserAdministrationService;

import org.junit.Ignore;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

@Ignore
public class UserAdministrationClientTest extends AbstractDFCEToolkitClientTest {
    private UserAdministrationService userAdministrationService = ServiceProvider
	    .getUserAdministrationService();
}
