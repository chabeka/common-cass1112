/**
 * 
 */
package fr.urssaf.image.sae.services.batch.restore;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Service de restore en masse de documents
 * supprimés
 * 
 */
public interface SAERestoreMasseService {

   /**
    * Service de restore de masse
    * 
    * @param idTraitement
    *           identifiant unique du traitement
    *           
    * @return le status du traitement
    */
   @PreAuthorize("hasRole('restore_masse')")
   ExitTraitement restoreMasse(UUID idTraitement);
}
