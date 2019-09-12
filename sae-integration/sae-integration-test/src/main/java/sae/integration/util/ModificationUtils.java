/**
 *
 */
package sae.integration.util;

import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.ModificationRequestType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Classe utilitaire facilitant la modification de documents
 */
public final class ModificationUtils {

   private ModificationUtils() {
      // Classe statique
   }

   /**
    * Lance une modification unitaire sur un document pour modifier une métadonnée
    * 
    * @param service
    *           L'accès aux services SAE
    * @param UUID
    *           L'identifiant du document à modifier
    * @param metaCode
    *           Le code de la métadonnée à modifier
    * @param metaValue
    *           Le valeur de la métadonnée à positionner
    */
   public static void sendModification(final SaeServicePortType service, final String UUID, final String metaCode, final String metaValue) {
      final ModificationRequestType modifRequest = new ModificationRequestType();
      modifRequest.setUuid(UUID);
      final ListeMetadonneeType modifMetadatas = new ListeMetadonneeType();
      SoapBuilder.setMetaValue(modifMetadatas, metaCode, metaValue);
      modifRequest.setMetadonnees(modifMetadatas);
      service.modification(modifRequest);
   }

}
