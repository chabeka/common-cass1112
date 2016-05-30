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
    * @param idTraitementRestore
    *           identifiant unique du traitement de restore des documents. 
    *           C'est l'identifiant du job en cours d'exécution.
    * @param idTraitementSuppression
    *           identifiant unique du traitement de supperssion des documents.
    *           C'est l'identifiant du job qui a permis de mettre les documents dans la corbeille.
    *           
    * @return le status du traitement
    */
   @PreAuthorize("hasRole('restore_masse')")
   ExitTraitement restoreMasse(UUID idTraitementRestore, UUID idTraitementSuppression);
}
