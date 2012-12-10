/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 * 
 */
@Aspect
public class ParametersServiceValidation {

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.ParametersService.";
   private static final String LOAD_METHOD = "execution(fr.urssaf.image.sae.trace.model.Parameter "
         + CLASS_NAME + "loadParameter(*))" + " && args(code)";
   private static final String SAVE_METHOD = "execution(void " + CLASS_NAME
         + "saveParameter(*))" + " && args(parameter)";

   /**
    * Réalise la validation de la méthode lecture de l'interface RegService
    * 
    * @param uuid
    *           identifiant de la trace
    */
   @Before(LOAD_METHOD)
   public final void testLoad(ParameterType code) {

      if (code == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "code"));
      }
   }

   /**
    * Réalise la validation de la méthode purge de l'interface RegService
    * 
    * @param dateDebut
    *           date de début
    * @param dateFin
    *           date de fin
    */
   @Before(SAVE_METHOD)
   public final void testPurge(Parameter parameter) {
      if (parameter == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "parameter"));
      }
   }

}
