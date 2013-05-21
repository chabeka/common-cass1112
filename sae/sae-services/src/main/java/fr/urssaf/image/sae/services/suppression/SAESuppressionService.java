/**
 * 
 */
package fr.urssaf.image.sae.services.suppression;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;

/**
 * Service permettant de réaliser des suppressions de documents
 * 
 */
public interface SAESuppressionService {

   /**
    * Supprime le document donné
    * 
    * @param idArchive
    *           identifiant unique du document à supprimer
    * @throws SuppressionException
    *            Une erreur s'est produite lors de la suppression de l'archive
    */
   @PreAuthorize("hasRole('suppression')")
   void suppression(UUID idArchive) throws SuppressionException;

}
