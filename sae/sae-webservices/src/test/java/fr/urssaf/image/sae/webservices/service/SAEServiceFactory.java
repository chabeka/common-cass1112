package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.webservices.service.support.LauncherSupport;

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

      SAEBulkCaptureService service = EasyMock
            .createMock(SAEBulkCaptureService.class);

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
    * @return instance de {@link LauncherSupport}
    */
   public final LauncherSupport createLauncherSupport() {

      LauncherSupport support = EasyMock.createMock(LauncherSupport.class);

      return support;
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
}
