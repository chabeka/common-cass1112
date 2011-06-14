package net.docubase.toolkit.service.administration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.docubase.toolkit.Authentication;
import net.docubase.toolkit.base.AbstractBaseTestCase;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseAdministrationServiceTest extends AbstractBaseTestCase {

    private static final String ADM_LOGIN = "_ADMIN";
    private static final String ADM_PASSWORD = "DOCUBASE";
    private static final String HOSTNAME = "cer69-ds4int";
    private static final Integer PORT = Integer.valueOf(4020);
    private static final Integer DOMAIN_ID = Integer.valueOf(1);

    @BeforeClass
    public static void setUp() {
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, HOSTNAME, PORT,
		DOMAIN_ID);
    }

    @AfterClass
    public static void afterClass() {
	Authentication.closeSession();
    }

    @Test
    public void testGetAllBases() {
	Base newBase = ServiceProvider.getBaseAdministrationService().getBase(
		"newBase");
	if (newBase != null) {
	    ServiceProvider.getBaseAdministrationService().stopBase(newBase);
	    ServiceProvider.getBaseAdministrationService().deleteBase(newBase);
	}

	List<Base> allBases = ServiceProvider.getBaseAdministrationService()
		.getAllBases();
	int allBasesSize = allBases.size();

	newBase = ToolkitFactory.getInstance().createBase("newBase");

	Category category = ServiceProvider.getStorageAdministrationService()
		.findOrCreateCategory("newCategory", CategoryDataType.STRING);

	BaseCategory baseCategory = ToolkitFactory.getInstance()
		.createBaseCategory(category, true);

	newBase.addBaseCategory(baseCategory);

	ServiceProvider.getBaseAdministrationService().createBase(newBase);

	List<Base> allBasesAfterNewBase = ServiceProvider
		.getBaseAdministrationService().getAllBases();
	int allBasesAfterNewBaseSize = allBasesAfterNewBase.size();

	assertEquals(allBasesSize + 1, allBasesAfterNewBaseSize);

	ServiceProvider.getBaseAdministrationService().deleteBase(newBase);
    }
}
