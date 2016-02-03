/**
 * 
 */
package fr.urssaf.image.sae.services.batch.supression;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Service de supression en masse du SAE
 * 
 */
public interface SAESupressionMasseService {

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
   @PreAuthorize("hasRole('supression_masse')")
   ExitTraitement supressionMasse(UUID idTraitement, String reqLucene);
}
