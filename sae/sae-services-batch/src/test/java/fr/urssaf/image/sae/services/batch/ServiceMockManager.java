package fr.urssaf.image.sae.services.batch;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.restore.SAERestoreMasseService;
import fr.urssaf.image.sae.services.batch.suppression.SAESuppressionMasseService;

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
      return EasyMock.createMock(SAECaptureMasseService.class);
   }
   
   /**
    * 
    * @return instance de {@link SAESuppressionMasseService}
    */
   public static SAESuppressionMasseService createSAESuppressionMasseService() {
      return EasyMock.createMock(SAESuppressionMasseService.class);
   }
   
   /**
    * 
    * @return instance de {@link SAERestoreMasseService}
    */
   public static SAERestoreMasseService createSAERestoreMasseService() {
      return EasyMock.createMock(SAERestoreMasseService.class);
   }

}
