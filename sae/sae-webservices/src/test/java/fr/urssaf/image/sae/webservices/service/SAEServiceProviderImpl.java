package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.SAEServiceProvider;
import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Implémentation de {@link SAEServiceProvider}
 * 
 * 
 */
@Service
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
