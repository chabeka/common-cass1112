/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback;

import java.util.UUID;

/**
 * Composant pour le rollback des documents persistés dans DFCE en cas d'échec
 * du traitement de capture de masse en mode tout ou rien
 * 
 */
public interface RollbackSupport {

   /**
    * Suppression des documents dans DFCE
    * 
    * @param identifiant
    *           identifiant d'un document à supprimer dans DFCE
    */
   void rollback(UUID identifiant);

}
