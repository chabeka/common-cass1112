/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse;

import java.net.URI;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

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
   @PreAuthorize("hasRole('archivage_masse')")
   ExitTraitement captureMasse(URI sommaireURL, UUID idTraitement);

   /**
    * Service de capture de masse
    * 
    * @param sommaireURI
    *           URI ECDE du fichier sommaire.xml
    * @param idTraitement
    *           identifiant unique du traitement
    * @param hash
    *            Le hash du fichier sommaire.xml
    * @param typeHash 
    *            algorithme de hash utilisé                     
    * @return le status du traitement
    */
   @PreAuthorize("hasRole('archivage_masse')")
   ExitTraitement captureMasse(URI sommaireURI, UUID idTraitement, String hash, String typeHash);

   
}
