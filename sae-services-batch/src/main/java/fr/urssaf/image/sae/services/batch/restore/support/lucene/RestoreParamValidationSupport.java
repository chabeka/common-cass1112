package fr.urssaf.image.sae.services.batch.restore.support.lucene;

import java.util.UUID;

import fr.urssaf.image.sae.services.batch.restore.exception.RestoreMasseParamValidationException;

/**
 * Composant de validation des droits de restore de masse.
 * 
 */
public interface RestoreParamValidationSupport {

   /**
    * verification des droits de la restore concernant l'identifiant de traitement de suppression.
    * 
    * @param idTraitementSuppression
    *           identifiant de traitement de suppression à restorer
    * @return String
    *       requete lucene contenant la limitation en fonction des droits
    * @throws RestoreMasseParamValidationException
    *           L'identifiant de traitement de suppression est invalide
    */
   String verificationDroitRestore(UUID idTraitementSuppression)
         throws RestoreMasseParamValidationException;
}
