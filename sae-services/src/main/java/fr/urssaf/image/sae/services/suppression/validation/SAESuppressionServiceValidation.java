package fr.urssaf.image.sae.services.suppression.validation;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAESuppressionServiceValidation
 * 
 * Classe de validation des arguments en entrée des implementations du service
 * SAESuppressionService
 * 
 */
@Aspect
public class SAESuppressionServiceValidation {

   private static final String SAE_DELETE_CLASS = "fr.urssaf.image.sae.services.suppression.SAESuppressionService.";
   private static final String PARAM_DELETE = "execution(void "
         + SAE_DELETE_CLASS + "suppression(*))" + "&& args(uuid)";

   /**
    * Vérifie les paramètres d'entrée de la méthode suppression
    * 
    * @param uuid
    *           identifiant unique du document à supprimer
    */
   @Before(PARAM_DELETE)
   public final void delete(UUID uuid) {
      Validate.notNull(uuid, ResourceMessagesUtils.loadMessage(
            "argument.required", "'identifiant de l'archive'"));
   }
}
