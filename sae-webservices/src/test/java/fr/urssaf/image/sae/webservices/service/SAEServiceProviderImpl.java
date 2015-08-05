package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.SAEServiceProvider;
import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Implémentation de {@link SAEServiceProvider}
 * 
 * 
 */

public class SAEServiceProviderImpl implements SAEServiceProvider {

   /**
    * @return Mock de {@link SAEDocumentService}
    */
   @Override
   public final SAEDocumentService getSaeDocumentService() {

      SAEDocumentService service = EasyMock
            .createMock(SAEDocumentService.class);

      return service;
   }

}
