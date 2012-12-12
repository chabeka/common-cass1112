/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * TraitementService
 * 
 */
@Aspect
public class TraitementServiceValidation {

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.executable.service.TraitementService.";
   private static final String PURGE_METHOD = "execution(void " + CLASS_NAME
         + "purgerRegistre(*))" + " && args(typePurge)";

   /**
    * Réalise la validation de la méthode lecture de l'interface RegService
    * 
    * @param typePurge
    *           type de purge
    */
   @Before(PURGE_METHOD)
   public final void testPurge(PurgeType typePurge) {

      if (typePurge == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "type de purge"));
      }
   }

}
