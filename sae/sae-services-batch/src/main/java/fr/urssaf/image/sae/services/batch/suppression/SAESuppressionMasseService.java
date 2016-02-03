/**
 * 
 */
package fr.urssaf.image.sae.services.batch.suppression;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Service de suppression en masse du SAE
 * 
 */
public interface SAESuppressionMasseService {

   /**
    * Service de suppression de masse
    * 
    * @param idTraitement
    *           identifiant unique du traitement
    *           
    * @param reqLucene
    *           Requete lucene de recherche des documents à supprimer
    *           
    * @return le status du traitement
    */
   @PreAuthorize("hasRole('suppression_masse')")
   ExitTraitement suppressionMasse(UUID idTraitement, String reqLucene);
}
