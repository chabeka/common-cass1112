package fr.urssaf.image.sae.services.batch.restore.support.lucene.validation;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Validation des paramètres passés en arguments des implémentations de
 * {@link fr.urssaf.image.sae.services.batch.restore.support.lucene.RestoreParamValidationSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class RestoreParamValidationSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String VERIF_DROIT_METHOD = "execution(String fr.urssaf.image.sae.services.batch.restore.support.lucene.RestoreParamValidationSupport.verificationDroitRestore(*))"
         + " && args(idTraitementSuppression)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * verificationDroitRestore possède tous les arguments renseignés
    * 
    * @param idTraitementSuppression
    *           Identifiant de suppression de masse
    */
   @Before(VERIF_DROIT_METHOD)
   public final void checkVerificationDroitRestore(final UUID idTraitementSuppression) {

      if (idTraitementSuppression == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "idTraitementSuppression"));
      }

   }
}
