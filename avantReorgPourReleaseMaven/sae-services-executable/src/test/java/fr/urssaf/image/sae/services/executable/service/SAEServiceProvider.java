package fr.urssaf.image.sae.services.executable.service;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;

/**
 * Fournisseur de singleton des mocks des services du SAE
 * 
 * 
 */
public final class SAEServiceProvider {

   private static SAEBulkCaptureService bulkCapture;

   private SAEServiceProvider() {

   }

   /**
    * 
    * @return instance unique de bulkCapture
    */
   public static SAEBulkCaptureService getInstanceSAEBulkCaptureService() {

      synchronized (SAEServiceProvider.class) {

         if (bulkCapture == null) {

            bulkCapture = EasyMock.createMock(SAEBulkCaptureService.class);
         }

         return bulkCapture;
      }

   }
}
