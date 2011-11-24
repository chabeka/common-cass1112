package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;

/**
 * Implémentation des Mocks des services SAE
 * 
 * 
 */
@Component
public class SAEServiceFactory {

   /**
    * 
    * @return instance de {@link SAECaptureService}
    */
   public final SAECaptureService createSAECaptureService() {

      SAECaptureService service = EasyMock.createMock(SAECaptureService.class);

      return service;
   }
   
   
   /**
    * 
    * @return instance de {@link SAEBulkCaptureService}
    */
   public final SAEBulkCaptureService createSAEBulkCaptureService() {

	   SAEBulkCaptureService service = EasyMock.createMock(SAEBulkCaptureService.class);

      return service;
   }
}
