package com.docubase.dfce.toolkit.security;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class SecurityTest extends AbstractTestCaseCreateAndPrepareBase {
    private final ServiceProvider serviceProvider = ServiceProvider
	    .newServiceProvider();

    @Test(expected = RuntimeException.class)
    public void testWrongLogin() {
	serviceProvider.connect(AbstractTestBase.ADM_LOGIN + "wrongLogin",
		AbstractTestBase.ADM_PASSWORD, AbstractTestBase.SERVICE_URL);
    }

    @Test(expected = RuntimeException.class)
    public void testMalformedURL() {
	serviceProvider.connect(AbstractTestBase.ADM_LOGIN,
		AbstractTestBase.ADM_PASSWORD, "malformed"
			+ AbstractTestBase.SERVICE_URL);
    }

    @Test(expected = RuntimeException.class)
    public void testRoleUserOperationAdmin() {
	serviceProvider.connect(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD,
		AbstractTestBase.SERVICE_URL);
	serviceProvider.getBaseAdministrationService().getAllBases();
    }

    @Test
    public void testRoleAdminOperationAdmin() {
	serviceProvider.connect(AbstractTestBase.ADM_LOGIN,
		AbstractTestBase.ADM_PASSWORD, AbstractTestBase.SERVICE_URL);
	List<Base> allBases = serviceProvider.getBaseAdministrationService()
		.getAllBases();
	assertNotNull(allBases);
    }

    @Test
    public void testRoleUserOperationUser() {
	serviceProvider.connect(SIMPLE_USER_NAME, SIMPLE_USER_PASSWORD,
		AbstractTestBase.SERVICE_URL);
	String formatDate = serviceProvider.getSearchService().formatDate(
		new Date(), DateFormat.DATE);
	assertNotNull(formatDate);
    }
}
