package fr.urssaf.image.sae.storage.dfce.services.provider;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Implémentation des Mocks de la couche sae-services
 * 
 * 
 */
public final class ServiceMockProvider {

   private ServiceMockProvider() {

   }

   /**
    * 
    * @return instance de {@link InsertionService}
    */
   public static InsertionService createSAEBulkCaptureService() {

      InsertionService service = EasyMock.createMock(InsertionService.class);

      return service;
   }
   
   /**
    * 
    * @return instance de {@link InterruptionTraitementSupport}
    */
   public static InterruptionTraitementSupport createInterruptionTraitementSupport() {

      InterruptionTraitementSupport service = EasyMock
            .createMock(InterruptionTraitementSupport.class);

      return service;
   }

}
