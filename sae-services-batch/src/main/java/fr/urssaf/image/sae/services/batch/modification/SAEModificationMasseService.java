/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification;

import java.net.URI;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Service de modification en masse de documents
 * 
 */
public interface SAEModificationMasseService {

   /**
    * Service de modification en masse
    * 
    * @param sommaireURI
    *           URI ECDE du fichier sommaire.xml
    * @param idTraitement
    *           identifiant unique du traitement
    * @param hash
    *           Le hash du fichier sommaire.xml
    * @param typeHash
    *           algorithme de hash utilisé
    * @return le status du traitement
    */
   @PreAuthorize("hasRole('modification_masse')")
   ExitTraitement modificationMasse(URI sommaireURI, UUID idTraitement,
         String hash, String typeHash);
}
