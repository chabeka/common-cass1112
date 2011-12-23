package com.docubase.dfce.toolkit.client.ged;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.StoreService;

import org.apache.commons.io.FilenameUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;

public class StoreClientTest extends AbstractTestBase {
    private final StoreService storeService = serviceProvider.getStoreService();
    private final BaseAdministrationService baseAdministrationService = serviceProvider
	    .getBaseAdministrationService();

    @BeforeClass
    public static void setUp() {
	connect();
    }

    @AfterClass
    public static void tearDown() {
	disconnect();
    }

    @Test
    public void testStoreDocument() throws IOException, TagControlException {
	Base base = baseAdministrationService.getBase("baseId");
	if (base == null) {
	    base = ToolkitFactory.getInstance().createBase("baseId");
	    try {
		baseAdministrationService.createBase(base);
	    } catch (ObjectAlreadyExistsException e) {
		e.printStackTrace();
		fail("base : " + base.getBaseId() + " already exists");
	    }
	    base = serviceProvider.getBaseAdministrationService().getBase(
		    "baseId");
	}

	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	File file = new File(ClassLoader.getSystemResource("doc1.pdf")
		.getPath());
	Document storeDocument = storeService.storeDocument(document,
		FilenameUtils.getBaseName(file.getName()),
		FilenameUtils.getExtension(file.getName()),
		new FileInputStream(file));
	assertNotNull(storeDocument);
    }
}
