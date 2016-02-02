package fr.urssaf.image.sae.services.batch.capturemasse;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;

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
    * @return instance de {@link SAECaptureMasseService}
    */
   public static SAECaptureMasseService createSAECaptureMasseService() {

      SAECaptureMasseService service = EasyMock
            .createMock(SAECaptureMasseService.class);

      return service;
   }

}
