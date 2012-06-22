package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;

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
    * @return instance de {@link SAEControlesCaptureService}
    */
   public final SAEControlesCaptureService createSAEControlesCaptureService() {

      SAEControlesCaptureService service = EasyMock
            .createMock(SAEControlesCaptureService.class);

      return service;
   }


   /**
    * 
    * @return instance de {@link TraitementAsynchroneService}
    */
   public final TraitementAsynchroneService createTraitementAsynchroneService() {

      TraitementAsynchroneService service = EasyMock
            .createMock(TraitementAsynchroneService.class);

      return service;
   }

   /**
    * 
    * @return instance de EcdeServices
    */
   public final EcdeServices createEcdeServices() {

      EcdeServices service = EasyMock.createMock(EcdeServices.class);

      return service;
   }
}
