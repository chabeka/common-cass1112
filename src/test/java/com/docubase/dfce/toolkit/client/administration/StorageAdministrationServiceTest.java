package com.docubase.dfce.toolkit.client.administration;

import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.AbstractTestBase;

public class StorageAdministrationServiceTest extends AbstractTestBase {
    private final StorageAdministrationService storageAdministrationService = serviceProvider
	    .getStorageAdministrationService();

    @BeforeClass
    public static void setUp() {
	connect();
    }

    @AfterClass
    public static void tearDown() {
	disconnect();
    }

    @Test
    public void testGetAllCategories() {
	storageAdministrationService.getAllCategories();
    }

}
