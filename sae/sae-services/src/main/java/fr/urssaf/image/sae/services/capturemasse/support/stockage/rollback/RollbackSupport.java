/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback;

import java.util.UUID;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;

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
    * @throws InterruptionTraitementException
    *            une exception est levée lors de la reprise du rollback après
    *            une interruption
    * @throws DeletionServiceEx
    *            une exception est levée lors de la suppression du document
    */
   void rollback(UUID identifiant) throws InterruptionTraitementException,
         DeletionServiceEx;

}
