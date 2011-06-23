package com.docubase.dfce.toolkit.client.administration;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.junit.Test;

import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class StorageAdministrationServiceTest extends AbstractDFCEToolkitClientTest {
   private StorageAdministrationService storageAdministrationService = ServiceProvider
         .getStorageAdministrationService();

   @Test
   public void testGetAllCategories() {
      storageAdministrationService.getAllCategories();
   }
}
