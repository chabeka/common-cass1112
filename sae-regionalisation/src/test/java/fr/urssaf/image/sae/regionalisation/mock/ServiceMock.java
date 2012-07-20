package fr.urssaf.image.sae.regionalisation.mock;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.regionalisation.service.ProcessingService;

/**
 * Service de mock
 * 
 * 
 */
@Component
public final class ServiceMock {

   private ServiceMock() {

   }

   private static ProcessingService processingService;

   /**
    * 
    * @return instance de {@link ProcessingService}
    */
   public static ProcessingService createProcessingService() {

      synchronized (ServiceMock.class) {

         if (processingService == null) {

            processingService = EasyMock.createMock(ProcessingService.class);

         }

      }

      return processingService;
   }
}
