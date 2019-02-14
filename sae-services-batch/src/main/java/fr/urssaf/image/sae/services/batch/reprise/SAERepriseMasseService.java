/**
 * 
 */
package fr.urssaf.image.sae.services.batch.reprise;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Service de reprise des traitemnts de masse
 * 
 */
public interface SAERepriseMasseService {

   /**
    * Service de reprise des traitements de masse
    * @param idTraitement l'uuid du traitement de reprise
    * @return
    */
   @PreAuthorize("hasRole('reprise_masse')")
   ExitTraitement repriseMasse(UUID idJobReprise);

}
