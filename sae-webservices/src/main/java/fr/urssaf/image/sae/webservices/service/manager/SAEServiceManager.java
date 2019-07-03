package fr.urssaf.image.sae.webservices.service.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.SAEServiceProvider;
import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Classe de composant pour récuperer les Services exposé dans l'artefact
 * sae-services<br>
 * 
 * 
 */
@Component
public final class SAEServiceManager {

   @Autowired
   private SAEServiceProvider provider;

   /**
    * 
    * @return l'ensemble des services du SAE
    */
   public SAEDocumentService loadSAEDocumentService() {
      return provider.getSaeDocumentService();
   }

}
