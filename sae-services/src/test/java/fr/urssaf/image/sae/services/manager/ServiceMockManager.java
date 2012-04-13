package fr.urssaf.image.sae.services.manager;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;

/**
 * Implémentation des Mocks de la couche sae-services
 * 
 * 
 */
public final class ServiceMockManager {

   private ServiceMockManager() {

   }

   /**
    * 
    * @return instance de {@link SAEBulkCaptureService}
    */
   public static SAEBulkCaptureService createSAEBulkCaptureService() {

      SAEBulkCaptureService service = EasyMock
            .createMock(SAEBulkCaptureService.class);

      return service;
   }

   /**
    * 
    * @return instance de {@link SAECaptureMasseService}
    */
   public static SAECaptureMasseService createSAECaptureMasseService() {

      SAECaptureMasseService service = EasyMock
            .createMock(SAECaptureMasseService.class);

      return service;
   }

}
