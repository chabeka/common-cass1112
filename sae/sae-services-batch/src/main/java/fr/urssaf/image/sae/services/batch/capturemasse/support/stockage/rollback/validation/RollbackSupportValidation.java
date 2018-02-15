/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.validation;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.RollbackSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class RollbackSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String ROLLBACK_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.RollbackSupport.rollback(*))"
         + " && args(identifiant)";

   /**
    * validation que tous les arguments sont obligatoires pour la methode
    * rollback
    * 
    * @param identifiant
    *           identifiant du document
    */
   @Before(ROLLBACK_METHOD)
   public final void checkRollback(final UUID identifiant) {

      if (identifiant == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "identifiant"));
      }

   }

}
