/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse;

import java.net.URI;
import java.util.UUID;

import fr.urssaf.image.sae.services.batch.model.ExitTraitement;

/**
 * Service de capture en masse du SAE
 * 
 */
public interface SAECaptureMasseService {

   /**
    * Service de capture de masse
    * 
    * @param sommaireURL
    *           URL ECDE du fichier sommaire.xml
    * @param idTraitement
    *           identifiant unique du traitement
    * @return le status du traitement
    */
   ExitTraitement captureMasse(URI sommaireURL, UUID idTraitement);

}
