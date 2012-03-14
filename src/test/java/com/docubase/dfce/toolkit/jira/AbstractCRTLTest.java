package com.docubase.dfce.toolkit.jira;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;

import org.junit.After;
import org.junit.Before;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public abstract class AbstractCRTLTest extends AbstractTestBase {
    protected static final String APR = "apr";
    protected static final String NCE = "nce";
    protected static final String SRN = "srn";
    protected static final String DATE = "date";
    public Base base;

    @Before
    public void before() {
	connect();
	createBase();
    }

    protected void createBase() {
	base = serviceProvider.getBaseAdministrationService().getBase(
		this.getClass().getSimpleName());
	if (base != null) {
	    serviceProvider.getBaseAdministrationService().deleteBase(base);
	}

	base = ToolkitFactory.getInstance().createBase(
		CTRL64Test.class.getSimpleName());

	Category categoryNCE = serviceProvider
		.getStorageAdministrationService().findOrCreateCategory(NCE,
			CategoryDataType.STRING);
	Category categoryAPR = serviceProvider
		.getStorageAdministrationService().findOrCreateCategory(APR,
			CategoryDataType.STRING);
	Category categorySRN = serviceProvider
		.getStorageAdministrationService().findOrCreateCategory(SRN,
			CategoryDataType.STRING);

	Category categoryDate = serviceProvider
		.getStorageAdministrationService().findOrCreateCategory(DATE,
			CategoryDataType.DATE);

	BaseCategory baseCategoryNCE = ToolkitFactory.getInstance()
		.createBaseCategory(categoryNCE, true);
	base.addBaseCategory(baseCategoryNCE);

	BaseCategory baseCategoryAPR = ToolkitFactory.getInstance()
		.createBaseCategory(categoryAPR, false);
	base.addBaseCategory(baseCategoryAPR);

	BaseCategory baseCategorySRN = ToolkitFactory.getInstance()
		.createBaseCategory(categorySRN, true);
	baseCategorySRN.setSingle(true);
	base.addBaseCategory(baseCategorySRN);

	BaseCategory baseCategoryDate = ToolkitFactory.getInstance()
		.createBaseCategory(categoryDate, true);
	base.addBaseCategory(baseCategoryDate);

	try {
	    serviceProvider.getBaseAdministrationService().createBase(base);
	} catch (ObjectAlreadyExistsException e) {
	    throw new RuntimeException();
	}
    }

    @After
    public void after() {
	serviceProvider.getBaseAdministrationService().deleteBase(base);
	disconnect();
    }
}
