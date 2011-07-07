package com.docubase.dfce.toolkit.client.administration;

import static org.junit.Assert.*;
import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;

import org.junit.Test;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class BaseAdministrationClientTest extends AbstractDFCEToolkitClientTest {
    private BaseAdministrationService baseAdministrationService = ServiceProvider
	    .getBaseAdministrationService();

    @Test
    public void testCreateBase() {
	Base base = baseAdministrationService.getBase("base");
	if (base != null) {
	    baseAdministrationService.deleteBase(base);
	}
	base = ToolkitFactory.getInstance().createBase("base");
	try {
	    baseAdministrationService.createBase(base);
	} catch (ObjectAlreadyExistsException e) {
	    e.printStackTrace();
	    fail("base : " + base.getBaseId() + " already exists");
	}
    }
}
