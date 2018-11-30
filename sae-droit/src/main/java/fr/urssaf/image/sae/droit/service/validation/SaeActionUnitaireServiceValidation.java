/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * SaeActionUnitaireService
 * 
 */
@Aspect
public class SaeActionUnitaireServiceValidation {

   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaeActionUnitaireService.createActionUnitaire(*))"
         + "&& args(actionUnitaire)";

   /**
    * méthode de validation des arguments de la méthode de création
    * 
    * @param actionUnitaire
    *           l'action unitaire à créer
    */
   @Before(CREATE_METHOD)
   public final void checkCreate(final ActionUnitaire actionUnitaire) {

      if (actionUnitaire == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "action unitaire"));
      }

      if (StringUtils.isBlank(actionUnitaire.getCode())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "code action unitaire"));
      }

      if (StringUtils.isBlank(actionUnitaire.getDescription())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "description action unitaire"));
      }

   }

}
